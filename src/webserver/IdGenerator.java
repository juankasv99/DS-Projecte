package webserver;

/**
 * Aquesta classe s'encarrega de donar id als ProjectComponent.
 * de manera que cadascun tingui una id diferent.
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */
public class IdGenerator {

  private static IdGenerator uniqueInstance;
  private int id;

  private IdGenerator() {
    this.id = 0;
  }

  /**
   * Amb el getInstance ens asegurem que només existeix
   * un unic idGenerator i no tindrem id duplicades.
   *
   */
  public static IdGenerator getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new IdGenerator();
    }

    return uniqueInstance;
  }

  /**
   * Retorna la id actual i la augmenta en 1 per
   * a que el proxim getId tingui una id diferent.
   */
  public int getId() {
    int id =  this.id;
    this.id = this.id + 1;

    return id;
  }
}
