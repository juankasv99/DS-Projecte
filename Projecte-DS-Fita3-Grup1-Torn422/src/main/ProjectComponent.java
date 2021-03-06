package main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.IdGenerator;

// Task:      Time Spent (sum of intervals), When
// Project:   Time Spent (sum of children Intervals), Contains (0-n)Tasks and (0-n)Projects

/**
 * Aquesta classe és la classe base de la qual es generen tant la classe Task com la classe
 * Project. En aquesta s'especifica l'estructura base de les dues, amb aquells atributs i
 * mètodes que comparteixen els dos elements.
 *
 * @author Grup 1 Torn 422.
 * @version 1.0.
 */

public abstract class ProjectComponent {
  private final int id;
  private final String name;
  private final ProjectComponent parent;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private Duration duration;
  private final ArrayList<String> tags;
  Logger logger = LoggerFactory.getLogger(ProjectComponent.class);

  /**
   * Constructor principal de la classe on es passa per paràmetre el nom amb el que es vol crear
   * el component i el seu component pare.
   *
   * @author Grup 1 Torn 422.
   */
  public ProjectComponent(String name, ProjectComponent parent) {
    IdGenerator idGenerator = IdGenerator.getInstance();
    this.id = idGenerator.getId();
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

  public int getId() {
    return id;
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
   * Funció que ajuda des d'un element extern especificar quin temps inicial es vol
   * assignar al component.
   *
   * @author Grup 1 Torn 422.
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

  public abstract JSONObject toJson(int level);

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
