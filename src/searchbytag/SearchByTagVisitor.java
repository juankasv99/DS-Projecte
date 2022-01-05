package searchbytag;

import java.util.ArrayList;
import java.util.Objects;
import main.Interval;
import main.Project;
import main.ProjectComponent;
import main.ProjectVisitor;
import main.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.SearchByTagSearchVisitor;


/**
 * Aquesta classe en un visitor que recorre tota la jerarquia.
 * En cada ProjectComponent mira els tags que te i els compara.
 * Amb el tag amb el que s'has creat la classe.
 * Si el te mostra el ProjectComponent.
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */

public class SearchByTagVisitor implements ProjectVisitor {

  private ProjectComponent root;
  private String tag;
  private ArrayList<ProjectComponent> projectComponentList;
  Logger logger = LoggerFactory.getLogger(SearchByTagSearchVisitor.class);

  public SearchByTagVisitor(ProjectComponent root) {
    this.root = root;
    this.tag = "";
    this.projectComponentList = new ArrayList<>();
  }

  public void search(String tag) {
    this.tag = tag;
    this.root.acceptVisitor(this);
  }

  public ArrayList<ProjectComponent> getProjectComponentList(String tag) {
    this.search(tag);
    return this.projectComponentList;
  }

  @Override
  public void visitProject(Project project) {
    for (String projectTag : project.getTags()) {
      if (Objects.equals(projectTag.toLowerCase(), tag.toLowerCase())) {
        this.projectComponentList.add(project);
        logger.info("Project " + project.getName() + " has tag " + tag);
      }
    }
  }

  @Override
  public void visitTask(Task task) {
    for (String taskTag : task.getTags()) {
      if (Objects.equals(taskTag.toLowerCase(), tag.toLowerCase())) {
        this.projectComponentList.add(task);
        logger.info("Task " + task.getName() + " has tag " + tag);
      }
    }
  }

  @Override
  public void visitInterval(Interval interval) {}
}
