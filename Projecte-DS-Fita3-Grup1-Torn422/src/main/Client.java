package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Programa Java on es troba la capa més externa del programa i els tests.
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */

@SuppressWarnings("checkstyle:SummaryJavadoc")
public class Client {

  /**
   * Clase client, amb la qual es fan les execucions i els tests de prova.
   *
   */
  static Logger logger = LoggerFactory.getLogger(Client.class);

  /**
   * Funció principal de tot el programa. Es selecciona quin dels diferents tests s'executen.
   * Hi ha 3 tipus de tests:
   * el testSampleTree on es comprova si l'estructura d'arbre es construeix
   * correctament,
   * el testLoadSampleTree on es comprova el funcionament de la càrrega i la descarrega del
   * json,
   * el testOfCountingTime on es comprova si el clock si el recompte de tots els elements
   * de l'arbre és correcte.
   *
   * @author Grup 1 Torn 422.
   */
  public static void main(String[] args) throws InterruptedException {
    /* Seleccionar test  */
    //testSampleTree();
    //testLoadSampleTree();
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

    Savejsonvisitor savejsonvisitor = new Savejsonvisitor();
    root.acceptVisitor(savejsonvisitor);
    savejsonvisitor.save("src/main/test.json");

    logger.debug("Test sample tree ends");
  }

  private static void testLoadSampleTree() {
    logger.debug("Test load sample tree starts:");

    Loadjson loadjson = new Loadjson();
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

    PrinterVisitor printer = PrinterVisitor.getInstance(root);
    printer.print();
    Task transportation = new Task("transportation", root);
    logger.debug("Test of counting time starts:");

    logger.debug("transportation starts");
    transportation.startTask(Clock.getInstance().getPeriod());
    logger.trace("El thread se para durante 4 segundos.");
    Thread.sleep(4000);
    transportation.stopTask();
    logger.debug("transportation stops");

    logger.trace("El thread se para durante 2 segundos.");
    Thread.sleep(2000);

    Project softwareDesign = new Project("software design", root);
    Project problems = new Project("problems", softwareDesign);

    Task firstList = new Task("first list", problems);

    logger.debug("first list starts");
    firstList.startTask();
    logger.trace("El thread se para durante 6 segundos.");
    Thread.sleep(6000);

    Task secondList = new Task("second list", problems);

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
