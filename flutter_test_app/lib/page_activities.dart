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
import 'package:flutter_toggle_tab/flutter_toggle_tab.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:intl/intl.dart';

import 'package:flutter_test_app/util/languages.dart' as globals;

class PageActivities extends StatefulWidget {
  final int id;

  const PageActivities({Key? key, required this.id}) : super(key: key);

  @override
  _PageActivitiesState createState() => _PageActivitiesState();
}

class _PageActivitiesState extends State<PageActivities> {
  late int id;
  late Future<Tree.Tree> futureTree;

  late Timer _timer;
  static const int periodeRefresh = 3;

  Widget icon = Icon(Icons.play_circle_rounded);

  List<String> languagesList = ["eng", "cat"];
  Map<String, String> displayList = {"eng" : "English", "cat": "Català"};

  Image flagCat = Image.asset('assets/cat.png', height: 30, width: 30,);
  Image flagEng = Image.asset('assets/eng.png', height: 30, width: 30,);
  Image logo = Image.asset('assets/timetrackerlogo.png');

  List<String> orderList = ["alph", "date"];
  int _selectedIndex = 0;


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
    Map<String, Image> displayImage = {"eng" : flagEng, "cat": flagCat};
    

    return FutureBuilder<Tree.Tree>(
      future: futureTree,
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return Scaffold(
            appBar: drawerFlag(snapshot.data!.root.children.length < 1, snapshot.data!.root.id == 0) ? AppBar(
              centerTitle: true,
              backgroundColor: primaryColorRed,
              title: Text(
                snapshot.data!.root.id == 0 ?
                globals.stringLang[globals.selectedLang]!["home"] :
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
            ) : null,
            drawer: id == 0 ? _buildDrawer(displayImage) : null,
            body: drawerFlag(snapshot.data!.root.children.length < 1, snapshot.data!.root.id == 0) ? Column(
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
            )
            :
            Center(
              child: Padding(
                padding: const EdgeInsets.symmetric(vertical: 150, horizontal: 35),
                child: Column(
                  children: <Widget>[
                    Text(globals.stringLang[globals.selectedLang]!["welcome"].toString().toUpperCase(), 
                    style: TextStyle(fontSize: 30, fontWeight: FontWeight.w900, color: Colors.grey[800],)),
                    logo,
                    SizedBox(height: 40,),
                    Text(globals.stringLang[globals.selectedLang]!["welcomemsg"], textAlign: TextAlign.center,)
                  ],
                ),
              ),
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
  

  Widget _buildRow(Tree.Activity activity, int index) {
    String strDuration =
        Duration(seconds: activity.duration).toString().split('.').first;

    String strStarted = activity.initialDate != null
        ? DateFormat(globals.stringLang[globals.selectedLang]!["formatDate"]).format(activity.initialDate!)
        : "-";
    String strEnded = activity.finalDate != null
        ? DateFormat(globals.stringLang[globals.selectedLang]!["formatDate"]).format(activity.finalDate!)
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
          "${globals.stringLang[globals.selectedLang]!["startedTile"]} $strStarted\n${globals.stringLang[globals.selectedLang]!["toTile"]} $strEnded",
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
          "${globals.stringLang[globals.selectedLang]!["startedTile"]} $strStarted\n${globals.stringLang[globals.selectedLang]!["toTile"]} $strEnded",
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
        ? globals.stringLang[globals.selectedLang]!["NotStartedYet"]
        : DateFormat(globals.stringLang[globals.selectedLang]!["formatDate"]).format(activity.initialDate!);
    String endedString = activity.finalDate == null
        ? globals.stringLang[globals.selectedLang]!["NoWorkingSessionYet"]
        : DateFormat(globals.stringLang[globals.selectedLang]!["formatDate"]).format(activity.finalDate!);

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
                globals.stringLang[globals.selectedLang]!["lastTaskWorked"],
                textAlign: TextAlign.start,
                style: TextStyle(fontSize: 14.0, fontWeight: FontWeight.w700),
              ),
              Spacer(),
              Text(
                globals.stringLang[globals.selectedLang]!["projectDuration"],
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
                globals.stringLang[globals.selectedLang]!["started"],
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
                globals.stringLang[globals.selectedLang]!["lastWorked"],
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

  _buildDrawer(displayImage) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: <Widget>[
          Container(
            height: MediaQuery.of(context).size.height * 0.19,
            child: DrawerHeader(
              padding: EdgeInsets.symmetric(vertical: 24, horizontal: 18),
              decoration: BoxDecoration(
                color: primaryColorRedDark
              ),
              child: Text(globals.stringLang[globals.selectedLang]!["drawerHeader"], style: TextStyle(
                color: Colors.white,
                fontSize: 20.0,
                fontWeight: FontWeight.w500),),
            ),
          ),
          Center(
            child: DropdownButton(
              value: globals.selectedLang,
              icon: const Icon(Icons.arrow_drop_down_rounded),
              hint: Text(globals.stringLang[globals.selectedLang]!["dropdownhint"]),
              items: languagesList
                .map<DropdownMenuItem<String>>((String value) {
                  return DropdownMenuItem<String>(
                    value: value,
                    child: 
                    Container(
                      padding: EdgeInsets.symmetric(vertical: 5),
                      height: 50,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: <Widget>[
                          displayImage[value],
                          SizedBox(width: 20,),
                          Text(displayList[value]!),
                        ],
                      ),
                    ),
                  );
                }).toList(),
                onChanged: (String? value) {
                  
                  setState(() {
                    globals.selectedLang = value!;
                    
                  });
                  Timer(Duration(milliseconds: 750), () {
                    Navigator.pop(context);
                  });
                  
                },
                ),
          ),
          SizedBox(height: MediaQuery.of(context).size.height * 0.55,),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Center(child: Text(globals.stringLang[globals.selectedLang]!["orderSelect"], style: TextStyle(
              fontSize: 15, 
            ),),
            ),
          ),
          Center(
            child: FlutterToggleTab(
              width: 50,
              borderRadius: 10,
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
              labels: [globals.stringLang[globals.selectedLang]!["orderName"], globals.stringLang[globals.selectedLang]!["orderDate"]],
              icons: [Icons.sort_by_alpha, Icons.access_time],
              selectedLabelIndex: (index) {
                setState(() {
                  _selectedIndex = index;
                  globals.selectedOrdr = orderList[index];
                });
              },
            ),
          )
        ],
      ),
    );
  }

  drawerFlag(bool cond1, bool cond2) {
    if(cond1 && cond2) {
      return !true;
    } else {
      return !false;
    }
  }
}
