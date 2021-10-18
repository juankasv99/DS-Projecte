package main;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Clock extends Observable {

  private static Clock uniqueInstance;
  private Timer timer;
  private TimerTask timerTask;

  private Clock() {
    this.timer = new Timer("Timer");
    this.timerTask = new TimerTask() {
      @Override
      public void run() {
        tick();
      }
    };
    this.timer.scheduleAtFixedRate(timerTask, 0, 1000);
  }

  public static Clock getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new Clock();
    }

    return uniqueInstance;
  }

  private void tick() {
    setChanged();
    notifyObservers();
  }
}
