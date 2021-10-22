package main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task extends ProjectComponent {

  private ArrayList<Interval> intervals;

  public Task(String name, ProjectComponent parent) {
    super(name, parent);
    this.intervals = new ArrayList<>();
  }

  public void startTask() {
    this.intervals.add(new Interval(this));
  }

  public void stopTask() {
    this.getCurrentInterval().stopInterval();
  }

  public void update() {
    super.setDuration(Duration.ZERO);
    super.setEndTime(LocalDateTime.now());
  }

  private Interval getCurrentInterval() {
    int index = this.intervals.size() - 1;
    return this.intervals.get(index);
  }
}
