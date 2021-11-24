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
    logger.debug("Se cargan los datos en el archivo 'test.json'");
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
    logger.debug("Se inicia el reloj.");
    clock.start();

    Project root = new Project("root", null);
    Task transportation = new Task("transportation", root);

    Project softwareDesign = new Project("software design", root);
    Project problems = new Project("problems", softwareDesign);
    Task firstList = new Task("first list", problems);
    Task secondList = new Task("second list", problems);

    PrinterVisitor printer = PrinterVisitor.getInstance(root);
    printer.print();

    logger.debug("Test of counting time starts:");

    logger.debug("transportation starts");
    transportation.startTask(Clock.getInstance().getPeriod());
    logger.trace("El thread se para durante 4 segundos.");
    Thread.sleep(4000);
    transportation.stopTask();
    logger.debug("transportation stops");

    logger.trace("El thread se para durante 2 segundos.");
    Thread.sleep(2000);

    logger.debug("first list starts");
    firstList.startTask();
    logger.trace("El thread se para durante 6 segundos.");
    Thread.sleep(6000);

   logger.debug("second list starts");
    secondList.startTask();
    logger.trace("El thread se para durante 4 segundos.");
    Thread.sleep(4000);

    firstList.stopTask();
    logger.debug("first list stops");

    logger.trace("El thread se para durante 2 segundos.");
    Thread.sleep(2000);

    secondList.stopTask();
    logger.debug("second list stops");

    logger.trace("El thread se para durante 2 segundos.");
    Thread.sleep(2000);

    logger.debug("transportation starts");
    transportation.startTask();
    logger.trace("El thread se para durante 4 segundos.");
    Thread.sleep(4000);
    transportation.stopTask();
    logger.debug("transportation stops");

    logger.debug("Test of counting time ends");

    Clock.getInstance().stop();
  }
}
