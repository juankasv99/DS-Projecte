package main;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        /*
        Clock clock = Clock.getInstance();

        clock.start(1);

        Project root = new Project("root", null);
        Project p1 = new Project("P1", root);
        Project p2 = new Project("P2", root);
        Task t1 = new Task("T1", root);
        Task t2 = new Task("T2", p1);
        Task t3 = new Task("T3", p2);

        PrinterVisitor printer = PrinterVisitor.getInstance(root);
        System.out.println("------------");

        Thread.sleep(4000);
        t1.startTask();
        Thread.sleep(4000);
        t2.startTask();
        Thread.sleep(2000);

        t1.stopTask();
        t2.stopTask();

        clock.stop();

        SaveJSONVisitor saveJSONVisitor = new SaveJSONVisitor();
        root.acceptVisitor(saveJSONVisitor);

        saveJSONVisitor.save("test.json");
        */

        LoadJSONVisitor loadJSONVisitor = new LoadJSONVisitor();
        loadJSONVisitor.load("../../test.json");
    }

}
