package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aquesta clase implementa un patró Visitor i s'encarrega de printar la informació de cada
 * component que visita.
 *
 * @author Grup 1 Torn 422.
 * @version 1.0.
 */

public class PrinterVisitor implements ProjectVisitor {

  private static PrinterVisitor uniqueInstance;
  private final ProjectComponent root;
  Logger logger = LoggerFactory.getLogger(PrinterVisitor.class);

  /**
   * D'igual manera que es fa en la classe Clock, la funció getInstance el que ens asegura és que
   * en tota l'execució del programa només existeix una instància de l'objecte PrinterVisitor.
   *
   * @author Grup 1 Torn 422.
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
