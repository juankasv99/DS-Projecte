import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_test_app/page_activities.dart';
import 'package:flutter_test_app/tree.dart' as Tree hide getTree;
import 'package:flutter_test_app/requests.dart';

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
            body: ListView.separated(
              padding: const EdgeInsets.all(16.0),
              itemCount: numChildren,
              itemBuilder: (BuildContext context, int index) =>
                      _buildRow(snapshot.data!.root.children[index], index),
              separatorBuilder: (BuildContext context, int index) =>
              const Divider(),
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
