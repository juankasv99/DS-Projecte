package main;

import java.util.ArrayList;

public class Project extends ProjectComponent {

  private ArrayList<ProjectComponent> children;

  public Project(String name, ProjectComponent parent) {

    super(name, parent);

    this.children = new ArrayList<>(); //La inicializamos así en el primer "add" no da error de lista sin inicializar

  }

  public ArrayList<ProjectComponent> getChildren() {
    return children;
  }

  public void addChildren(ProjectComponent appendChildren) {
    this.children.add(appendChildren);
  }

  public void removeChildren(ProjectComponent children) {
    this.children.remove(children);
  }

  public void acceptVisitor(ProjectVisitor visitor) {
    visitor.visitProject(this);
    for(ProjectComponent component : this.children) {
      component.acceptVisitor(visitor);
    }
  }
}
