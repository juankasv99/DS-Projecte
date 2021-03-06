package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.StringTokenizer;
import main.Project;
import main.ProjectComponent;
import main.Savejsonvisitor;
import main.Task;
import org.json.JSONArray;
import org.json.JSONObject;
import searchbytag.SearchByTagVisitor;


// Based on
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// http://www.jcgonzalez.com/java-socket-mini-server-http-example

/**
 * Aquesta classe es la que ens fa de enpoint.
 * Aqui es on es conectara la aplicacio i fara crides.
 * demanant diferents tipus de serveis.
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */
public class WebServer {
  private static final int PORT = 8080; // port to listen to

  private final ProjectComponent currentActivity;
  private final ProjectComponent root;

  /**
   * En el constructor s'inicia el ServerSocket.
   * Aquest es queda en un bucle infinit escoltant.
   * Quan li arribi una petició la avalua i si esta contemplada la resol.
   *
   * @author Grup 1 Torn 422.
   */
  public WebServer(ProjectComponent root) {
    this.root = root;
    System.out.println(root);
    currentActivity = root;

    try {
      ServerSocket serverConnect = new ServerSocket(PORT);
      System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
      // we listen until user halts server execution
      while (true) {
        // each client connection will be managed in a dedicated Thread
        new SocketThread(serverConnect.accept());
        // create dedicated thread to manage the client connection
      }
    } catch (IOException e) {
      System.err.println("Server Connection error : " + e.getMessage());
    }
  }

  private ProjectComponent findActivityById(int id) {
    SearchByIdVisitor searchByIdVisitor = SearchByIdVisitor.getInstance(root);
    return searchByIdVisitor.search(id);
  }

  private ProjectComponent findLastWorkedTask(ProjectComponent root) {
    LastWorkedVisitor lastWorkedVisitor = new LastWorkedVisitor(root);
    return lastWorkedVisitor.search();
  }

  private ArrayList<ProjectComponent> getProjectList(ProjectComponent root) {
    ProjectListVisitor projectListVisitor = new ProjectListVisitor(root);
    return projectListVisitor.getProjectList();
  }

  private ArrayList<String> getTagList(ProjectComponent root) {
    TagListVisitor tagListVisitor = new TagListVisitor(root);
    return tagListVisitor.getTagList();
  }

  private ArrayList<ProjectComponent> getProjectComponentsByTag(ProjectComponent root, String tag) {
    SearchByTagVisitor searchByTagVisitor = new SearchByTagVisitor(root);
    return searchByTagVisitor.getProjectComponentList(tag);
  }

  private void saveJson() {
    Savejsonvisitor savejsonvisitor = new Savejsonvisitor();
    this.root.acceptVisitor(savejsonvisitor);
    savejsonvisitor.save("src/main/test.json");
  }

  private class SocketThread extends Thread {
    // SocketThread sees WebServer attributes
    private final Socket insocked;
    // Client Connection via Socket Class

    SocketThread(Socket insocket) {
      this.insocked = insocket;
      this.start();
    }

    @Override
    public void run() {
      // we manage our particular client connection
      BufferedReader in;
      PrintWriter out;
      String resource;

      try {
        // we read characters from the client via input stream on the socket
        in = new BufferedReader(new InputStreamReader(insocked.getInputStream()));
        // we get character output stream to client
        out = new PrintWriter(insocked.getOutputStream());
        // get first line of the request from the client
        String input = in.readLine();
        // we parse the request with a string tokenizer

        System.out.println("sockedthread : " + input);

        StringTokenizer parse = new StringTokenizer(input);
        String method = parse.nextToken().toUpperCase();
        // we get the HTTP method of the client
        if (!method.equals("GET")) {
          System.out.println("501 Not Implemented : " + method + " method.");
        } else {
          // what comes after "localhost:8080"
          resource = parse.nextToken();
          System.out.println("input " + input);
          System.out.println("method " + method);
          System.out.println("resource " + resource);

          parse = new StringTokenizer(resource, "/[?]=&");
          int i = 0;
          String[] tokens = new String[20];
          // more than the actual number of parameters
          while (parse.hasMoreTokens()) {
            tokens[i] = parse.nextToken();
            System.out.println("token " + i + "=" + tokens[i]);
            i++;
          }

          // Make the answer as a JSON string, to be sent to the Javascript client
          String answer = makeHeaderAnswer() + makeBodyAnswer(tokens);
          System.out.println("answer\n" + answer);
          // Here we send the response to the client
          out.println(answer);
          out.flush(); // flush character output stream buffer
        }

        in.close();
        out.close();
        insocked.close(); // we close socket connection
      } catch (Exception e) {
        System.err.println("Exception : " + e);
      }
    }


