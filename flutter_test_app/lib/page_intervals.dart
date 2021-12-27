import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_test_app/page_activities.dart';
import 'package:flutter_test_app/tree.dart' as Tree hide getTree;
import 'package:flutter_test_app/requests.dart';
import 'package:flutter_test_app/util/colors.dart';

class PageIntervals extends StatefulWidget {
  final int id;

  const PageIntervals(this.id, {Key? key}) : super(key: key);

  @override
  _PageIntervalsState createState() => _PageIntervalsState();
}

class _PageIntervalsState extends State<PageIntervals> {
  late int id;
  late Future<Tree.Tree> futureTree;

  late Timer _timer;
  static const int periodeRefresh = 6;

  @override
  void initState() {
    super.initState();
    id = widget.id;
    futureTree = getTree(id);
    // the root is a task and the children its intervals
    _activateTimer();
  }

  @override

  Widget build(BuildContext context) {
    return FutureBuilder<Tree.Tree>(
      future: futureTree,
      builder: (context, snapshot) {
        if(snapshot.hasData) {
          int numChildren = snapshot.data!.root.children.length;
          return Scaffold(
            appBar: AppBar(
              centerTitle: true,
              backgroundColor: primaryColorRed,
              title: Text(snapshot.data!.root.name),
              actions: <Widget>[
                IconButton(icon: Icon(Icons.home),
                onPressed: () {
                  while(Navigator.of(context).canPop()) {
                    print("pop");
                    Navigator.of(context).pop();
                  }
                  PageActivities(id: 0);
                }, 
                )
              ],
            ),
            body: Column( 
              children: <Widget>[
                _sumUpTask(activity: snapshot.data!.root),
                SizedBox(height: 15.0),
                Divider(
                  thickness: 5.0,
                  indent: 12.0,
                  endIndent: 12.0,
                ),
            Expanded(
              child: ListView.separated(
                padding: const EdgeInsets.all(16.0),
                itemCount: numChildren,
                itemBuilder: (BuildContext context, int index) =>
                        _buildRow(snapshot.data!.root.children[index], index),
                separatorBuilder: (BuildContext context, int index) =>
                const Divider(),
              ),
            ),]),
            floatingActionButton: FloatingActionButton(
              onPressed: () {
              },
              backgroundColor: primaryColorRedDark,
              
            ),
          );
        } else if (snapshot.hasError) {
          return Text("${snapshot.error}");
        }
        return Container(
          height: MediaQuery.of(context).size.height,
          color: Colors.white,
          child: const Center(
            child: CircularProgressIndicator(),
          ),
        );
      },
    );
  }

  Widget _buildRow(Tree.Interval interval, int index) {
  String strDuration = Duration(seconds: interval.duration)
      .toString()
      .split('.')
      .first;
  String strInitialDate = interval.initialDate.toString().split('.')[0];
  // this removes the microseconds part
  String strFinalDate = interval.finalDate.toString().split('.')[0];
  return ListTile(
    title: Text('from ${strInitialDate} to ${strFinalDate}'),
    trailing: Text('$strDuration'),
  );

}

void _activateTimer() {
  _timer = Timer.periodic(Duration(seconds: periodeRefresh), (Timer t) {
    futureTree = getTree(id);
    setState(() {
      
    });
  });
}

@override
void dispose() {
  _timer.cancel();
  super.dispose();
}

  _sumUpTask({Tree.Activity? activity}) {
    return Container(
      padding: EdgeInsets.only(left: 15.0, right: 15.0, top: 5.0),
      child: Column(
        children: <Widget>[
          Row(children: <Widget>[
            Text("Last Time Worked", textAlign: TextAlign.start, style: TextStyle(fontSize: 14.0, fontWeight: FontWeight.w700),),
            Spacer(),
            Text("Total Duration", textAlign: TextAlign.end, style: TextStyle(fontSize: 14.0, fontWeight: FontWeight.w700),)
          ],),
          SizedBox(height: 5.0,),
          Row(children: <Widget>[
            Text("Task Name (Project)", textAlign: TextAlign.start, style: TextStyle(fontSize: 12.0, fontWeight: FontWeight.w600, color: Colors.grey[600]),),
            Spacer(),
            Text("hh:mm:ss", textAlign: TextAlign.start, style: TextStyle(fontSize: 14.0, fontWeight: FontWeight.w600, color: Colors.grey[600]),),
          ],),
          SizedBox(height: 20.0),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
            Text("Started on:", textAlign: TextAlign.center, style: TextStyle(fontSize: 30.0, fontWeight: FontWeight.bold),),
          ],),
          SizedBox(height: 5.0,),
          Row(mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
            Text("DD/MM/AA - hh:mm:ss", textAlign: TextAlign.center, style: TextStyle(fontSize: 25.0, fontWeight: FontWeight.w700, color: Colors.grey[600]),),
          ],),
        ],),
    );
  }
}

  /*
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
      title: Text(tree.root.name),
      actions: <Widget>[
        IconButton(icon: Icon(Icons.home),
                  onPressed: () {      
            Navigator.of(context).pushReplacement(MaterialPageRoute<void>(
              builder: (context) => const PageActivities(),
              ));
                  }
        ),
        //TODO other actions
      ],
      ),
      body: ListView.separated(
      // it's like ListView.builder() but better because it includes a
      // separator between items
      padding: const EdgeInsets.all(16.0),
      itemCount: tree.root.children.length, // number of intervals
      itemBuilder: (BuildContext context, int index) =>
        _buildRow(tree.root.children[index], index),
      separatorBuilder: (BuildContext context, int index) =>
        const Divider(),
      ),
    );
  }
}

*/
