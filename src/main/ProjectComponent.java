package main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Task:      Time Spent (sum of intervals), When
// Project:   Time Spent (sum of children Intervals), Contains (0-n)Tasks and (0-n)Projects

/**
 * Some javadoc.
 *
 * @author Some javadoc.
 * @version Some javadoc.
 */

public abstract class ProjectComponent {
  private final String name;
  private final ProjectComponent parent;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Duration duration;
  private final ArrayList<String> tags;
  Logger logger = LoggerFactory.getLogger(ProjectComponent.class);

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   * @version Some javadoc.
   */
  public ProjectComponent(String name, ProjectComponent parent) {
    this.name = name;
    this.parent = parent;
    this.duration = Duration.ZERO;
    this.tags = new ArrayList<>();

    if (this.parent != null) {
      this.parent.addChildren(this); //Añadimos al padre este hijo//
    }
  }

  public ProjectComponent getParent() {
    return parent;
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

  public ArrayList<String> getTags() {
    return tags;
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   * @version Some javadoc.
   */
  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;

    if (this.parent != null && this.parent.getStartTime() == null) {
      this.parent.setStartTime(this.startTime);
    }
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public void addTag(String tag) {
    logger.info("Se ha añadido el tag " + tag);
    this.tags.add(tag);
  }

  public abstract void addChildren(ProjectComponent children);

  // public abstract void removeChildren(ProjectComponent children);

  public abstract void update(Interval activeInterval);

  public abstract void acceptVisitor(ProjectVisitor visitor);

  @Override
  public String toString() {
    String parentName = (this.parent == null) ? null : this.parent.getName();
    //printa la informacion con una columna por cada atributo
    return String.format("%-10s %-20s child of %-20s %-30s %-30s %-5d",
            this.getClass().getSimpleName(),
            this.name, parentName, this.startTime,
            this.endTime, this.duration.toSeconds());
  }
}
