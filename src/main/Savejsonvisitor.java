package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Some javadoc.
 *
 * @author Some javadoc.
 * @version Some javadoc.
 * @deprecated Some javadoc.
 */

public class Savejsonvisitor implements ProjectVisitor {

  private JSONObject root;
  private JSONArray children;
  private JSONArray intervals;

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   * @version Some javadoc.
   * @deprecated Some javadoc.
   */
  public Savejsonvisitor() {
    this.root = new JSONObject();
    this.children = new JSONArray();
    this.intervals = new JSONArray();
  }

  public JSONObject getRoot() {
    return this.root;
  }

  private JSONArray removejsons(JSONArray jsonArray, ArrayList<Integer> indexes) {
    JSONArray result = new JSONArray();
    for (int i = 0; i < jsonArray.length(); i++) {
      if (!indexes.contains(i)) {
        result.put(jsonArray.get(i));
      }
    }

    return result;
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   * @version Some javadoc.
   * @deprecated Some javadoc.
   */
  public void save(String fileName) {
    FileWriter file = null;

    try {
      file = new FileWriter(fileName);
      file.write(this.root.toString(2));
    } catch (IOException error) {
      error.printStackTrace();
    } finally {
      try {
        file.flush();
        file.close();
      } catch (IOException error) {
        error.printStackTrace();
      }
    }
  }

  @Override
  public void visitProject(Project project) {
    for (ProjectComponent child : project.getChildren()) {
      child.acceptVisitor(this);
    }

    JSONObject projectDetails = new JSONObject();
    projectDetails.put("name", project.getName());
    projectDetails.put("startTime", project.getStartTime());
    projectDetails.put("endTime", project.getEndTime());
    projectDetails.put("duration", project.getDuration().toSeconds());
    if (project.getParent() != null) {
      projectDetails.put("parent", project.getParent().getName());
    }
    JSONArray children = new JSONArray();

    ArrayList<Integer> indexesToRemove = new ArrayList<>();

    for (int i = 0; i < this.children.length(); i++) {
      JSONObject jsonChild = (JSONObject) this.children.get(i);

      if (jsonChild.getString("parent").equals(project.getName())) {
        children.put(jsonChild);
        indexesToRemove.add(i);
      }
    }

    this.children = this.removejsons(this.children, indexesToRemove);

    projectDetails.put("children", children);

    if (project.getParent() == null) {
      this.root = projectDetails;
    } else {
      this.children.put(projectDetails);
    }
  }

  @Override
  public void visitTask(Task task) {
    for (Interval interval : task.getIntervals()) {
      interval.acceptVisitor(this);
    }

    JSONObject taskDetails = new JSONObject();
    taskDetails.put("name", task.getName());
    taskDetails.put("startTime", task.getStartTime());
    taskDetails.put("endTime", task.getEndTime());
    if (task.getParent() != null) {
      taskDetails.put("duration", task.getDuration().toSeconds());
    }
    taskDetails.put("parent", task.getParent().getName());

    JSONArray intervals = new JSONArray();

    ArrayList<Integer> indexesToRemove = new ArrayList<>();

    for (int i = 0; i < this.intervals.length(); i++) {
      JSONObject jsonChild = (JSONObject) this.intervals.get(i);

      if (jsonChild.getString("task").equals(task.getName())) {
        intervals.put(jsonChild);
        indexesToRemove.add(i);
      }
    }

    this.intervals = this.removejsons(this.intervals, indexesToRemove);

    taskDetails.put("intervals", intervals);

    if (task.getParent() == null) {
      this.root = taskDetails;
    } else {
      this.children.put(taskDetails);
    }
  }

  @Override
  public void visitInterval(Interval interval) {
    JSONObject intervalDetails = new JSONObject();
    intervalDetails.put("startTime", interval.getStartTime());
    intervalDetails.put("endTime", interval.getEndTime());
    intervalDetails.put("duration", interval.getDuration().toSeconds());
    intervalDetails.put("task", interval.getTask().getName());

    this.intervals.put(intervalDetails);
  }
}
