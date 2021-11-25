package main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aquesta classe sempre tindra com a pare un project.
 * Cada vegada que es treballi en una tasca creara un interval.
 * Aquest interval s'afegira a la llista d'intervals de la classe.
 * Son aquest intervals els que acualitzen els valors de temps.
 * d'aquesta classe i auqesta actualitza al seu pare.
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */
public class Task extends ProjectComponent {

  private ArrayList<Interval> intervals;

  Logger logger = LoggerFactory.getLogger(Task.class);

  /**
   * Constructor de la classe.
   * Sempre ha de tenir un nom i un pare.
   *
   * @author Grup 1 Torn 422
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
   * Constructor de la classe que utilitza el loadjson.
   * Crea una Task amb els valors de temps afegits.
   *
   * @author Grup 1 Torn 422
   */
  public Task(String name, LocalDateTime startTime,
              LocalDateTime endTime, Duration duration, ProjectComponent parent) {
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
   * Creara un nou interval i sel afegeix a la llista d'intervals.
   * A mes aquest interval comerçara a actualitzar la task amb els tick del Clock.
   *
   * @author Grup 1 Torn 422
   */
  public void startTask() {
    // Precondiciones
    assert this.intervals != null : "La lista de Intervals de la Task no debe ser null.";
    final int sizeBeforeInsert = this.intervals.size();

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    int zeroSecondsDelay = 0;
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
   * Creara un nou interval amb delay i sel afegeix a la llista d'intervals.
   * A mes aquest interval comerçara a actualitzar la task amb els tick del Clock.
   *
   * @author Grup 1 Torn 422
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
   * La task avisa al Interval de que deixi d'escoltar al Clock.
   * i per tant ja no actualitza la task amb cada tik del CLock.
   *
   * @author Grup 1 Torn 422
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
   * Retorna els Intervals.
   *
   * @author Grup 1 Torn 422
   */
  public ArrayList<Interval> getIntervals() {
    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    return this.intervals;
  }
  /**
   * Guarda la llista de intervals que es passa per parametre en la seva llista.
   *
   * @author Grup 1 Torn 422
   */

  public void setIntervals(ArrayList<Interval> intervals) {
    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    this.intervals = intervals;

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";
  }

  public void addChildren(ProjectComponent children) {}

  // public void removeChildren(ProjectComponent children) {}

  private Interval getCurrentInterval() {
    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    int index = this.intervals.size() - 1;
    return this.intervals.get(index);
  }

  /**
   * Crida a la funcio visitTask i acceptViisitor dels seus intervals.
   *
   * @author Grup 1 Torn 422
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
    boolean check = !this.getName().isEmpty();

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
