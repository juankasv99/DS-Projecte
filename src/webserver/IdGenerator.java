package webserver;

/**
 * Some Javadoc.
 */
public class IdGenerator {

  private static IdGenerator uniqueInstance;
  private int id;

  private IdGenerator() {
    this.id = 0;
  }

  /**
   * Some Javadoc.
   */
  public static IdGenerator getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new IdGenerator();
    }

    return uniqueInstance;
  }

  /**
   * Some Javadoc.
   */
  public int getId() {
    int id =  this.id;
    this.id = this.id + 1;

    return id;
  }
}
