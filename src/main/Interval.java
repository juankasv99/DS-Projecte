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
    this.startTime = LocalDateTime.now();
    this.duration = Duration.ZERO;
    this.task = task;
    Clock.getInstance().addObserver(this);
  }

  @Override
  public void update(Observable o, Object arg) {
    this.duration = this.duration.plusSeconds(1);
    this.endTime = this.startTime.plusSeconds(1);

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

  @Override
  public String toString() {
    return "";
  }
}
