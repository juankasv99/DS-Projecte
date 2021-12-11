package main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * La classe Project és el component projecte que engloba tasques i altres projectes del
 * timetracker. Com a paràmetres importants té l'element pare del que procedeix, una
 * llista amb els elements que engloba, etc. Tots menys la llista de fills les hereda
 * de la seva classe heredada, ProjectComponent.
 *
 * @author Grup 1 Torn 422.
 * @version 1.0.
 */
public class Project extends ProjectComponent {

  private ArrayList<ProjectComponent> children;
  Logger logger = LoggerFactory.getLogger(Project.class);

  /**
   * Constructor principal on rep el nom que se li vol posar al projecte que es vol crear
   * i l'element pare amb el que es vol relacionar.
   *
   * @author Grup 1 Torn 422.
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
   * Aquest constructor rep els atributs d'una classe projecte, i és la que es crida quan és vol
   * construir un projecte prefabricat, com per exemple quan estem carregant un JSON.
   *
   * @author Grup 1 Torn 422.
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
   * Funció que ens ajuda a accedir als elements fills del projecte des d'un element exterior.
   *
   * @author Grup 1 Torn 422.
   */
  public ArrayList<ProjectComponent> getChildren() {
    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    return children;
  }

  /**
   * Funció que ens ajuda a establir quins són els elements que composa el project actual.
   *
   * @author Grup 1 Torn 422.
   */
  public void setChildren(ArrayList<ProjectComponent> children) {
    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";

    this.children = children;

    // Invariants
    assert this.invariants() : "Los invariants no se cumplen.";
  }

  /**
   * Funció que ens ajuda a afegir un element als fills del projecte.
   *
   * @author Grup 1 Torn 422.
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
   * Funció que ens ajuda a eliminar un element fill que passem per paràmetre del projecte.
   *
   * @author Grup 1 Torn 422.
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
   * Funció complementària del patró visitor. Segons el visitor que el crida fa una funció
   * diferent, segons la seva especificació.
   *
   * @author Grup 1 Torn 422.
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

  @Override
  public JSONObject toJson(int level) {
    JSONObject json = new JSONObject();

    json.put("id", this.getId());
    json.put("type", this.getClass().getSimpleName());
    json.put("name", this.getName());
    json.put("startTime", this.getStartTime());
    json.put("endTime", this.getEndTime());
    json.put("duration", this.getDuration().toSeconds());
    if (this.getParent() != null) {
      json.put("parent", this.getParent().getName());
    }

    JSONArray jsonChildren = new JSONArray();
    if (level > 0) {
      for (ProjectComponent child : this.getChildren()) {
        jsonChildren.put(child.toJson(level - 1));
      }
    }
    json.put("children", jsonChildren);

    return json;
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
