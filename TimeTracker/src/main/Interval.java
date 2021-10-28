package main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

public class Interval implements Observer {

  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Duration duration;
  private Task task;

  public Interval(Task task) {
    this.startTime = Clock.getInstance().getTime();
    this.task = task;

    if (task.getStartTime() == null) task.setStartTime(this.startTime);

    this.duration = Duration.ZERO;
    Clock.getInstance().addObserver(this);
  }

  public Duration getDuration() {
    return duration;
  }

  public void stopInterval() {
    Clock.getInstance().deleteObserver(this);
  }

  @Override
  public void update(Observable o, Object arg) {
    this.duration = this.duration.plusSeconds(1);
    this.endTime = this.startTime.plusSeconds(1);

    this.task.update();

    PrinterVisitor.getInstance(null).print();


    /*
    if (this.startTime == null) {
      this.startTime = currentTime;
      task.setStartTime(this.startTime);
    }
    this.duration = Duration.between(this.startTime, currentTime);
    this.endTime = currentTime;
    this.task.componentDuration();
    this.task.setEndTime(this.endTime);
    Printer.getInstance(null).print();
    */
  }


  public void acceptVisitor(ProjectVisitor visitor) {
    visitor.visitInterval(this);
  }

  @Override
  public String toString() {
    //return "Interval \t child of task " + this.task.getName() + "\t" + this.startTime + "\t" + this.endTime + "\t" + this.duration.toSeconds();
    return (String.format("%-15s %-30s %-30s %-30s %-4s", "Interval", "child of task " + this.task.getName(), this.startTime, this.endTime,this.duration.toSeconds()));
  }
}
