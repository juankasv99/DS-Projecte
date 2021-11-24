package main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Some javadoc.
 *
 * @author Some javadoc.
 * @version Some javadoc.
 * @deprecated Some javadoc.
 */

public class Client {

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   * @deprecated Some javadoc.
   */
  static Logger logger = LoggerFactory.getLogger(Client.class);
  public static void main(String[] args) throws InterruptedException {
    /* Seleccionar test  */
    // testSampleTree();
    // testLoadSampleTree();
    testOfCountingTime();
  }

  private static void testSampleTree() {
    logger.debug("Test sample tree starts:");

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

    savejsonvisitor savejsonvisitor = new savejsonvisitor();
    root.acceptVisitor(savejsonvisitor);
    savejsonvisitor.save("src/main/test.json");

    logger.debug("Test sample tree ends");
  }

  private static void testLoadSampleTree() {
    logger.debug("Test load sample tree starts:");

    loadjson loadjson = new loadjson();
    loadjson.load("test.json");
    Project root = loadjson.getRoot();

    PrinterVisitor printer = PrinterVisitor.getInstance(root);
    printer.print();

    logger.debug("Test load sample tree ends");
  }

  private static void testOfCountingTime() throws InterruptedException {
    Clock clock = Clock.getInstance();
    logger.trace("Se ha instanciado la variable periodo a 2.");
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
