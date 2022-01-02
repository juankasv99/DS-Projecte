import 'package:flutter/material.dart';
import 'package:flutter_test_app/page_activities.dart';
import 'package:flutter_test_app/tree.dart' hide getTree;
import 'package:flutter_test_app/requests.dart';
import 'package:flutter_test_app/page_intervals.dart';
import 'dart:async';
import 'package:flutter_test_app/util/colors.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:flutter_toggle_tab/flutter_toggle_tab.dart';


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
  String dropdownValue = "";
  
  String newActivityName = "";
  String newActivityParent = "";
  String newActivityTag = "";
  String newActivityType = "";

  static const List<String> typeList = ["Project", "Task"];
  static const List<String> listTags = ["java", "Dart", "python", "flutter", "Java", "IntelliJ", "c++", "SQL", "C++"];

  @override
  void initState() {
    super.initState();
    project = widget.project;
    id = project.id;
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
      appBar: AppBar(
        centerTitle: true,
        backgroundColor: primaryColorRed,
        title: Text("New Activity"),
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
                  labels: ["Project","Task"],
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
                        border: UnderlineInputBorder(), labelText: "Name"),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return "Please enter a name";
                        }
                        return null;
                      },
                    ),

                    SizedBox(height: 10,),
                    
                    DropdownButtonFormField(
                      //value: project.name,
                      onSaved: (newValue) => newActivityParent,
                      icon: const Icon(Icons.arrow_drop_down_rounded),
                      hint: Text("Directory"),
                      elevation: 16,
                      //dropdownColor: primaryColorRedLight,
                      onChanged: (String? newValue) {
                        setState(() {
                          dropdownValue = newValue!;
                        });
                      },
                      items: <String>["Home", "software testing", "databases"]
                        .map<DropdownMenuItem<String>>((String value){
                          return DropdownMenuItem<String>(
                            value: value,
                            child: Text(value)
                          );
                        }).toList(),
                      validator: (value) {
                        if (value == null) {
                          return "Please select the project directory";
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
                        border: UnderlineInputBorder(), labelText: "Tag"),
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
                    
                    SizedBox(height: 25,),
        
                    ElevatedButton(
                      style: ButtonStyle(
                        backgroundColor: MaterialStateProperty.all<Color>(primaryColorRedDark),
                      ),
                      onPressed: () {
                        if(_formKey.currentState!.validate()) {
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(content: Text("Creating ${nameController.text}"))
                          );
                          add(newActivityName, newActivityParent, newActivityTag, newActivityType);
                        }
                        },
                      child: const Text("Save"))
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