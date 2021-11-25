package searchbytag;

import main.Project;
import main.Task;
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
    testSearchByTag();
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   * @deprecated Some javadoc.
   */
  public static void testSearchByTag() {
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

    logger.debug("Test search by tag starts:");

    SearchByTagVisitor searchByTagVisitor = SearchByTagVisitor.getInstance(root);
    searchByTagVisitor.search("java");

    logger.debug("Test search by tag ends");
  }
}
