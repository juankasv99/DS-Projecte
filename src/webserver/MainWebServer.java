package webserver;

import java.time.Duration;
import java.time.LocalDateTime;
import java.io.File;
import main.*;

/**
 * Some Javadoc.
 */
public class MainWebServer {
  public static void main(String[] args) throws InterruptedException {
    webServer();
  }

  /**
   * Some Javadoc.
   *
   */
  public static void webServer() throws InterruptedException {
    final ProjectComponent root = makeTreeCourses();
     //final ProjectComponent root = makeVoidTree();
    //final ProjectComponent root = makeLoadedTree();
    // implement this method that returns the tree of
    // appendix A in the practicum handout

    // start your clock
    Clock clock = Clock.getInstance();
    clock.setPeriod(1);
    clock.start();

    new WebServer(root);
  }

  private static ProjectComponent makeTreeCourses() throws InterruptedException {
    Project root = new Project("Home", null);

    Project softwareDesign = new Project("Software Design", root);
    softwareDesign.addTag("java");
    softwareDesign.addTag("flutter");
    Project problems = new Project("Problems", softwareDesign);
    Task firstList = new Task("First List", problems);
    firstList.addTag("java");
    Task secondList = new Task("Second List", problems);
    secondList.addTag("Dart");
    Project projectTimeTracker = new Project("Project Time Tracker", softwareDesign);
    Task readHandout = new Task("Read Handout", projectTimeTracker);
    Task firstMilestone = new Task("First Milestone", projectTimeTracker);
    firstMilestone.addTag("Java");
    firstMilestone.addTag("IntelliJ");

    Project softwareTesting = new Project("Software Testing", root);
    softwareTesting.addTag("c++");
    softwareTesting.addTag("Java");
    softwareTesting.addTag("python");
    Project databases = new Project("Databases", root);
    databases.addTag("SQL");
    databases.addTag("python");
    databases.addTag("C++");
    Task transportation = new Task("Transportation", root);

    PrinterVisitor printer = PrinterVisitor.getInstance(root);

    return root;
  }

  private static ProjectComponent makeVoidTree() throws InterruptedException {
    Project root = new Project("Home", null);

    PrinterVisitor printer = PrinterVisitor.getInstance(root);

    return root;
  }

  private static ProjectComponent makeLoadedTree() throws InterruptedException {
    Loadjson loadjson = new Loadjson();
    try {
      String filepath = "../../test.json";
      File file = new File(filepath);
      String path = file.getPath();
      loadjson.load("test.json");
    } catch (Exception e) {
      return makeVoidTree();
    }
    Project root = loadjson.getRoot();

    PrinterVisitor printer = PrinterVisitor.getInstance(root);

    return root;
  }
}
