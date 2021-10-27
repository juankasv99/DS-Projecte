package main;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LoadJSONVisitor implements ProjectVisitor {

    private JSONObject root;
    private JSONArray children;
    private JSONArray intervals;

    public LoadJSONVisitor() {
        this.root = new JSONObject();
        this.children = new JSONArray();
        this.intervals = new JSONArray();
    }

    public JSONObject getRoot() {
        return this.root;
    }

    private JSONArray removeJSONS(JSONArray jsonArray, ArrayList<Integer> indexes) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (!indexes.contains(i)) result.put(jsonArray.get(i));
        }

       return result;
    }

    public void load(String fileName) {
        InputStream is = LoadJSONVisitor.class.getResourceAsStream(fileName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + fileName);
        }

        JSONTokener tokenizer = new JSONTokener(is);
        this.root = new JSONObject(tokenizer);

        String name = this.root.getString("name");
        LocalDateTime startTime = LocalDateTime.parse(this.root.getString("startTime"));
        LocalDateTime endTime = LocalDateTime.parse(this.root.getString("endTime"));
        Duration duration = Duration.ofSeconds(this.root.getLong("duration"));
        ProjectComponent parent = null;

        System.out.println(name + " - " + startTime + " - " + endTime + " - " + duration.getSeconds());

        /*
        System.out.println("Id  : " + object.getLong("id"));
        System.out.println("Name: " + object.getString("name"));
        System.out.println("Age : " + object.getInt("age"));
        JSONArray courses = object.getJSONArray("courses");
        */
    }

    @Override
    public void visitProject(Project project) {
        for (ProjectComponent child : project.getChildren()) {
            child.acceptVisitor(this);
        }
    }

    @Override
    public void visitTask(Task task) {
        for(Interval interval: task.getIntervals()) {
            interval.acceptVisitor(this);
        }
    }

    @Override
    public void visitInterval(Interval interval) {

    }
}
