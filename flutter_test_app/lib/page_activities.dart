import 'package:flutter/material.dart';
import 'package:flutter_test_app/page_intervals.dart';
import 'package:flutter_test_app/page_report.dart';
import 'package:flutter_test_app/tree.dart' hide getTree; //old getTree()
import 'package:flutter_test_app/requests.dart';
import 'dart:async';
import 'package:flutter_test_app/util/colors.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class PageActivities extends StatefulWidget {
  final int id;
  
  const PageActivities({Key? key, required this.id}) : super(key: key);

  

  @override
  _PageActivitiesState createState() => _PageActivitiesState();
}

class _PageActivitiesState extends State<PageActivities> {
  //late Tree tree;
  late int id;
  late Future<Tree> futureTree;

  late Timer _timer;
  static const int periodeRefresh = 6;

  @override
  void initState() {
    super.initState();
    id = widget.id; //of PageActivities
    futureTree = getTree(id);
    _activateTimer();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Tree>(
      future: futureTree,
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          return Scaffold(
            appBar: AppBar(
              centerTitle: true,
              backgroundColor: primaryColorRed,
              title: Text(snapshot.data!.root.name,),
              actions: <Widget>[
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
                              
                    padding: const EdgeInsets.symmetric(vertical: 16.0, horizontal: 5),
                    itemCount: snapshot.data!.root.children.length,
                    itemBuilder: (BuildContext context, int index) =>
                    _buildRow(snapshot.data!.root.children[index], index),
                              separatorBuilder: (BuildContext context, int index) =>
                    const Divider(),
                            ),
                ),
              ],),
              
            floatingActionButton: FloatingActionButton(
              onPressed: () {
                print("click add");
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

  Widget _buildRow(Activity activity, int index) {
    String strDuration = Duration(seconds: activity.duration).toString().split('.').first;

    if(activity is Project) {
      return ListTile(
        leading: CircleAvatar(
          child: FaIcon(FontAwesomeIcons.briefcase, color: Colors.white, size: 19,),
          radius: 21.0,
          backgroundColor: primaryColorRedDark,
        ),
        title: Text('${activity.name}'),
        trailing: Text('$strDuration'),
        onTap: () => _navigateDownActivities(activity.id),
      );
    } else if (activity is Task) {
      Task task = activity as Task;
      Widget trailing;
      trailing = Text('$strDuration');
      return ListTile(
        leading: CircleAvatar(
          child: FaIcon(FontAwesomeIcons.listOl, color: Colors.white, size: 20,),
          radius: 21.0,
          backgroundColor: primaryColorRedLight,
        ),
        title: Text('${activity.name}'),
        trailing: trailing,
        onTap: () => _navigateDownIntervals(activity.id),
        onLongPress: () {
          if ((activity as Task).active) {
            stop(activity.id);
            _refresh();
          } else {
            start(activity.id);
            _refresh();
          }
        }, 
      );
    } else {
      throw(Exception("Activity that is neither a Task or a Project"));
    }
  }

  _navigateDownIntervals(int childId) {
    Navigator.of(context)
        .push(MaterialPageRoute(builder: (context) => PageIntervals(childId))).then((var value) {
          _activateTimer();
          _refresh();
        });
  }

  _navigateDownActivities(int childId) {
    _timer.cancel();
    Navigator.of(context)
      .push(MaterialPageRoute<void>(
        builder: (context) => PageActivities(id:childId),
      )).then((var value) {
        _activateTimer();
        _refresh();
        });
  }

  void _refresh() async {
    futureTree = getTree(id);
    setState(() {
      
    });
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

  _sumUpProject({Activity? activity}) {
      return Container(
        padding: EdgeInsets.only(left: 15.0, right: 15.0, top: 5.0),
        child: Column(
          children: <Widget>[
            Row(children: <Widget>[
              Text("Last Task Worked", textAlign: TextAlign.start, style: TextStyle(fontSize: 14.0, fontWeight: FontWeight.w700),),
              Spacer(),
              Text("Project Duration", textAlign: TextAlign.end, style: TextStyle(fontSize: 14.0, fontWeight: FontWeight.w700),)
            ],),
            SizedBox(height: 5.0,),
            Row(children: <Widget>[
              Text("Task Name (Project)", textAlign: TextAlign.start, style: TextStyle(fontSize: 12.0, fontWeight: FontWeight.w600, color: Colors.grey[600]),),
              Spacer(),
              Text("hh:mm:ss", textAlign: TextAlign.start, style: TextStyle(fontSize: 14.0, fontWeight: FontWeight.w600, color: Colors.grey[600]),),
            ],  
            ),
            SizedBox(height: 25.0),
            Row(
              children: <Widget>[
                Text("Started:", textAlign: TextAlign.start, style: TextStyle(fontSize: 20.0, fontWeight: FontWeight.w600),),
                Spacer(),
                Text("DD/MM/AA - hh:mm:ss", textAlign: TextAlign.end, style: TextStyle(fontSize: 16.0, fontWeight: FontWeight.w600, color: Colors.grey[600]),),
              ],
            ),
            SizedBox(height: 2.0,),
            Row(
              children: <Widget>[
                Text("Last Worked:", textAlign: TextAlign.start, style: TextStyle(fontSize: 20.0, fontWeight: FontWeight.w600),),
                Spacer(),
                Text("DD/MM/AA - hh:mm:ss", textAlign: TextAlign.end, style: TextStyle(fontSize: 16.0, fontWeight: FontWeight.w600, color: Colors.grey[600]),),
              ],
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: <Widget>[
                
              IconButton(onPressed: () {}, icon: Icon(Icons.play_circle), iconSize: 45, splashRadius: 30, color: primaryColorRedDark,)
            ],)
            
          ],
        ),
      );
  }

}
