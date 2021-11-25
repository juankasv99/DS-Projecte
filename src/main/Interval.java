package main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

/**
 * La classe Interval és la que s'inclou dintre de cada Task cada vegada que hi ha un
 * periode d'activitat. Tota la informació de cada periode la té la classe interval.
 *
 * @author Grup 1 Torn 422.
 * @version 1.0.
 */

public class Interval implements Observer {

  private final LocalDateTime startTime;
  private LocalDateTime endTime;
  private Duration duration;
  private final Task task; //parent

  /**
   * El constructor de la classe truca a la instància única del timer per tal d'agafar la
   * informació relativa al temps. Aquesta versió del constructor rep la tasca a la que
   * afegir l'interval i el delay amb el que es vol afegir l'interval.
   *
   * @author Grup 1 Torn 422.
   */
  public Interval(Task task, int delay) {
    this.startTime = Clock.getInstance().getTime(); //la fecha de inicio es cuando se ha creado
    this.task = task;

    if (task.getStartTime() == null) {
      task.setStartTime(this.startTime);
    }

    this.duration = Duration.ofSeconds(delay);
    Clock.getInstance().addObserver(this);
  }

  /**
   * El constructor de la classe truca a la instància única del timer per tal d'agafar la
   * informació relativa al temps. Aquesta versió del constructor rep la tasca a la que
   * afegir l'interval, el temps final, el temps inicial i la duració de l'interval.
   *
   * @author Grup 1 Torn 422.
   */
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
    /* A cada tick se actualiza la duration y el endTime */
    this.duration = this.duration.plusSeconds(Clock.getInstance().getPeriod());
    this.endTime = this.startTime.plusSeconds(Clock.getInstance().getPeriod());

    this.task.update(this); //actualiza el parent

    PrinterVisitor.getInstance(null).print();
  }

  public void acceptVisitor(ProjectVisitor visitor) {
    visitor.visitInterval(this);
  }

  @Override
  public String toString() {
    //printa la informacion con una columna por cada atributo
    return String.format("%-31s child of %-20s %-30s %-30s %-5d",
            this.getClass().getSimpleName(), this.task.getName(),
            this.startTime, this.endTime, this.duration.toSeconds());
  }
}
