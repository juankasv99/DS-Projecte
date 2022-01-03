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
  
  List<String> finalString = tag.replaceAll(" ", "").split(",");
  
  var uri = Uri.parse("$baseUrl/add?$name&$parent&$finalString&$type");
  final response = await client.get(uri);

  

  if (response.statusCode == 200) {
    print("statusCode=$response.statusCode");
  } else {
    print("statusCode=$response.statusCode");
    throw Exception('Failed to get children');
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