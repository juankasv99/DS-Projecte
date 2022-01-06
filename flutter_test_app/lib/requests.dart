import 'dart:convert' as convert;
import 'package:http/http.dart' as http;
import 'tree.dart';
import 'package:flutter_test_app/util/languages.dart' as globals;

final http.Client client = http.Client();

//const String baseUrl = "http://10.0.2.2:8080";
//si se conecta con el movil enchufado hay que hacer la wea del ngrok

const String baseUrl = "http://48c1-83-40-155-162.ngrok.io";


Future<Tree> getTree(int id) async {
  var uri = Uri.parse("$baseUrl/get_tree?$id");
  final response = await client.get(uri);

  if(response.statusCode == 200) {
    print("statusCode=$response.statusCode");
    print(response.body);

    Map<String, dynamic> decoded = convert.jsonDecode(response.body);
    //decoded["children"] = decoded["children"].sort((a, b) => a["name"].compareTo(b["name"]));

    // decoded["children"].sort((m1, m2) {
    //   var r = m1["name"].compareTo(m2["name"]);
    //   if (r != 0) return r;
    // });

    List<Map<String, dynamic>> newList = List.empty(growable: true);

    if(globals.selectedOrdr == "alph") {

      if(decoded["children"] != null && decoded["children"].length > 0) {
        newList.add(decoded["children"][0]);
        for(int i = 1; i < decoded["children"].length; i++) {

          for(int j = 0; j < newList.length; j++)
          {
            if(decoded["children"][i]["name"].compareTo(newList[j]["name"]) < 0) {
              newList.insert(j, decoded["children"][i]);
              break;
            }
            if(j == newList.length-1){
              newList.insert(j+1, decoded["children"][i]);
              break;
            }
          }

        }
      }
      decoded["children"] = newList;
    }

    else{
      if(decoded["children"] != null && decoded["children"].length > 0) {
        newList.add(decoded["children"][0]);
        for(int i = 1; i < decoded["children"].length; i++) {

          if(decoded["children"][i]["endTime"] == null)
          {
            newList.insert(newList.length, decoded["children"][i]);
            continue;
          }

          for(int j = 0; j < newList.length; j++)
          {
            if(newList[j]["endTime"] == null) {
              newList.insert(j, decoded["children"][i]);
              break;
            }
            if(decoded["children"][i]["endTime"].compareTo(newList[j]["endTime"]) > 0) {
              newList.insert(j, decoded["children"][i]);
              break;
            }
            if(j == newList.length-1){
              newList.insert(j+1, decoded["children"][i]);
              break;
            }
          }

        }
      }

      decoded["children"] = newList;
    }
    
    
    return Tree(decoded);
  } else {
    print("statusCode=$response.statusCode");
    throw Exception("Failed to get children");
  } 
}

Future<void> start(int id) async {
  var uri = Uri.parse("$baseUrl/start?$id");
  final response = await client.get(uri);
  if (response.statusCode == 200) {
    print("statusCode=$response.statusCode");
  } else {
    print("statusCode=$response.statusCode");
    throw Exception('Failed to get children');
  }
}

Future<void> stop(int id) async {
  var uri = Uri.parse("$baseUrl/stop?$id");
  final response = await client.get(uri);
  if (response.statusCode == 200) {
    print("statusCode=$response.statusCode");
  } else {
    print("statusCode=$response.statusCode");
    throw Exception('Failed to get children');
  }
}

Future<void> add(String name, String parent, String tag, String type) async {
  
  String finalString = tag.replaceAll(" ", "");
  
  var uri = Uri.parse("$baseUrl/add?$name&$parent&$finalString&$type");
  final response = await client.get(uri);

  

  if (response.statusCode == 200) {
    print("statusCode=$response.statusCode");
  } else {
    print("statusCode=$response.statusCode");
    throw Exception('Failed to get children');
  }
}

Future<List<Project>> getProjectList(int id) async {
  var uri = Uri.parse("$baseUrl/projects?$id");
  final response = await client.get(uri);

  if(response.statusCode == 200){
    print("statusCode=$response.statusCode");
    print(response.body);

    Map<String, dynamic> decoded = convert.jsonDecode(response.body);

    List<Project> projectList = [];

    print(decoded["projects"].length);

    for(int i = 0; i<decoded["projects"].length; i++){
      projectList.add(Project.fromJson(decoded["projects"][i]));
    }
    return projectList;
  }

  else {
    print("statusCode=$response.statusCode");
    throw Exception("Failed to get children");
  }
  
}

Future<Task> getLastTask(int id) async{
  var uri = Uri.parse("$baseUrl/last?$id");
  final response = await client.get(uri);

  if(response.statusCode == 200) {
    print("statusCode=$response.statusCode");
    print(response.body);

    Map<String, dynamic> decoded = convert.jsonDecode(response.body);


    
    if (decoded["type"] == "Project")
    {
      String newjson = '{"duration":0,"parent":"none","intervals":[],"name":"-","active":false,"id":99,"type":"Task"}';
      Map<String, dynamic> newDecoded = convert.jsonDecode(newjson);
      return Task.fromJson(newDecoded);
    } else {
      return Task.fromJson(decoded);
    }
    
  } else {
    print("statusCode=$response.statusCode");
    throw Exception("Failed to get children");
  }
}


Future<List<String>> getTags(int id) async{
  var uri = Uri.parse("$baseUrl/tags?$id");
  final response = await client.get(uri);

  if(response.statusCode == 200) {
    print("statusCode=${response.statusCode}");
    print(response.body);

    Map<String, dynamic> decoded = convert.jsonDecode(response.body);

    List<String> tagsList = decoded["tags"].cast<String>();

    return tagsList;
  }
  else {
    print("statusCode=${response.statusCode}");
    throw Exception("Failed to get children");
  }
  
}

Future<List<Map<String, dynamic>>> searchByTag(int id, String tag) async{
  var uri = Uri.parse("$baseUrl/search_tag?$id?$tag");
  final response = await client.get(uri);

  List<Map<String, dynamic>> activityList = [];

  if(response.statusCode == 200){
    print(response.body);

    Map<String, dynamic> decoded = convert.jsonDecode(response.body);

    List<dynamic> data = decoded["project_components"];

    

    for(int i = 0; i<data.length; i++) {
      activityList.add(data[i]);
    }

  return activityList;
  }
  else {
    print("statusCode=${response.statusCode}");
    throw Exception("Failed to get children");
  }
}



// Future<List<Project>> getProjectList(int id) async {
//   var uri = Uri.parse("$baseUrl/projects?$id");
//   final response = await client.get(uri);

//   if(response.statusCode == 200){
//     print("statusCode=$response.statusCode");
//     print(response.body);

//     Map<String, dynamic> decoded = convert.jsonDecode(response.body);

//     List<Project> projectList = [];

//     print(decoded["projects"].length);

//     for(int i = 0; i<decoded["projects"].length; i++){
//       projectList.add(Project.fromJson(decoded["projects"][i]));
//     }
//     return projectList;
//   }

//   else {
//     print("statusCode=$response.statusCode");
//     throw Exception("Failed to get children");
//   }
  
// }