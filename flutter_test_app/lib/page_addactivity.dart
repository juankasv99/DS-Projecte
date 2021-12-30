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

  @override
  void initState() {
    super.initState();
    project = widget.project;
    id = project.id;
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
                  });
                  print("Selected Index $index");
                },
                ),
              ],
            ),
            TextFormField(
              decoration: const InputDecoration(
                border: UnderlineInputBorder(),
                labelText: "Name"
              ),
            ),
              
            
          ],
        ),
      ),
    );
  }
}