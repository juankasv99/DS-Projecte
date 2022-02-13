import 'package:flutter/material.dart';
import 'package:flutter_test_app/page_activities.dart';
import 'package:flutter_test_app/page_intervals.dart';
import 'package:flutter_test_app/tree.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'TimeTracker',
      theme: ThemeData(
        //primarySwatch: Colors.red,
        primaryColor: Color(0xFF960000),
        secondaryHeaderColor: Color(0xFF620000),
        textTheme: TextTheme(
          subtitle1: TextStyle(fontSize: 20.0),
          bodyText2: TextStyle(fontSize: 20.0),
        ),
      ),
      home: PageActivities(id:0),
    );
  }
}

