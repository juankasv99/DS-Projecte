package main;

import java.time.Duration;
import java.time.LocalDateTime;

// Task:      Time Spent (sum of intervals), When
// Project:   Time Spent (sum of children Intervals), Contains (0-n)Tasks and (0-n)Projects

public abstract class ProjectComponent {
  private String name;
  private ProjectComponent parent;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Duration duration;

  public ProjectComponent(String name, ProjectComponent parent) {
    this.name = name;
    this.parent = parent;

    if (this.parent != null) this.parent.addChildren(this); //Añadimos el parent al Component
  }

  public String getName() {
    return name;
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

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;

    if (this.parent.getStartTime() == null) this.parent.setStartTime(this.startTime);
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public abstract void addChildren(ProjectComponent children);

  public abstract void removeChildren(ProjectComponent children);

  public abstract void acceptVisitor(ProjectVisitor visitor);
}
