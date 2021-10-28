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

  public Task(String name, LocalDateTime startTime, LocalDateTime endTime, Duration duration, ProjectComponent parent) {
    super(name, parent);
    super.setStartTime(startTime);
    super.setEndTime(endTime);
    super.setDuration(duration);
    this.intervals = new ArrayList<>();
  }

  public void startTask() {
    this.intervals.add(new Interval(this));
  }

  public void stopTask() {
    this.getCurrentInterval().stopInterval();
  }

  @Override
  public void update() {
    //super.setDuration(Duration.ZERO);
    super.setEndTime(Clock.getInstance().getTime());

    Duration counter = Duration.ZERO;
    for(Interval interval : this.intervals) {
      counter = counter.plus(interval.getDuration());
    }
    super.setDuration(counter);

    super.getParent().update();
  }

  private Interval getCurrentInterval() {
    int index = this.intervals.size() - 1;
    return this.intervals.get(index);
  }

  public ArrayList<Interval> getIntervals() {
    return this.intervals;
  }

  public void setIntervals(ArrayList<Interval> intervals) {
    this.intervals = intervals;
  }

  public void addChildren(ProjectComponent children) {}

  public void removeChildren(ProjectComponent children) {}

  public void acceptVisitor(ProjectVisitor visitor) {
    visitor.visitTask(this);
    for(Interval interval : this.intervals) {
      interval.acceptVisitor(visitor);
    }
  }
}
