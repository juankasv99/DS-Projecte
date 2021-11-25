package main;

/**
 * Aquest es la classe base que implementa els visitors a ProjectComponent.
 * les classes que la implementen sobrescriuen les funcions.
 * d'aquesta classe.
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */

public interface ProjectVisitor {
  void visitProject(Project project);

  void visitTask(Task task);

  void visitInterval(Interval interval);
}
