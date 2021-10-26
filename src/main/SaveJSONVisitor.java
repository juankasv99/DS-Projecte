package main;
import org.json.JSONArray;
import org.json.JSONObject;

public class SaveJSONVisitor implements ProjectVisitor {

    private JSONObject root;
    private JSONArray children;
    /*
    private static SaveJSONVisitor uniqueInstance;

    public static SaveJSONVisitor getInstance(ProjectComponent root) {
        if (uniqueInstance == null) {
            uniqueInstance = new SaveJSONVisitor(root);
        }

        return uniqueInstance;
    }*/

    //TODO
    public SaveJSONVisitor() {
        this.root = new JSONObject();
        this.children = new JSONArray();
    }

    public JSONObject getRoot() {
        return this.root;
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
        if (project.getParent() != null) projectDetails.put("parent", project.getParent().getName());
        projectDetails.put("children", this.children);

        this.children = new JSONArray();

        if (this.root.isEmpty()) {
            this.root = projectDetails;
        } else {
            this.children.put(this.root);
            this.children.put(projectDetails);

            this.root = new JSONObject();
        }
    }

    @Override
    public void visitTask(Task task) {
        for(Interval interval: task.getIntervals()) {
            interval.acceptVisitor(this);
        }

        JSONObject taskDetails = new JSONObject();
        taskDetails.put("name", task.getName());
        taskDetails.put("startTime", task.getStartTime());
        taskDetails.put("endTime", task.getEndTime());
        if (task.getParent() != null) taskDetails.put("duration", task.getDuration().toSeconds());
        taskDetails.put("parent", task.getParent().getName());
        taskDetails.put("intervals", this.children);

        this.children = new JSONArray();

        if (this.root.isEmpty()) {
            this.root = taskDetails;
        } else {
            this.children.put(this.root);
            this.children.put(taskDetails);

            this.root = new JSONObject();
        }
    }

    @Override
    public void visitInterval(Interval interval) {
        JSONObject intervalDetails = new JSONObject();
        intervalDetails.put("startTime", interval.getStartTime());
        intervalDetails.put("endTime", interval.getEndTime());
        intervalDetails.put("duration", interval.getDuration().toSeconds());
        intervalDetails.put("task", interval.getTask().getName());

        this.children.put(intervalDetails);
    }
}
