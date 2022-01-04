package webserver;

import main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


/**
 * Aquesta classe en un visitor que recorre tota la jerarquia.
 * En cada ProjectComponent ...
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */

public class ProjectListVisitor implements ProjectVisitor {

  private static ProjectListVisitor uniqueInstance;
  private ProjectComponent root;
  private ArrayList<ProjectComponent> projectList;
  Logger logger = LoggerFactory.getLogger(ProjectListVisitor.class);

  /**
   * Al ser un singleton en comptes de cridar al constructor es crida.
   * a aquesta funcio si mai sha cridat abans crida al constructor.
   * sino retorna la instancia del objecta actual.
   *
   * @author Grup 1 Torn 422
   */
  public static ProjectListVisitor getInstance(ProjectComponent root) {
    if (uniqueInstance == null) {
      uniqueInstance = new ProjectListVisitor(root);
    }

    return uniqueInstance;
  }

  private ProjectListVisitor(ProjectComponent root) {
    this.root = root;
    this.projectList = new ArrayList<>();
  }

  public ArrayList<ProjectComponent> getProjectList(ProjectComponent root) {

    if(root != null) {
      this.root = root;
      this.root.acceptVisitor(this);
    }

    return this.projectList;
  }

  public int getProjectListSize() {
    return projectList.size();
  }

  @Override
  public void visitProject(Project project) {
    this.projectList.add(project);
  }

  @Override
  public void visitTask(Task task) {}

  @Override
  public void visitInterval(Interval interval) {}
}
