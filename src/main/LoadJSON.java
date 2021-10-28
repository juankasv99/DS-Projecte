package main;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

public class LoadJSON {

    private Project root;

    public LoadJSON() {}

    public Project getRoot() {
        return this.root;
    }

    private String getStringFromJSON(JSONObject jsonObject, String key) {
        String text = "";
        if (jsonObject.has(key)) {
            text = jsonObject.getString(key);
        }

        return text;
    }

    private LocalDateTime getLocalDateTimeFromJSON(JSONObject jsonObject, String key) {
        LocalDateTime localDateTime = null;
        if (jsonObject.has(key)) {
            localDateTime = LocalDateTime.parse(jsonObject.getString(key));
        }

        return localDateTime;
    }

    private long getLongFromJSON(JSONObject jsonObject, String key) {
        long data = 0;
        if (jsonObject.has(key)) {
            data = jsonObject.getLong(key);
        }

        return data;
    }

    private ArrayList<ProjectComponent> loadChildren(ProjectComponent parent, JSONArray jsonChildren) {
        ArrayList<ProjectComponent> children = new ArrayList<>();

        for (Object childObject : jsonChildren) {
            JSONObject jsonChild = (JSONObject) childObject;

            String name = getStringFromJSON(jsonChild, "name");
            LocalDateTime startTime = getLocalDateTimeFromJSON(jsonChild, "startTime");
            LocalDateTime endTime = getLocalDateTimeFromJSON(jsonChild, "endTime");
            Duration duration = Duration.ofSeconds(getLongFromJSON(jsonChild, "duration"));

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

            LocalDateTime startTime = getLocalDateTimeFromJSON(jsonInterval, "startTime");
            LocalDateTime endTime = getLocalDateTimeFromJSON(jsonInterval, "endTime");
            Duration duration = Duration.ofSeconds(getLongFromJSON(jsonInterval, "duration"));

            Interval interval = new Interval(startTime, endTime, duration, task);
            intervals.add(interval);
        }

        return intervals;
    }

    public void load(String fileName) {
        InputStream is = LoadJSON.class.getResourceAsStream(fileName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + fileName);
        }

        JSONTokener tokenizer = new JSONTokener(is);
        JSONObject jsonObject = new JSONObject(tokenizer);

        String name = getStringFromJSON(jsonObject, "name");
        LocalDateTime startTime = getLocalDateTimeFromJSON(jsonObject, "startTime");
        LocalDateTime endTime = getLocalDateTimeFromJSON(jsonObject, "endTime");
        Duration duration = Duration.ofSeconds(getLongFromJSON(jsonObject, "duration"));

        Project root = new Project(name, startTime, endTime, duration, null);

        if (jsonObject.has("children")) {
            root.setChildren(loadChildren(root, jsonObject.getJSONArray("children")));
        }

        this.root = root;
    }
}
