import 'dart:math';

import 'package:intl/intl.dart';
import 'dart:convert' as convert;

final DateFormat _dateFormatter = DateFormat("yyyy-MM-ddTHH:mm:ss");

abstract class Activity {
  int id;
  String name;
  DateTime? initialDate;
  DateTime? finalDate;
  int duration;
  List<dynamic> children = List<dynamic>.empty(growable: true);

  Activity.fromJson(Map<String, dynamic> json)
    : id = json['id'],
      name = json['name'],
      initialDate = json['initialDate'] == null ? null : _dateFormatter.parse(json['initialDate']),
      finalDate = json['finalDate'] == null ? null : _dateFormatter.parse(json['finalDate']),
      duration = json['duration'];
}


class Project extends Activity {
  Project.fromJson(Map<String, dynamic> json) : super.fromJson(json) {
    if(json.containsKey('children')) {
      for (Map<String, dynamic> jsonChild in json['children']) {
        if(jsonChild['type'].toString().toLowerCase() == 'project') {
          children.add(Project.fromJson(jsonChild));
        } else if (jsonChild['type'].toString().toLowerCase() == 'task') {
          children.add(Task.fromJson(jsonChild));
        } else {
          assert(false);
        }
      }
    }
  }
}


class Task extends Activity {
  bool active = true;
  Task.fromJson(Map<String, dynamic> json) :
            //active = json['active'],
            
            super.fromJson(json) {
    for (Map<String, dynamic> jsonChild in json['intervals']) {
      children.add(Interval.fromJson(jsonChild));
    }
  }
}


class Interval {
  int id;
  DateTime? initialDate;
  DateTime? finalDate;
  int duration;
  bool active;

  Interval.fromJson(Map<String, dynamic> json)
      : id = json['id'] ?? 1, //lo mismo que un operador ternario, si el valor es null el valor es 1, si no es el valor que ya tenga
        initialDate = json['startTime'] == null ? null : _dateFormatter.parse(json['startTime']),
        finalDate = json['endTime'] == null ? null : _dateFormatter.parse(json['endTime']),
        duration = json['duration'],
        //active = json['active'];
        active = false;
}


class Tree {
  late Activity root;

  Tree(Map<String, dynamic> dec) {
    // 1 level tree, root and children only, root is either Project or Task. If Project
    // children are Project or Task, that is, Activity. If root is Task, children are Instance.
    if (dec['type'].toString().toLowerCase() == "project") {
      root = Project.fromJson(dec);
    } else if (dec['type'].toString().toLowerCase() == "task") {
      root = Task.fromJson(dec);
    } else {
      assert(false, "neither project or task");
    }
  }
}


Tree getTree() {
String strJson = "{"
      "\"name\":\"root\", \"class\":\"project\", \"id\":0, \"initialDate\":\"2020-09-22 16:04:56\", \"finalDate\":\"2020-09-22 16:05:22\", \"duration\":26,"
      "\"activities\": [ "
      "{ \"name\":\"software design\", \"class\":\"project\", \"id\":1, \"initialDate\":\"2020-09-22 16:05:04\", \"finalDate\":\"2020-09-22 16:05:16\", \"duration\":16 },"
      "{ \"name\":\"software testing\", \"class\":\"project\", \"id\":2, \"initialDate\": null, \"finalDate\":null, \"duration\":0 },"
      "{ \"name\":\"databases\", \"class\":\"project\", \"id\":3,  \"finalDate\":null, \"initialDate\":null, \"duration\":0 },"
      "{ \"name\":\"transportation\", \"class\":\"task\", \"id\":6, \"active\":false, \"initialDate\":\"2020-09-22 16:04:56\", \"finalDate\":\"2020-09-22 16:05:22\", \"duration\":10, \"intervals\":[] }"
      "] "
      "}";
  Map<String, dynamic> decoded = convert.jsonDecode(strJson);
  Tree tree = Tree(decoded);
  return tree;
}

testLoadTree() {
  Tree tree = getTree();
  print("root name ${tree.root.name}, duration ${tree.root.duration}");
  for (Activity act in tree.root.children) {
    print("child name ${act.name}, duration ${act.duration}");
  }
}

Tree getTreeTask() {
  String strJson = "{"
      "\"name\":\"transportation\",\"class\":\"task\", \"id\":10, \"active\":false, \"initialDate\":\"2020-09-22 13:36:08\", \"finalDate\":\"2020-09-22 13:36:34\", \"duration\":10,"
      "\"intervals\":["
      "{\"class\":\"interval\", \"id\":11, \"active\":false, \"initialDate\":\"2020-09-22 13:36:08\", \"finalDate\":\"2020-09-22 13:36:14\", \"duration\":6 },"
      "{\"class\":\"interval\", \"id\":12, \"active\":false, \"initialDate\":\"2020-09-22 13:36:30\", \"finalDate\":\"2020-09-22 13:36:34\", \"duration\":4}"
      "]}";
  Map<String, dynamic> decoded = convert.jsonDecode(strJson);
  Tree tree = Tree(decoded);
  return tree;
}


void main() {
  testLoadTree();
}