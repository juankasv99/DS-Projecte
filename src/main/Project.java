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
 */
public class Project extends ProjectComponent {

  private ArrayList<ProjectComponent> children;
  Logger logger = LoggerFactory.getLogger(Project.class);

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public Project(String name, ProjectComponent parent) {
    super(name, parent);

    // Precondiciones
    assert this.children == null : "La lista de hijos del Project"
        + " debe ser null antes de ser creado.";

    this.children = new ArrayList<>();
    if (getParent() == null) {
      logger.debug("Se crea Project " + name + ", que no tiene padre.");

    } else {
      logger.debug("Se crea Project " + name + ", hijo de " + parent.getName());
    }

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    // Postcondiciones
    assert this.getName().equals(name) : "El nombre del Project"
        + " debe ser el mismo que el de la variable name.";
    assert this.getParent() == null || this.getParent().equals(parent) : "El padre del Project"
        + " debe ser el mismo que el de la variable parent.";
    assert this.getDuration().toSeconds() == 0 : "La duración del Project"
        + " debe ser 0 justo después de ser creado.";
    assert this.children != null : "La lista de hijos del Project"
        + " no debe ser null just después de ser creado.";
    assert this.children.isEmpty() : "La lista de hijos del"
        + " Project debe estar vacía justo después de ser creado.";
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public Project(String name, LocalDateTime startTime,
                 LocalDateTime endTime, Duration duration, ProjectComponent parent) {
    super(name, parent);
    super.setStartTime(startTime);
    super.setEndTime(endTime);
    super.setDuration(duration);

    // Precondiciones
    assert this.children == null : "La lista de hijos del Project"
        + " debe ser null antes de ser creado.";

    this.children = new ArrayList<>();

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    // Postcondiciones
    assert this.getName().equals(name) : "El nombre del Project"
        + " debe ser el mismo que el de la variable name.";
    assert this.getParent() == null || this.getParent().equals(parent) : "El padre del Project"
        + " debe ser el mismo que el de la variable parent.";
    assert this.getDuration().toSeconds() == 0 : "La duración del Project"
        + " debe ser 0 justo después de ser creado.";
    assert this.children != null : "La lista de hijos del Project"
        + " no debe ser null just después de ser creado.";
    assert this.children.isEmpty() : "La lista de hijos del Project"
        + " debe estar vacía justo después de ser creado.";
  }

  @Override
  public void update(Interval activeInterval) {
    // Precondiciones
    assert activeInterval != null : "El Interval activo no debe ser null.";
    assert this.children != null : "La lista de hijos del Project no debe ser null.";

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    Duration counter = Duration.ZERO;
    /* duracion = Suma de las duraciones de los hijos */
    for (ProjectComponent component : this.children) {
      counter = counter.plus(component.getDuration());
    }

    super.setDuration(counter);
    super.setEndTime(activeInterval.getEndTime());

    if (super.getParent() != null) {
      super.getParent().update(activeInterval);
    }

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    // Postcondiciones
    assert this.getDuration().toSeconds() >= 0 : "La duración del Project"
        + " debe ser mayor o igual 0.";
    assert this.getEndTime().equals(activeInterval.getEndTime()) : "El tiempo"
        + " de finalización del Project debe ser el mismo que el del Interval activo.";
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public ArrayList<ProjectComponent> getChildren() {
    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    return children;
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public void setChildren(ArrayList<ProjectComponent> children) {
    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    this.children = children;

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public void addChildren(ProjectComponent children) {
    // Precondiciones
    assert children != null : "El hijo"
        + " que va a ser añadido a la lista de hijos del Project no debe ser null.";
    final int sizeBeforeInsert = this.children.size();

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    this.children.add(children);

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    // Postcondiciones
    assert this.children.size() == sizeBeforeInsert + 1 : "El tamaño"
        + " de la lista de hijos del Project debe aumentar en 1 después de insertar al hijo.";
    assert this.children.contains(children) : "La lista de hijos"
        + " del Project debe contener al hijo después de insertarlo.";
  }

  /**
   * Some javadoc.
   *
   * @author Some javadoc.
   */
  public void removeChildren(ProjectComponent children) {
    // Precondiciones
    assert children != null : "El hijo que va a ser eliminado"
        + " de la lista de hijos del Project no debe ser null.";
    final int sizeBeforeRemove = this.children.size();

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    this.children.remove(children);

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    // Postcondiciones
    assert this.children.size() == sizeBeforeRemove - 1 : "El tamaño"
        + " de la lista de hijos del Project debe disminuir en 1 después de eliminar al hijo.";
    assert !this.children.contains(children) : "La lista de hijos"
        + " del Project no debe contener al hijo después de eliminarlo.";
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

    visitor.visitProject(this);
    for (ProjectComponent component : this.children) {
      component.acceptVisitor(visitor);
    }

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    //Postcondiciones: No existen postcondiciones para esta función, puesto que no modifica la clase
  }

  private boolean invariants() {
    boolean check = !this.getName().isEmpty();

    if (this.getParent() != null) {
      if (this.getParent().getClass().getSimpleName().equals("Task")) {
        check = false;
      }
    }

    if (this.getDuration().toSeconds() < 0) {
      check = false;
    }

    // Getchildren peta como palomita en el micro

    return check;
  }
}
