package webserver;

import main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Aquesta classe en un visitor que recorre tota la jerarquia.
 * En cada ProjectComponent mira els tags que te i els compara.
 * Amb el tag amb el que s'has creat la classe.
 * Si el te mostra el ProjectComponent.
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */
public class SearchByIdVisitor implements ProjectVisitor {

  private static SearchByIdVisitor uniqueInstance;
  private final ProjectComponent root;
  private int id;
  private ProjectComponent foundProjectComponent;
  Logger logger = LoggerFactory.getLogger(SearchByIdVisitor.class);

  /**
   * Al ser un singleton en comptes de cridar al constructor es crida.
   * a aquesta funcio si mai sha cridat abans crida al constructor.
   * sino retorna la instancia del objecta actual.
   *
   * @author Grup 1 Torn 422
   */
  public static SearchByIdVisitor getInstance(ProjectComponent root) {
    if (uniqueInstance == null) {
      uniqueInstance = new SearchByIdVisitor(root);
    }

    return uniqueInstance;
  }

  private SearchByIdVisitor(ProjectComponent root) {
    this.root = root;
    this.id = 0;
    this.foundProjectComponent = null;
  }

  /**
   * Some Javadoc.
   *
   * @author Grup 1 Torn 422.
   */
  public ProjectComponent search(int id) {
    this.id = id;
    this.root.acceptVisitor(this);

    return this.foundProjectComponent;
  }

  @Override
  public void visitProject(Project project) {
    if (project.getId() == this.id) {
      this.foundProjectComponent = project;
      logger.info("Project found: " + project);
    }
  }

  @Override
  public void visitTask(Task task) {
    if (task.getId() == this.id) {
      this.foundProjectComponent = task;
      logger.info("Task found: " + task);
    }
  }

  @Override
  public void visitInterval(Interval interval) {}
}
