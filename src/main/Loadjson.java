package main;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * Some javadoc.
 *
 * @author Some javadoc.
 * @version Some javadoc.
 * @deprecated Some javadoc.
 */

public class Loadjson {

  private Project root;

  public Loadjson() {
  }

  public Project getRoot() {
    return this.root;
  }

  private String getstringfromjson(JSONObject jsonObject, String key) {
    String text = "";
    if (jsonObject.has(key)) {
      text = jsonObject.getString(key);
    }

    return text;
  }

  private LocalDateTime getlocaldatetimefromjson(JSONObject jsonObject, String key) {
    LocalDateTime localDateTime = null;
    if (jsonObject.has(key)) {
      localDateTime = LocalDateTime.parse(jsonObject.getString(key));
    }

    return localDateTime;
  }

  private long getlongfromjson(JSONObject jsonObject, String key) {
    long data = 0;
    if (jsonObject.has(key)) {
      data = jsonObject.getLong(key);
    }

    return data;
  }

  private ArrayList<ProjectComponent> loadChildren(ProjectComponent parent,
                                                   JSONArray jsonChildren) {
    ArrayList<ProjectComponent> children = new ArrayList<>();

    for (Object childObject : jsonChildren) {
      JSONObject jsonChild = (JSONObject) childObject;

      String name = getstringfromjson(jsonChild, "name");
      LocalDateTime startTime = getlocaldatetimefromjson(jsonChild, "startTime");
      LocalDateTime endTime = getlocaldatetimefromjson(jsonChild, "endTime");
      Duration duration = Duration.ofSeconds(getlongfromjson(jsonChild, "duration"));

      if (jsonChild.has("children")) {
        Project child = new Project(name, startTime, endTime, duration, parent);
        child.setChildren(loadChildren(child, jsonChild.getJSONArray("children")));
        children.add(child);
      }

      if (jsonChild.has("intervals")) {
        Task child = new Task(name, startTime, endTime, duration, parent);
        child.setIntervals(loadIntervals(child, jsonChild.getJSONArray("intervals")));
        children.add(child);
      }
    }

    return children;
  }

  private ArrayList<Interval> loadIntervals(Task task, JSONArray jsonIntervals) {
    ArrayList<Interval> intervals = new ArrayList<>();

    for (Object intervalObject : jsonIntervals) {
      JSONObject jsonInterval = (JSONObject) intervalObject;

      LocalDateTime startTime = getlocaldatetimefromjson(jsonInterval, "startTime");
      LocalDateTime endTime = getlocaldatetimefromjson(jsonInterval, "endTime");
      Duration duration = Duration.ofSeconds(getlongfromjson(jsonInterval, "duration"));

      Interval interval = new Interval(startTime, endTime, duration, task);
      intervals.add(interval);
    }

    return intervals;
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   * @version Some javadoc.
   * @deprecated Some javadoc.
   */
  public void load(String fileName) {
    InputStream is = Loadjson.class.getResourceAsStream(fileName);
    if (is == null) {
      throw new NullPointerException("Cannot find resource file " + fileName);
    }

    JSONTokener tokenizer = new JSONTokener(is);
    JSONObject jsonObject = new JSONObject(tokenizer);

    String name = getstringfromjson(jsonObject, "name");
    LocalDateTime startTime = getlocaldatetimefromjson(jsonObject, "startTime");
    LocalDateTime endTime = getlocaldatetimefromjson(jsonObject, "endTime");
    Duration duration = Duration.ofSeconds(getlongfromjson(jsonObject, "duration"));

    Project root = new Project(name, startTime, endTime, duration, null);

    if (jsonObject.has("children")) {
      root.setChildren(loadChildren(root, jsonObject.getJSONArray("children")));
    }

    this.root = root;
  }
}
