package main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some javadoc.
 *
 * @author Some javadoc.
 * @version Some javadoc.
 * @deprecated Some javadoc.
 */
public class Task extends ProjectComponent {

  private final int zeroSecondsDelay = 0;

  private ArrayList<Interval> intervals;

  Logger logger = LoggerFactory.getLogger(Task.class);

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public Task(String name, ProjectComponent parent) {
    super(name, parent);
    // Precondiciones
    assert this.intervals == null : "La lista de Intervals de la Task"
        + " debe ser null antes de ser creada.";

    this.intervals = new ArrayList<>();
    if (getParent() != null) {
      logger.debug("Se crea Task " + name + ", hija de " + parent.getName());
    } else {
      logger.warn("La Task " + name + " no tiene padre.");
    }

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    // Postcondiciones
    assert this.getName().equals(name) : "El nombre de la Task"
        + " debe ser el mismo que el de la variable name.";
    assert this.getParent().equals(parent) : "El padre de la Task"
        + " debe ser el mismo que el de la variable parent.";
    assert this.getDuration().toSeconds() == 0 : "La duración de la Task"
        + " debe ser 0 justo después de ser creada.";
    assert this.intervals != null : "La lista de Intervals de la Task"
        + " no debe ser null just después de ser creada.";
    assert this.intervals.isEmpty() : "La lista de Intervals de la Task"
        + " debe estar vacía justo después de ser creada.";
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public Task(String name, @Deprecated LocalDateTime startTime,
              @Deprecated LocalDateTime endTime, Duration duration, ProjectComponent parent) {
    super(name, parent);
    super.setStartTime(startTime);
    super.setEndTime(endTime);
    super.setDuration(duration);


    // Precondiciones
    assert this.intervals == null : "La lista de Intervals de la Task"
        + " debe ser null antes de ser creada.";

    this.intervals = new ArrayList<>();

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    // Postcondiciones
    assert this.getName().equals(name) : "El nombre de la Task"
        + " debe ser el mismo que el de la variable name.";
    assert this.getParent().equals(parent) : "El padre de la Task"
        + " debe ser el mismo que el de la variable parent.";
    assert this.getDuration().toSeconds() == 0 : "La duración de la Task"
        + " debe ser 0 justo después de ser creada.";
    assert this.intervals != null : "La lista de Intervals de la Task"
        + " no debe ser null just después de ser creada.";
    assert this.intervals.isEmpty() : "La lista de Intervals de la Task"
        + " debe estar vacía justo después de ser creada.";
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public void startTask() {
    // Precondiciones
    assert this.intervals != null : "La lista de Intervals de la Task no debe ser null.";
    final int sizeBeforeInsert = this.intervals.size();

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    Interval interval = new Interval(this, zeroSecondsDelay);
    this.intervals.add(interval);

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    // Postcondiciones
    assert this.intervals.size() == sizeBeforeInsert + 1 : "El tamaño de la lista de Intervals "
        + "de la Task debe aumentar en 1 después de insertar el Interval.";
    assert this.intervals.contains(interval) : "La lista de Intervals "
        + "de la Task debe contener el Interval después de insertarlo.";
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public void startTask(int delay) {
    // Precondiciones
    assert this.intervals != null : "La lista de Intervals de la Task no debe ser null.";
    assert delay > 0 : "El delay añadido al Interval debe ser mayor a 0.";
    final int sizeBeforeInsert = this.intervals.size();

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    Interval interval = new Interval(this, delay);
    this.intervals.add(interval);

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    // Postcondiciones
    assert this.intervals.size() == sizeBeforeInsert + 1 : "El tamaño de la lista de Intervals"
        + " de la Task debe aumentar en 1 después de insertar el Interval.";
    assert this.intervals.contains(interval) : "La lista de Intervals de la Task"
        + " debe contener el Interval después de insertarlo.";
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public void stopTask() {
    // Precondiciones
    assert this.intervals != null : "La lista de Intervals de la Task no debe ser null.";
    assert this.intervals.size() > 0 : "La lista de Intervals debe contener, almenos, 1 Interval.";

    // Intervals
    assert this.invariants() : "Los invariants no se cumplen.";

    this.getCurrentInterval().stopInterval();

    // Intervals
    assert this.invariants() : "Los invariants no se cumplen.";

    // Postcondiciones
  }

  @Override
  public void update(Interval activeInterval) {
    // Precondiciones

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    Duration counter = Duration.ZERO;
    /* duracion = Suma de las duraciones de los hijos */
    for (Interval interval : this.intervals) {
      counter = counter.plus(interval.getDuration());
    }
    super.setDuration(counter);

    super.setEndTime(this.getCurrentInterval().getEndTime());

    super.getParent().update(activeInterval);

    // Intervals
    assert this.invariants() : "Los invariants no se cumplen.";

    //Postcondiciones: No existen postcondiciones para esta función, puesto que no modifica la clase
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public ArrayList<Interval> getIntervals() {
    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    return this.intervals;
  }
  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */

  public void setIntervals(ArrayList<Interval> intervals) {
    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    this.intervals = intervals;

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";
  }

  public void addChildren(ProjectComponent children) {}

  public void removeChildren(ProjectComponent children) {}

  private Interval getCurrentInterval() {
    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    int index = this.intervals.size() - 1;
    return this.intervals.get(index);
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public void acceptVisitor(ProjectVisitor visitor) {
    // Precondiciones
    assert visitor != null : "El visitor no debe ser null.";

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    visitor.visitTask(this);
    for (Interval interval : this.intervals) {
      interval.acceptVisitor(visitor);
    }

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    //Postcondiciones: No existen postcondiciones para esta función, puesto que no modifica la clase
  }


  private boolean invariants() {
    boolean check = true;

    if (this.getName().isEmpty()) {
      check = false;
    }

    if (this.getParent() == null) {
      check = false;
    } else if (this.getParent().getClass().getSimpleName().equals("Task")) {
      check = false;
    }

    if (this.getDuration().toSeconds() < 0) {
      check = false;
    }

    // Getintervals peta como palomita en el micro

    return check;
  }
}
