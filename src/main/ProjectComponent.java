package main;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class ProjectComponent {
  private String name;
  private ProjectComponent parent;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Duration duration;

  public ProjectComponent(String name, ProjectComponent parent) {
    this.name = name;
    this.parent = parent;
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
}
