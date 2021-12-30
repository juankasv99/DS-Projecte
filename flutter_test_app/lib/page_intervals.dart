import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_test_app/page_activities.dart';
import 'package:flutter_test_app/tree.dart' as Tree hide getTree;
import 'package:flutter_test_app/requests.dart';
import 'package:flutter_test_app/util/colors.dart';
import 'package:flutter_test_app/util/semicircle.dart';

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
          Tree.Task task = snapshot.data!.root as Tree.Task;
          return Scaffold(
            appBar: AppBar(
              centerTitle: true,
              backgroundColor: primaryColorRed,
              title: Text(task.name),
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
                Container(height: 200, child: _sumUpTask(activity: task),
               ),
                
                //SizedBox(height: 20.0),
                Divider(
                  thickness: 5.0,
                  indent: 12.0,
                  endIndent: 12.0,
                ),
            Expanded(
              child: ListView.separated(
                padding: const EdgeInsets.only(top: 20.0),
                itemCount: numChildren,
                itemBuilder: (BuildContext context, int index) =>
                        _buildRow(snapshot.data!.root.children[index], index),
                separatorBuilder: (BuildContext context, int index) =>
                const Divider(),
              ),
            ),]),
            floatingActionButton: FloatingActionButton(
              onPressed: () {
                task.active ? stop(task.id) : start(task.id);
                print("Task was " + task.active.toString());
                print("Click play");
                setState(() {
                  //task.active = !task.active;
                });
                print("And now is " + task.active.toString());
              },
              backgroundColor: primaryColorRedDark,
              child: task.active ? Icon(Icons.pause_rounded, size: 35,) : Icon(Icons.play_arrow_rounded, size: 35,), //Icon(Icons.play_arrow_rounded, size: 35,),
              
            ),
          );
        }
        else if (snapshot.hasError) {
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
    leading: _generateClock(interval, index),
    title: Text(strInitialDate),
    isThreeLine: true,
    subtitle: Text("From: ${strInitialDate} \nTo: ${strFinalDate}", style: TextStyle(fontSize: 15.0, fontWeight: FontWeight.w400),),//Text('from ${strInitialDate} to ${strFinalDate}'),
    trailing: Text(strDuration),
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
    assert(activity is Tree.Activity);
    Tree.Task task = activity as Tree.Task;
    if(task.active){
      return Container(
      padding: EdgeInsets.only(left: 15.0, right: 15.0, top: 5.0),
      child: Column(
        children: <Widget>[
          Row(children: <Widget>[
            Spacer(),
            Text("Total Duration", textAlign: TextAlign.end, style: TextStyle(fontSize: 17.0, fontWeight: FontWeight.w700),)
          ],),
          SizedBox(height: 3.0,),
          Row(children: <Widget>[
            Spacer(),
            Text("hh:mm:ss", textAlign: TextAlign.start, style: TextStyle(fontSize: 17.0, fontWeight: FontWeight.w600, color: Colors.grey[600]),),
          ],),
          SizedBox(height: 20.0),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
            Text("HH:MM:SS", textAlign: TextAlign.center, style: TextStyle(fontSize: 70.0, fontWeight: FontWeight.bold, color: Colors.grey[800]),),
          ],),
          SizedBox(height: 0.0,),
          Row(mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
            Text("Current Working Period", textAlign: TextAlign.center, style: TextStyle(fontSize: 15.0, fontWeight: FontWeight.w700, color: Colors.grey[600]),),
          ],),
        ],),
    );
    }

    else {
      return Container(
      padding: EdgeInsets.only(left: 15.0, right: 15.0, top: 5.0),
      child: Column(
        children: <Widget>[
          Row(children: <Widget>[
            Text("Last Time Worked", textAlign: TextAlign.start, style: TextStyle(fontSize: 17.0, fontWeight: FontWeight.w700),),
            Spacer(),
            Text("Total Duration", textAlign: TextAlign.end, style: TextStyle(fontSize: 17.0, fontWeight: FontWeight.w700),)
          ],),
          SizedBox(height: 3.0,),
          Row(children: <Widget>[
            Text("Task Name (Project)", textAlign: TextAlign.start, style: TextStyle(fontSize: 14.0, fontWeight: FontWeight.w600, color: Colors.grey[600]),),
            Spacer(),
            Text("hh:mm:ss", textAlign: TextAlign.start, style: TextStyle(fontSize: 17.0, fontWeight: FontWeight.w600, color: Colors.grey[600]),),
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

  _generateClock(Tree.Interval interval, int index) {
    return Stack(
      alignment: Alignment.center,
      children: <Widget>[
        Container(
          width: 40,
          height: 40,
          decoration: BoxDecoration(
            color: Colors.grey.withOpacity(0.2),
            shape: BoxShape.circle,
          ),
        ),
        const SemiCircleWidget(
          diameter: 10,
          //hay que mirar si queremos hacer el tiempo en minutos/horas o el porcentage de la tarea
          //aqui va la variable que cambia el valor
          sweepAngle: (90.0 * 6.0) % 360.0,
          color: primaryColorRedDark,
        ),
        Container(
          height: 30,
          width: 30,
          decoration: BoxDecoration(
            color: Colors.white,
            shape: BoxShape.circle,
            boxShadow: [
              BoxShadow(
                color: Colors.grey.withOpacity(0.2),
                spreadRadius: 5,
                blurRadius: 7,
                offset: Offset(0,3),
              ),
            ],
          ),
        ),
        Text((index+1).toString(), style: TextStyle(fontWeight: FontWeight.bold),)
      ],
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
