import 'dart:convert' as convert;
import 'package:http/http.dart' as http;
import 'tree.dart';

final http.Client client = http.Client();

const String baseUrl = "http://10.0.2.2:8080";
//si se conecta con el movil enchufado hay que hacer la wea del ngrok

//const String baseUrl = "http://528d-158-109-94-83.ngrok.io";


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