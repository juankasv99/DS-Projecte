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

  public Project(String name, LocalDateTime startTime, LocalDateTime endTime, Duration duration, ProjectComponent parent) {
    super(name, parent);
    super.setStartTime(startTime);
    super.setEndTime(endTime);
    super.setDuration(duration);
    this.children = new ArrayList<>();
  }

  @Override
  public void update(Interval activeInterval) {
    Duration counter = Duration.ZERO;
    for(ProjectComponent component : this.children) {
      counter = counter.plus(component.getDuration());
    }

    super.setDuration(counter);
    super.setEndTime(activeInterval.getEndTime());

    if(super.getParent() != null) {
      super.getParent().update(activeInterval);
    }
  }

  public ArrayList<ProjectComponent> getChildren() {
    return children;
  }

  public void setChildren(ArrayList<ProjectComponent> children) {
    this.children = children;
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
