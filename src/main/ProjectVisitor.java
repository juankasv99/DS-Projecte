package main;

public interface ProjectVisitor {
    void visitProject(Project project);
    void visitTask(Task task);
    void visitInterval(Interval interval);
}
