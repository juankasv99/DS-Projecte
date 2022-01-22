package webserver;

import java.util.ArrayList;
import main.Interval;
import main.Project;
import main.ProjectComponent;
import main.ProjectVisitor;
import main.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aquesta classe es un Visitor que.
 * recorre i retorna una llista amb tots el Projects que hi ha.
 * desde el ProjectComponent que se li passa amb aquest inclos.
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */

public class ProjectListVisitor implements ProjectVisitor {

  private final ProjectComponent root;
  private ArrayList<ProjectComponent> projectList;
  Logger logger = LoggerFactory.getLogger(ProjectListVisitor.class);

  public ProjectListVisitor(ProjectComponent root) {
    this.root = root;
    this.projectList = new ArrayList<>();
  }

  public ArrayList<ProjectComponent> getProjectList() {
    this.root.acceptVisitor(this);
    return this.projectList;
  }

  @Override
  public void visitProject(Project project) {

    this.projectList.add(project);
    logger.info("Project " + project.getName() + " added to list");
  }

  @Override
  public void visitTask(Task task) {}

  @Override
  public void visitInterval(Interval interval) {}
}
