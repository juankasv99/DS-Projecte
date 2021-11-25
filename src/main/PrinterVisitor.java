package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some javadoc.
 *
 * @author Some javadoc.
 * @version Some javadoc.
 */

public class PrinterVisitor implements ProjectVisitor {

  private static PrinterVisitor uniqueInstance;
  private final ProjectComponent root;
  Logger logger = LoggerFactory.getLogger(PrinterVisitor.class);

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   * @version Some javadoc.
   */
  public static PrinterVisitor getInstance(ProjectComponent root) {
    if (uniqueInstance == null) {
      uniqueInstance = new PrinterVisitor(root);
    }

    return uniqueInstance;
  }

  private PrinterVisitor(ProjectComponent root) {
    this.root = root;
  }

  public void print() {
    this.root.acceptVisitor(this);
    System.out.println("\n");
  }

  @Override
  public void visitProject(Project project) {
    logger.info(project.toString());
  }

  @Override
  public void visitTask(Task task) {
    logger.info(task.toString());
  }

  @Override
  public void visitInterval(Interval interval) {
    logger.info(interval.toString());
  }
}
