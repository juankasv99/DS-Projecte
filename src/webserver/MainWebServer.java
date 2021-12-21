package webserver;

import main.Project;
import main.ProjectComponent;
import main.Clock;
import main.Task;

public class MainWebServer {
  public static void main(String[] args) {
    webServer();
  }

  public static void webServer() {
    final ProjectComponent root = makeTreeCourses();
    // implement this method that returns the tree of
    // appendix A in the practicum handout

    // start your clock
    Clock clock = Clock.getInstance();
    clock.setPeriod(2);
    clock.start();

    new WebServer(root);
  }

  private static ProjectComponent makeTreeCourses() {
    Project root = new Project("root", null);

    Project softwareDesign = new Project("software design", root);
    softwareDesign.addTag("java");
    softwareDesign.addTag("flutter");
    Project problems = new Project("problems", softwareDesign);
    Task firstList = new Task("first list", problems);
    firstList.addTag("java");
    Task secondList = new Task("second list", problems);
    secondList.addTag("Dart");
    Project projectTimeTracker = new Project("project time tracker", softwareDesign);
    Task readHandout = new Task("read handout", projectTimeTracker);
    Task firstMilestone = new Task("first milestone", projectTimeTracker);
    firstMilestone.addTag("Java");
    firstMilestone.addTag("IntelliJ");

    Project softwareTesting = new Project("software testing", root);
    softwareTesting.addTag("c++");
    softwareTesting.addTag("Java");
    softwareTesting.addTag("python");
    Project databases = new Project("databases", root);
    databases.addTag("SQL");
    databases.addTag("python");
    databases.addTag("C++");
    Task transportation = new Task("transportation", root);

    return root;
  }
}