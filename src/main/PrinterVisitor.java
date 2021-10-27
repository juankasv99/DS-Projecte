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

    public void print() {
        this.root.acceptVisitor(this);
        System.out.println("------------");
    }

    @Override
    public void visitProject(Project project) {
        System.out.println(project.toString());
    }

    @Override
    public void visitTask(Task task) {
        System.out.println(task.toString());
    }

    @Override
    public void visitInterval(Interval interval) {
        System.out.println(interval.toString());
    }
}
