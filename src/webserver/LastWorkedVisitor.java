package webserver;

import main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Aquesta classe en un visitor que recorre tota la jerarquia.
 * En cada ProjectComponent ...
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */

public class LastWorkedVisitor implements ProjectVisitor {

  private ProjectComponent root;
  private ProjectComponent foundProjectComponent;
  Logger logger = LoggerFactory.getLogger(LastWorkedVisitor.class);

  public LastWorkedVisitor(ProjectComponent root) {
    this.root = root;
    this.foundProjectComponent = null;
  }

  public ProjectComponent search() {
    this.root.acceptVisitor(this);

    return this.foundProjectComponent;
  }

  @Override
  public void visitProject(Project project) {}

  @Override
  public void visitTask(Task task) {
    if (task.getEndTime() != null) {
      if (this.foundProjectComponent == null || task.getEndTime().isAfter(this.foundProjectComponent.getEndTime())) {
        this.foundProjectComponent = task;
      }
    }
  }

  @Override
  public void visitInterval(Interval interval) {}
}
