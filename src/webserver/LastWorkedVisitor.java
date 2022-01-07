package webserver;

import main.Interval;
import main.Project;
import main.ProjectComponent;
import main.ProjectVisitor;
import main.Task;

/**
 * Aquesta classe es un visitor que buscara el project.
 * component en el que es va treballar per ultima vegada.
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */

public class LastWorkedVisitor implements ProjectVisitor {

  private final ProjectComponent root;
  private ProjectComponent foundProjectComponent;

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
      if (this.foundProjectComponent == null
              || task.getEndTime().isAfter(this.foundProjectComponent.getEndTime())) {
        this.foundProjectComponent = task;
      }
    }
  }

  @Override
  public void visitInterval(Interval interval) {}
}
