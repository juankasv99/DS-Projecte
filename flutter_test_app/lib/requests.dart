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
  var uri = Uri.parse("$baseUrl/add?$name$parent$tag$type");
  final response = await client.get(uri);

  List<String> finalString = tag.replaceAll(" ", "").split(",");

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
    return Task.fromJson(decoded);
  } else {
    print("statusCode=$response.statusCode");
    throw Exception("Failed to get children");
  }
}

/*Future<Interval> getLastWorked(int id) async {
  var uri = Uri.parse("$baseUrl/last?$id");
  final response = await client.get(uri);

  if(response.statusCode == 200) {
    print("statusCode=$response.statusCode");
    print(response.body);

    Map<String, dynamic> decoded = convert.jsonDecode(response.body);
    return Interval.fromJson(decoded);
  } else {
    print("statusCode=$response.statusCode");
    throw Exception("Failed to get last Interval");
  }
}*/