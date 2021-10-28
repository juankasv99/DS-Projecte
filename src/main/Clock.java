package main;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Clock extends Observable {

  private static Clock uniqueInstance;
  private Timer timer;
  private int period;
  private LocalDateTime time;

  private Clock() {
    this.timer = new Timer("Timer");
    this.time = LocalDateTime.now();
  }

  public static Clock getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new Clock();
    }

    return uniqueInstance;
  }

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
  }
}
