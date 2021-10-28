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

  public Interval(Task task, int delay) {
    this.startTime = Clock.getInstance().getTime();
    this.task = task;

    if (task.getStartTime() == null) task.setStartTime(this.startTime);

    this.duration = Duration.ofSeconds(delay);
    Clock.getInstance().addObserver(this);
  }

  public Interval(LocalDateTime startTime, LocalDateTime endTime, Duration duration, Task task) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.duration = duration;
    this.task = task;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public Duration getDuration() {
    return duration;
  }

  public Task getTask() {
    return task;
  }

  public void stopInterval() {
    Clock.getInstance().deleteObserver(this);
  }

  @Override
  public void update(Observable o, Object arg) {
    this.duration = this.duration.plusSeconds(Clock.getInstance().getPeriod());
    this.endTime = this.startTime.plusSeconds(Clock.getInstance().getPeriod());

    this.task.update(this);

    PrinterVisitor.getInstance(null).print();
  }

  public void acceptVisitor(ProjectVisitor visitor) {
    visitor.visitInterval(this);
  }

  @Override
  public String toString() {
    // return "Interval | child of task " + this.task.getName() + " | " + this.startTime + " | " + this.endTime + " | " + this.duration.toSeconds();
    return String.format("%-31s child of %-20s %-30s %-30s %-5d", this.getClass().getSimpleName(), this.task.getName(),
        this.startTime, this.endTime, this.duration.toSeconds());
  }
}
