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

public class TagListVisitor implements ProjectVisitor {

  private ProjectComponent root;
  private ArrayList<String> tagList;
  Logger logger = LoggerFactory.getLogger(TagListVisitor.class);

  public TagListVisitor(ProjectComponent root) {
    this.root = root;
    this.tagList = new ArrayList<>();
  }

  public ArrayList<String> getTagList() {
    this.root.acceptVisitor(this);
    return this.tagList;
  }

  @Override
  public void visitProject(Project project) {
    for (String tag : project.getTags()) {
      if (!this.tagList.contains(tag)) {
        this.tagList.add(tag);
        logger.info("Tag: '" + tag + "' was added to project " + project.getName());
      }
    }
  }

  @Override
  public void visitTask(Task task) {
    for (String tag : task.getTags()) {
      if (!this.tagList.contains(tag)) {
        this.tagList.add(tag);
        logger.info("Tag: '" + tag + "' was added to task " + task.getName());
      }
    }
  }

  @Override
  public void visitInterval(Interval interval) {}
}
