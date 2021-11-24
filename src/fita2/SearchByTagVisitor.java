package fita2;

import main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some javadoc.
 *
 * @author Some javadoc.
 * @version Some javadoc.
 * @deprecated Some javadoc.
 */

public class SearchByTagVisitor implements ProjectVisitor {

  private static SearchByTagVisitor uniqueInstance;
  private ProjectComponent root;
  private String tag;
  Logger logger = LoggerFactory.getLogger(SearchByTagVisitor.class);

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   * @version Some javadoc.
   * @deprecated Some javadoc.
   */
  public static SearchByTagVisitor getInstance(ProjectComponent root) {
    if (uniqueInstance == null) {
      uniqueInstance = new SearchByTagVisitor(root);
    }

    return uniqueInstance;
  }

  private SearchByTagVisitor(ProjectComponent root) {
    this.root = root;
    this.tag = "";
  }

  public void search(String tag) {
    this.tag = tag;
    this.root.acceptVisitor(this);
  }

  @Override
  public void visitProject(Project project) {
    if (project.getTags().contains(tag)) {
      logger.info(project.getName());
    }
  }

  @Override
  public void visitTask(Task task) {
    if (task.getTags().contains(tag)) {
      logger.info(task.getName());
    }
  }

  @Override
  public void visitInterval(Interval interval) {}
}
