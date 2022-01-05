import 'dart:convert' as convert;
import 'package:http/http.dart' as http;
import 'tree.dart';

final http.Client client = http.Client();

const String baseUrl = "http://10.0.2.2:8080";
//si se conecta con el movil enchufado hay que hacer la wea del ngrok

//const String baseUrl = "http://a5a5-83-40-155-162.ngrok.io";


Future<Tree> getTree(int id) async {
  var uri = Uri.parse("$baseUrl/get_tree?$id");
  final response = await client.get(uri);

  if(response.statusCode == 200) {
    print("statusCode=$response.statusCode");
    print(response.body);

    Map<String, dynamic> decoded = convert.jsonDecode(response.body);
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
    print("statusCode=$response.statusCode");
    throw Exception("Failed to get children");
  }
  
}



/*
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
*/