package main;

public class PrinterVisitor implements ProjectVisitor{

    private static PrinterVisitor uniqueInstance;
    private ProjectComponent root;

    public static PrinterVisitor getInstance(ProjectComponent root) {
        if (uniqueInstance == null) {
            uniqueInstance = new PrinterVisitor(root);
        }

        return uniqueInstance;
    }

    private PrinterVisitor(ProjectComponent root) { this.root = root; root.acceptVisitor(this); }

    @Override
    public void visitProject(Project project) {
        System.out.println("Project: " + project.getName()); //TODO: hacer el toString()
    }

    @Override
    public void visitTask(Task task) {
        System.out.println("Task: " + task.getName()); //TODO: hacer el toString()
    }

    @Override
    public void visitInterval(Interval interval) {
        System.out.println("Interval"); //TODO: hacer el toString()
    }
}
