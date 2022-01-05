package webserver;

import java.util.ArrayList;
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

public class ProjectListVisitor implements ProjectVisitor {

  private ProjectComponent root;
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
    logger.info("Project " + project + " added to list");
  }

  @Override
  public void visitTask(Task task) {}

  @Override
  public void visitInterval(Interval interval) {}
}
