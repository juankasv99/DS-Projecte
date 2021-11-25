package searchbytag;

import main.Interval;
import main.Project;
import main.ProjectComponent;
import main.ProjectVisitor;
import main.Task;
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
    this.tag = tag.toLowerCase();
    this.root.acceptVisitor(this);
  }

  @Override
  public void visitProject(Project project) {
    for (String projecttag : project.getTags()) {
      if (projecttag == tag) {
        logger.info("Project " + project.getName() + " has tag " + tag);
      }
    }
  }

  @Override
  public void visitTask(Task task) {
    for (String tasktag : task.getTags()) {
      if (tasktag == tag) {
        logger.info("Task " + task.getName() + " has tag " + tag);
      }
    }
  }

  @Override
  public void visitInterval(Interval interval) {}
}
