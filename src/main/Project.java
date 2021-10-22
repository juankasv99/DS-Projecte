package main;

import java.util.ArrayList;

public class Project extends ProjectComponent {

  private ArrayList<ProjectComponent> children;

  public Project(String name, ProjectComponent parent) {
    super(name, parent);
  }

  public ArrayList<ProjectComponent> getChildren() {
    return children;
  }
}
