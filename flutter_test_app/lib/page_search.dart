import 'package:flutter/material.dart';
import 'package:flutter_test_app/page_activities.dart';
import 'package:flutter_test_app/page_intervals.dart';
import 'package:flutter_test_app/requests.dart';
import 'package:flutter_test_app/util/colors.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class PageSearch extends StatefulWidget {
  final int id;

  PageSearch({Key? key, required this.id}) : super(key: key);

  @override
  _PageSearchState createState() => _PageSearchState();
}

class _PageSearchState extends State<PageSearch> {

  late int id;
  late List<String> listTags;
  final tagController = TextEditingController();
  List<Map<String, dynamic>> activityList = [];

  late Widget searchResult;

  CircleAvatar projectIcon = CircleAvatar(
          child: FaIcon(
            FontAwesomeIcons.briefcase,
            color: Colors.white,
            size: 19,
          ),
          radius: 21.0,
          backgroundColor: primaryColorRedDark,
        );

  CircleAvatar taskIcon = CircleAvatar(
          child: FaIcon(
            FontAwesomeIcons.listOl,
            color: Colors.white,
            size: 20,
          ),
          radius: 21.0,
          backgroundColor: primaryColorRedLight,
        );

  @override
  void initState() {
    super.initState();
    id = widget.id;

    searchResult = Center(child: Container(child: Text("Waiting for tag search"),));

    getTags(id).then((value) {
      listTags = value;
      setState(() {
        
      });
    });
  }

  @override
  void dispose() {
    tagController.dispose();
    super.dispose();
  }
  

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: primaryColorRedDark,
        title: Container(
          width: double.infinity,
          height: 42,
          decoration: BoxDecoration(
            color: Colors.white, borderRadius: BorderRadius.circular(5)
          ),
          child: Center(
            child: Autocomplete<String>(
              optionsBuilder: (TextEditingValue textEditingValue) {
                if (textEditingValue.text == "") {
                  return const Iterable<String>.empty();
                }
                return listTags.where((String option) {
                  return option.toLowerCase().contains(textEditingValue.text.toLowerCase());
                });
              },
              onSelected: (String selection) {
                debugPrint("Selected $selection");
                searchByTag(id, selection).then((value) {
                  activityList = value;

                  _buildResult();

                  setState(() {
                    
                  });

                  
                });
              },

              fieldViewBuilder: (context, tagController, focusNode, onEditingComplete) {
                return TextField(
                  controller: tagController,
                  focusNode: focusNode,
                  onEditingComplete: onEditingComplete,
                  decoration: InputDecoration(
                    labelStyle: TextStyle(color: Colors.grey[600]),
                    prefixIcon: Icon(Icons.search, color: Colors.grey[600],),
                    suffixIcon: IconButton(
                      icon: Icon(Icons.clear, color: primaryColorRedDark),
                      onPressed: () {
                        tagController.clear();
                        activityList = [];
                        _buildResult();
                        setState(() {
                          
                        });
                      },
                    ),
                    hintText: "Search by tag...",
                    border: InputBorder.none

                  ),
                );
              },

              ),
          ),
        ),
      ),
      body: searchResult
      
    );
  }

  void _buildResult() {
    searchResult = ListView.builder(
      padding: EdgeInsets.symmetric(vertical: 16.0, horizontal: 8.0),
      itemBuilder: (context, index) => ListTile(
        leading: activityList[index]["type"] == "Project" ? projectIcon : taskIcon,
        title: Text(activityList[index]["name"], style: TextStyle(fontSize: 20, color: Colors.grey[800]),),
        subtitle: Text("Tags: " + activityList[index]["tags"].toString().replaceAll("[", "").replaceAll("]", ""),style: TextStyle(
          fontSize: 15,
        ),),
        onTap: () {
          Navigator.of(context).pop();
          if(activityList[index]["type"] == "Project") {
            Navigator.of(context).push(MaterialPageRoute(builder: (context) => PageActivities(id: activityList[index]["id"])));
          } else {
            Navigator.of(context).push(MaterialPageRoute(builder: (context) => PageIntervals(activityList[index]["id"])));
          }
        },
      ),
      itemCount: activityList.length,);
  }

}