    private String makeBodyAnswer(String[] tokens) {
      String body = "";
      switch (tokens[0]) {
        case "get_tree": {
          int id = Integer.parseInt(tokens[1]);
          ProjectComponent activity = findActivityById(id);
          assert (activity != null);
          body = activity.toJson(1).toString();
          break;
        }
        case "start": {
          int id = Integer.parseInt(tokens[1]);
          ProjectComponent activity = findActivityById(id);
          assert (activity != null);
          Task task = (Task) activity;
          task.startTask();
          body = "{}";
          break;
        }
        case "stop": {
          int id = Integer.parseInt(tokens[1]);
          ProjectComponent activity = findActivityById(id);
          assert (activity != null);
          Task task = (Task) activity;
          task.stopTask();
          body = "{}";
          break;
        }
        case "add": {
          String name = tokens[1].replace("%20", " ");
          int parentId = Integer.parseInt(tokens[2]);
          String type = tokens[3];
          String unprocessedTokens = tokens[4];

          ProjectComponent parentActivity = findActivityById(parentId);
          ProjectComponent newActivity = null;
          if (type.equals("Project")) {
            newActivity = new Project(name, parentActivity);
          } else if (type.equals("Task")) {
            newActivity = new Task(name, parentActivity);
          }

          if (newActivity != null) {
            String[] processedTokens = unprocessedTokens.split(",");
            for (String tag : processedTokens) {
              newActivity.addTag(tag);
            }

            body = "{}";
          } else {
            body = "{\"error\": \"Type must be Project or Task\"}";
          }
          break;
        }
        case "last": {
          int id = Integer.parseInt(tokens[1]);
          ProjectComponent activity = findActivityById(id);
          assert (activity != null);
          ProjectComponent lastWorkedTask = findLastWorkedTask(activity);
          body = lastWorkedTask.toJson(1).toString();
          break;
        }
        case "projects": {
          int id = Integer.parseInt(tokens[1]);
          ProjectComponent activity = findActivityById(id);
          assert (activity != null);
          ArrayList<ProjectComponent> projectList = getProjectList(activity);


          JSONArray jsonProjects = new JSONArray();
          for (ProjectComponent project : projectList) {
            jsonProjects.put(project.toJson(1));
          }

          System.out.println(jsonProjects.length());
          System.out.println(projectList.size());

          JSONObject json = new JSONObject();
          json.put("projects", jsonProjects);

          body = json.toString();

          projectList = null;
          jsonProjects = null;

          break;
        }
        case "tags": {
          int id = Integer.parseInt(tokens[1]);
          ProjectComponent activity = findActivityById(id);
          assert (activity != null);
          ArrayList<String> tagList = getTagList(activity);

          JSONObject json = new JSONObject();
          JSONArray jsonTags = new JSONArray();
          for (String tag : tagList) {
            jsonTags.put(tag);
          }

          json.put("tags", jsonTags);

          body = json.toString();

          break;
        }
        case "search_tag": {
          int id = Integer.parseInt(tokens[1]);
          String tag = tokens[2];
          ProjectComponent activity = findActivityById(id);
          assert (activity != null);
          ArrayList<ProjectComponent> projectComponList = getProjectComponentsByTag(activity, tag);


          JSONArray jsonProjectComponents = new JSONArray();
          for (ProjectComponent projectComponent : projectComponList) {
            jsonProjectComponents.put(projectComponent.toJson(1));
          }

          System.out.println(jsonProjectComponents.length());
          System.out.println(projectComponList.size());

          JSONObject json = new JSONObject();
          json.put("project_components", jsonProjectComponents);

          body = json.toString();

          break;
        }
        default:
          assert false;
      }

      saveJson();

      System.out.println(body);
      return body;
    }

    private String makeHeaderAnswer() {
      String answer = "";
      answer += "HTTP/1.0 200 OK\r\n";
      answer += "Content-type: application/json\r\n";
      answer += "\r\n";
      // blank line between headers and content, very important !
      return answer;
    }
  }
}
