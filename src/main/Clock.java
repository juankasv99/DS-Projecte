package main;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * La clase Clock serveix per comptar el temps de tot el programa. És una extensió de la classe
 * Observable, que ens ajuda en que tot el programa pugui fer l'update quan s'actualitza el tick
 * del rellotge
 *
 * @author Grup 1 Torn 422
 * @version 1.0.
 */

public class Clock extends Observable {

  private static Clock uniqueInstance;
  private final Timer timer;
  private int period;
  private LocalDateTime time;
  Logger logger = LoggerFactory.getLogger(Clock.class);

  private Clock() {
    this.timer = new Timer("Timer");
    this.time = LocalDateTime.now();
  }

  /**
   * Amb el mètode getInstance el que ens assegurem és que en tot el programa només existeix
   * una instància de l'objecte clock, per així compartir el mateix entre tots els components.
   *
   * @author Grup 1 Torn 422.
   */
  public static Clock getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new Clock();
    }

    return uniqueInstance;
  }

  /**
   * El mètode Start comença l'execució del timer amb un periode de ticks i un retràs determinat.
   *
   * @author Grup 1 Torn 422
   */
  public void start() {
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {
        tick();
      }
    };
    this.timer.scheduleAtFixedRate(timerTask, 0, this.period * 1000L);
  }

  public void stop() {
    this.timer.cancel();
  }

  public int getPeriod() {
    return this.period;
  }

  public void setPeriod(int period) {
    this.period = period;
  }

  public LocalDateTime getTime() {
    return this.time;
  }

  private void tick() {
    this.time = LocalDateTime.now();
    setChanged();
    notifyObservers();
    logger.trace("El reloj ha hecho tick. Se inician los updates.");
  }
}
