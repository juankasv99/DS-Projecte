package main;

public class Client {

    public static void main(String[] args) throws
            InterruptedException{
        Clock clock = Clock.getInstance();

        clock.start(1);

        Project root = new Project("root", null);
        Project p1 = new Project("P1", root);
        Task t1 = new Task("T1", root);
        Task t2 = new Task("T2", p1);

        PrinterVisitor printer = PrinterVisitor.getInstance(root);

        t1.startTask();

        System.out.println("Pasada");
    }

}
