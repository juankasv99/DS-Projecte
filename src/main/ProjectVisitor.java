package main;

/**
 * Some javadoc.
 *
 * @author Some javadoc.
 * @version Some javadoc.
 * @deprecated Some javadoc.
 */

public interface ProjectVisitor {
  void visitProject(Project project);

  void visitTask(Task task);

  void visitInterval(Interval interval);
}
