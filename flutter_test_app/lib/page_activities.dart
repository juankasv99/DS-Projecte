import 'package:flutter/material.dart';
import 'package:flutter_test_app/page_addactivity.dart';
import 'package:flutter_test_app/page_intervals.dart';
import 'package:flutter_test_app/page_report.dart';
import 'package:flutter_test_app/page_search.dart';
import 'package:flutter_test_app/tree.dart' as Tree
    hide getTree; //old getTree()
import 'package:flutter_test_app/requests.dart';
import 'dart:async';
import 'package:flutter_test_app/util/colors.dart';
import 'package:flutter_test_app/util/functions.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:intl/intl.dart';

class PageActivities extends StatefulWidget {
  final int id;

  const PageActivities({Key? key, required this.id}) : super(key: key);

  @override
  _PageActivitiesState createState() => _PageActivitiesState();
}

class _PageActivitiesState extends State<PageActivities> {
  //late Tree tree;
  late int id;
  late Future<Tree.Tree> futureTree;

  late Timer _timer;
  static const int periodeRefresh = 3;

  Widget icon = Icon(Icons.play_circle_rounded);

  Tree.Task? lastWorkedTask;

  @override
  void initState() {
    super.initState();
    id = widget.id; //of PageActivities
    futureTree = getTree(id);

    _activateTimer();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Tree.Tree>(
      future: futureTree,
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return Scaffold(
            appBar: AppBar(
              centerTitle: true,
              backgroundColor: primaryColorRed,
              title: Text(
                snapshot.data!.root.name,
              ),
              actions: <Widget>[
                IconButton(icon: Icon(Icons.search),
                onPressed: () {
                  _navigateSearch(id);
                },),
                
                IconButton(
                    icon: Icon(Icons.home),
                    onPressed: () {
                      while (Navigator.of(context).canPop()) {
                        print("pop");
                        Navigator.of(context).pop();
                      }
                      PageActivities(id: 0);
                    }),
                
                //TODO: other actions
              ],
            ),
            body: Column(
              children: <Widget>[
                _sumUpProject(activity: snapshot.data!.root),
                SizedBox(height: 5.0),
                Divider(
                  thickness: 5.0,
                  indent: 12.0,
                  endIndent: 12.0,
                ),
                Expanded(
                  child: ListView.separated(
                    //scrollDirection: Axis.vertical,
                    //shrinkWrap: true,
                    
                    padding: const EdgeInsets.symmetric(
                        vertical: 16.0, horizontal: 5),
                    itemCount: snapshot.data!.root.children.length,
                    itemBuilder: (BuildContext context, int index) =>
                        _buildRow(snapshot.data!.root.children[index], index),
                    separatorBuilder: (BuildContext context, int index) =>
                        const Divider(),
                  ),
                ),
              ],
            ),
            floatingActionButton: FloatingActionButton(
              onPressed: () {
                print("click add");
                _createPage(snapshot.data!.root as Tree.Project);
              },
              backgroundColor: primaryColorRedDark,
              child: const Icon(Icons.add),
            ),
          );
        } else if (snapshot.hasError) {
          return Text("${snapshot.error}");
        }

        return Container(
            height: MediaQuery.of(context).size.height,
            color: Colors.white,
            child: Center(
              child: CircularProgressIndicator(),
            ));
      },
    );
  }
  /*
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(tree.root.name),
        actions: <Widget>[
          IconButton(icon: const Icon(Icons.home), onPressed: () {
            //YA ESTAMOS ALLÍ, NO HACE FALTA MOVERSE
          }  
              ),
          IconButton(icon: const Icon(Icons.addchart),
                      onPressed: () {
                        Navigator.of(context)
                        .push(MaterialPageRoute<void>(
                          builder: (context) => const PageReport(),
                        ));
                      },)
          
        ],
      ),
      body: ListView.separated(
        padding: const EdgeInsets.all(16.0),
        itemCount: tree.root.children.length,
        itemBuilder: (BuildContext context, int index) =>
            _buildRow(tree.root.children[index], index),
        separatorBuilder: (BuildContext context, int index) => const Divider(),
      ),
    );
  }

  Widget _buildRow(Activity activity, int index) {
    String strDuration =
        Duration(seconds: activity.duration).toString().split('.').first;
    // split by '.' and taking first element of resulting list
    // removes the microseconds part
    if (activity is Project) {
      return ListTile(
        title: Text(activity.name),
        trailing: Text(strDuration),
        onTap: () => {},
        // TODO, navigate down to show children tasks and projects
      );
    } else if (activity is Task) {
      Task task = activity as Task;
      Widget trailing;
      trailing = Text(strDuration);
      return ListTile(
        title: Text(activity.name),
        trailing: trailing,
        onTap: () => _navigateDownIntervals(index),
        onLongPress: () {},
        // TODO start/stop counting the time for tis task
      );
    } else {
      throw (Exception("Activity that is neither a Task or a Project"));
    }
  } 
  */

  Widget _buildRow(Tree.Activity activity, int index) {
    String strDuration =
        Duration(seconds: activity.duration).toString().split('.').first;

    String strStarted = activity.initialDate != null
        ? DateFormat('dd/MM/yy - HH:mm:ss').format(activity.initialDate!)
        : "-";
    String strEnded = activity.finalDate != null
        ? DateFormat('dd/MM/yy - HH:mm:ss').format(activity.finalDate!)
        : "-";

    if (activity is Tree.Project) {
      return ListTile(
        leading: CircleAvatar(
          child: FaIcon(
            FontAwesomeIcons.briefcase,
            color: Colors.white,
            size: 19,
          ),
          radius: 21.0,
          backgroundColor: primaryColorRedDark,
        ),
        title: Text('${activity.name}'),
        trailing: Text('$strDuration', style: TextStyle(fontSize: 19),),
        subtitle: Text(
          "Started: $strStarted\nTo: $strEnded",
          style: TextStyle(fontSize: 14),
        ),
        onTap: () => _navigateDownActivities(activity.id),
      );
    } else if (activity is Tree.Task) {
      Tree.Task task = activity as Tree.Task;
      Widget trailing;

      trailing = new Container(
        height: 80,
        child: Column(
          children: <Widget>[
            Flexible(
              flex: 4,
              child: IconButton(
                onPressed: () {
                  task.active ? stop(task.id) : start(task.id);
                  setState(() {
                    getLastTask(activity.id).then((val) {
                      lastWorkedTask = val;
                    });
                    if (lastWorkedTask == null) {
                      icon = Icon(Icons.play_circle_rounded);
                    } else {
                      lastWorkedTask!.active
                          ? icon = Icon(Icons.play_circle_rounded)
                          : icon = Icon(Icons.pause_circle_rounded);
                    }
                  });
                },
                icon: task.active
                    ? Icon(Icons.pause_circle_rounded)
                    : Icon(Icons.play_circle_rounded),
                iconSize: 40,
                splashRadius: 25,
                color: primaryColorRedDark,
                padding: const EdgeInsets.all(0.0),
              ),
            ),
            Flexible(
              flex: 2,
              child: Text('$strDuration', style: TextStyle(fontSize: 19),),
            ),
          ],
        ),
      );

      return ListTile(
        minVerticalPadding: 0,
        //dense: true,
        leading: CircleAvatar(
          child: FaIcon(
            FontAwesomeIcons.listOl,
            color: Colors.white,
            size: 20,
          ),
          radius: 21.0,
          backgroundColor: primaryColorRedLight,
        ),
        title: Text('${activity.name}', style: TextStyle(fontSize: 20),),
        trailing: trailing,
        subtitle: Text(
          "Started: $strStarted\nTo: $strEnded",
          style: TextStyle(fontSize: 14),
        ),
        onTap: () => navigateDownIntervals(activity.id),
        onLongPress: () {
          /*if ((activity as Tree.Task).active) {
            stop(activity.id);
            _refresh();
          } else {
            start(activity.id);
            _refresh();
          }*/
        },
      );
    } else {
      throw (Exception("Activity that is neither a Task or a Project"));
    }
  }

  _navigateSearch(int actualId) {
    Navigator.of(context)
      .push(MaterialPageRoute(builder: (context) => PageSearch(id: actualId)))
      .then((var value) {
        _activateTimer();
        _refresh();
      });
  }

  navigateDownIntervals(int childId) {
    Navigator.of(context)
        .push(MaterialPageRoute(builder: (context) => PageIntervals(childId)))
        .then((var value) {
      _activateTimer();
      _refresh();
    });
  }

  _navigateDownActivities(int childId) {
    _timer.cancel();
    Navigator.of(context)
        .push(MaterialPageRoute<void>(
      builder: (context) => PageActivities(id: childId),
    ))
        .then((var value) {
      _activateTimer();
      _refresh();
    });
  }

  _createPage(Tree.Project project) {
    _timer.cancel();
    Navigator.of(context)
        .push(MaterialPageRoute<void>(
      builder: (context) => PageAddActivity(project: project),
    ))
        .then((var value) {
      _activateTimer();
      _refresh();
    });
    print("Entered create page from ${project.name} page");
  }

  void _refresh() async {
    futureTree = getTree(id);
    setState(() {});
  }

  void _activateTimer() {
    _timer = Timer.periodic(Duration(seconds: periodeRefresh), (Timer t) {
      futureTree = getTree(id);
      setState(() {});
    });
  }

  @override
  void dispose() {
    _timer.cancel();
    super.dispose();
  }

  _sumUpProject({Tree.Activity? activity}) {
    if (activity!.finalDate != null) {
      getLastTask(activity.id).then((val) {
        lastWorkedTask = val;
      });
    }

    String startedString = activity.initialDate == null
        ? "Not Started Yet"
        : DateFormat('dd/MM/yy - HH:mm:ss').format(activity.initialDate!);
    String endedString = activity.finalDate == null
        ? "No Working Session Yet"
        : DateFormat('dd/MM/yy - HH:mm:ss').format(activity.finalDate!);

    //Widget icon = Icon(Icons.play_circle_rounded);

    if (lastWorkedTask == null) {
      icon = Icon(Icons.play_circle_rounded);
    } else {
      lastWorkedTask!.active
          ? Icon(Icons.pause_circle_rounded)
          : Icon(Icons.play_circle_rounded);
    }

    return Container(
      padding: EdgeInsets.only(left: 15.0, right: 15.0, top: 5.0),
      child: Column(
        children: <Widget>[
          Row(
            children: <Widget>[
              Text(
                "Last Task Worked",
                textAlign: TextAlign.start,
                style: TextStyle(fontSize: 14.0, fontWeight: FontWeight.w700),
              ),
              Spacer(),
              Text(
                "Project Duration",
                textAlign: TextAlign.end,
                style: TextStyle(fontSize: 14.0, fontWeight: FontWeight.w700),
              )
            ],
          ),
          SizedBox(
            height: 5.0,
          ),
          Row(
            children: <Widget>[
              Text(
                activity.finalDate == null
                    ? "-"
                    : titleCase(lastWorkedTask?.name ?? "-"),
                textAlign: TextAlign.start,
                style: TextStyle(
                    fontSize: 12.0,
                    fontWeight: FontWeight.w600,
                    color: Colors.grey[600]),
              ),
              Spacer(),
              Text(
                printDuration(activity.duration),
                textAlign: TextAlign.start,
                style: TextStyle(
                    fontSize: 14.0,
                    fontWeight: FontWeight.w600,
                    color: Colors.grey[600]),
              ),
            ],
          ),
          SizedBox(height: 25.0),
          Row(
            children: <Widget>[
              Text(
                "Started:",
                textAlign: TextAlign.start,
                style: TextStyle(fontSize: 20.0, fontWeight: FontWeight.w600),
              ),
              Spacer(),
              Text(
                startedString,
                textAlign: TextAlign.end,
                style: TextStyle(
                    fontSize: 16.0,
                    fontWeight: FontWeight.w600,
                    color: Colors.grey[600]),
              ),
            ],
          ),
          SizedBox(
            height: 2.0,
          ),
          Row(
            children: <Widget>[
              Text(
                "Last Worked:",
                textAlign: TextAlign.start,
                style: TextStyle(fontSize: 20.0, fontWeight: FontWeight.w600),
              ),
              Spacer(),
              Text(
                endedString,
                textAlign: TextAlign.end,
                style: TextStyle(
                    fontSize: 16.0,
                    fontWeight: FontWeight.w600,
                    color: Colors.grey[600]),
              ),
            ],
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: <Widget>[
              IconButton(
                onPressed: () {
                  lastWorkedTask!.active
                      ? stop(lastWorkedTask!.id)
                      : start(lastWorkedTask!.id);
                  setState(() {
                    getLastTask(activity.id).then((val) {
                      lastWorkedTask = val;
                    });

                    if (lastWorkedTask == null) {
                      icon = Icon(Icons.play_circle_rounded);
                    } else {
                      lastWorkedTask!.active
                          ? icon = Icon(Icons.play_circle_rounded)
                          : icon = Icon(Icons.pause_circle_rounded);
                    }
                  });
                },
                icon: icon,
                iconSize: 45,
                splashRadius: 30,
                color: primaryColorRedDark,
              )
            ],
          )
        ],
      ),
    );
  }
}
