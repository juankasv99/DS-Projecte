package main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Project extends ProjectComponent {

  private ArrayList<ProjectComponent> children;

  public Project(String name, ProjectComponent parent) {
    super(name, parent);
    this.children = new ArrayList<>();
  }

  @Override
  public void update() {
    Duration counter = Duration.ZERO;

    for(ProjectComponent component : this.children) {
      counter = counter.plus(component.getDuration());
    }

    super.setDuration(counter);
    super.setEndTime(Clock.getInstance().getTime());

    if(super.getParent() != null) {
      super.getParent().update();
    }
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
