import 'package:flutter/material.dart';
import 'package:flutter_test_app/page_activities.dart';
import 'package:flutter_test_app/tree.dart' hide getTree;
import 'package:flutter_test_app/requests.dart';
import 'package:flutter_test_app/page_intervals.dart';
import 'dart:async';
import 'package:flutter_test_app/util/colors.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:flutter_toggle_tab/flutter_toggle_tab.dart';

import 'package:flutter_test_app/util/languages.dart' as globals;


class PageAddActivity extends StatefulWidget {
  final Project project;
  
  const PageAddActivity({ Key? key, required this.project }) : super(key: key);

  @override
  _PageAddActivityState createState() => _PageAddActivityState();
}

class _PageAddActivityState extends State<PageAddActivity> {

  late Project project;
  late int id;
  int _selectedIndex = 0;
  final _formKey = GlobalKey<FormState>();
  final nameController = TextEditingController();
  final tagController = TextEditingController();
  
  String newActivityName = "";
  String newActivityParent = "";
  String newActivityTag = "";
  String newActivityType = "Project";

  static const List<String> typeList = ["Project", "Task"];
  static List<String> listTags = [];

  static  List<Project> projectList = [];

  @override
  void initState() {
    super.initState();
    project = widget.project;
    id = project.id;

    //projectList = getProjectList(id) as List<Project>;
    getProjectList(id).then((value) {
      projectList = value;
      setState(() {
        
      });
    });

    getTags(id).then((value) {
      listTags = value;
      setState(() {
        
      });
    });
  }

  @override
  void dispose() {
    nameController.dispose();
    tagController.dispose();
    super.dispose();
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        centerTitle: true,
        backgroundColor: primaryColorRed,
        title: Text(globals.stringLang[globals.selectedLang]!["titlePage"]),
        actions: <Widget>[
          IconButton(
              icon: Icon(Icons.home),
              onPressed: () {
                while (Navigator.of(context).canPop()) {
                  print("pop");
                  Navigator.of(context).pop();
                }
                PageActivities(id: 0);
              })
        ],
      ),
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 8.0, vertical: 16.0),
        child: Form(
          key: _formKey,
          child: Column(
            children: <Widget>[
              SizedBox(height: 20,),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  FlutterToggleTab(
                    width: 85,
                    borderRadius: 15,
                    selectedIndex: _selectedIndex,
                    selectedTextStyle: TextStyle(
                      color: Colors.white,
                      fontSize: 18,
                      fontWeight: FontWeight.w600
                    ),
                    unSelectedTextStyle: TextStyle(
                      color: primaryColorRedLight,
                      fontSize: 14,
                      fontWeight: FontWeight.w400
                    ),
                  labels: [globals.stringLang[globals.selectedLang]!["projectOption"], globals.stringLang[globals.selectedLang]!["taskOption"]],
                  icons: [FontAwesomeIcons.suitcase, FontAwesomeIcons.listOl],
                  selectedLabelIndex: (index) {
                    setState(() {
                      _selectedIndex = index;
                     newActivityType = typeList[index];
                    });
                    print("Selected Index $index");
                    print(newActivityType);
                  },
                  ),
                ],
              ),
              Container(
                padding: EdgeInsets.symmetric(vertical: 8.0, horizontal: 24.0),
                child: Column(
                  children: <Widget>[
                    TextFormField(
                      onSaved: (newValue) => newActivityName = newValue!,
                      controller: nameController,
                      textCapitalization: TextCapitalization.words,
                      decoration: InputDecoration(
                        labelStyle: TextStyle(
                          color: Colors.grey[600]
                        ),
                        focusedBorder: UnderlineInputBorder(
                          borderSide: BorderSide(
                            color: primaryColorRed
                          )
                        ),
                        border: UnderlineInputBorder(), labelText: globals.stringLang[globals.selectedLang]!["nameTip"],),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return globals.stringLang[globals.selectedLang]!["errorName"];
                        }
                        return null;
                      },
                    ),

                    SizedBox(height: 10,),
                    
                    DropdownButtonFormField(
                      //value: project.name,
                      onSaved: (newValue) => newActivityParent,
                      icon: const Icon(Icons.arrow_drop_down_rounded),
                      hint: Text(globals.stringLang[globals.selectedLang]!["directoryTip"],),
                      elevation: 16,
                      //dropdownColor: primaryColorRedLight,
                      onChanged: (String? newValue) {
                        setState(() {
                          newActivityParent = newValue!;
                        });
                      },
                      items: projectList
                        .map<DropdownMenuItem<String>>((Project value){
                          return DropdownMenuItem<String>(
                            value: value.id.toString(),
                            child: Text(value.name)
                          );
                        }).toList(),
                      validator: (value) {
                        if (value == null) {
                          return globals.stringLang[globals.selectedLang]!["errorDirectory"];
                        }
                        return null;
                      },
                      ),

                    SizedBox(height: 5,),

                    TextFormField(
                      onSaved: (newValue) => newActivityTag = newValue!,
                      controller: tagController,
                      decoration: InputDecoration(
                        labelStyle: TextStyle(
                          color: Colors.grey[600]
                        ),
                        focusedBorder: UnderlineInputBorder(
                          borderSide: BorderSide(
                            color: primaryColorRed
                          )
                        ),
                        border: UnderlineInputBorder(), labelText: globals.stringLang[globals.selectedLang]!["tagTip"],),
                    ),

                    // Autocomplete<String>(
                    //   optionsBuilder: (TextEditingValue textEditingValue) {
                    //     if (textEditingValue.text == "") {
                    //       return const Iterable<String>.empty();
                    //     }
                    //     return listTags.where((String option) {
                    //       return option.toLowerCase().contains(textEditingValue.text.toLowerCase());
                    //     });
                    //   },
                      
                    //   onSelected: (String selection) {
                    //     debugPrint("Selected $selection");
                    //   },
                    //   fieldViewBuilder: (context, controller, focusNode, onEditingComplete) {
                    //     return TextField(
                    //       controller: controller,
                    //       focusNode: focusNode,
                    //       onEditingComplete: onEditingComplete,
                    //       decoration: InputDecoration(
                    //         labelStyle: TextStyle(color: Colors.grey[600]),
                    //         border: UnderlineInputBorder(
                    //             borderSide: BorderSide(color: primaryColorRed)),
                    //         focusedBorder: UnderlineInputBorder(
                    //             borderSide: BorderSide(color: primaryColorRed)),
                    //         enabledBorder: UnderlineInputBorder(
                    //             borderSide:
                    //                 BorderSide(color: Colors.grey[600]!)),
                    //         labelText: "Tag",
                    //       ),
                    //     );
                    //   },
                    // ),
                    
                    SizedBox(height: 45,),
                    
                    ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        //fixedSize: MediaQuery.of(context).size.width,
                        primary: primaryColorRedDark,
                        elevation: 3,
                        onPrimary: Colors.white,
                        minimumSize: Size(230,40),
                      ),
                      onPressed: () {
                        if(_formKey.currentState!.validate()) {
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(content: Text("${globals.stringLang[globals.selectedLang]!["savemsg"]} ${nameController.text}"))
                          );
                          _formKey.currentState!.save();
                          add(newActivityName, newActivityParent, newActivityTag, newActivityType);

                          Navigator.of(context).pop();
                        }
                        },
                      child: Text(globals.stringLang[globals.selectedLang]!["saveTip"], style: TextStyle(fontSize: 17),))
                  ],
                ),
              ),
              
                
              
            ],
          ),
        ),
      ),
    );
  }
}