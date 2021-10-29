package main;

public class Client {

  public static void main(String[] args) throws InterruptedException {
    /* Seleccionar test  */
    // testSampleTree();
    // testLoadSampleTree();
    testOfCountingTime();
  }

  private static void testSampleTree() {
    System.out.println("Test sample tree starts:");

    Project root = new Project("root", null);

    Project softwareDesign = new Project("software design", root);
    Project problems = new Project("problems", softwareDesign);
    Task firstList = new Task("first list", problems);
    Task secondList = new Task("second list", problems);
    Project projectTimeTracker = new Project("project time tracker", softwareDesign);
    Task readHandout = new Task("read handout", projectTimeTracker);
    Task firstMilestone = new Task("first milestone", projectTimeTracker);

    Project softwareTesting = new Project("software testing", root);
    Project databases = new Project("databases", root);
    Task transportation = new Task("transportation", root);

    SaveJSONVisitor saveJSONVisitor = new SaveJSONVisitor();
    root.acceptVisitor(saveJSONVisitor);
    saveJSONVisitor.save("src/main/test.json");

    System.out.println("Test sample tree ends");
  }

  private static void testLoadSampleTree() {
    System.out.println("Test load sample tree starts:");

    LoadJSON loadJSON = new LoadJSON();
    loadJSON.load("test.json");
    Project root = loadJSON.getRoot();

    PrinterVisitor printer = PrinterVisitor.getInstance(root);
    printer.print();

    System.out.println("Test load sample tree ends");
  }

  private static void testOfCountingTime() throws InterruptedException {
    Clock clock = Clock.getInstance();
    clock.setPeriod(2);
    clock.start();

    Project root = new Project("root", null);
    Task transportation = new Task("transportation", root);

    Project softwareDesign = new Project("software design", root);
    Project problems = new Project("problems", softwareDesign);
    Task firstList = new Task("first list", problems);
    Task secondList = new Task("second list", problems);

    PrinterVisitor printer = PrinterVisitor.getInstance(root);
    printer.print();

    System.out.println("Test of counting time starts:");

    System.out.println("transportation starts");
    transportation.startTask(Clock.getInstance().getPeriod());
    Thread.sleep(4000);
    transportation.stopTask();
    System.out.println("transportation stops");

    Thread.sleep(2000);

    System.out.println("first list starts");
    firstList.startTask();
    Thread.sleep(6000);

    System.out.println("second list starts");
    secondList.startTask();
    Thread.sleep(4000);

    firstList.stopTask();
    System.out.println("first list stops");

    Thread.sleep(2000);

    secondList.stopTask();
    System.out.println("second list stops");

    Thread.sleep(2000);

    System.out.println("transportation starts");
    transportation.startTask();
    Thread.sleep(4000);
    transportation.stopTask();
    System.out.println("transportation stops");

    System.out.println("Test of counting time ends");

    Clock.getInstance().stop();
  }
}
