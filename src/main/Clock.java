package main;

import java.util.Observable;

public class Clock extends Observable {
  private static Clock uniqueInstance;

  private Clock() {}

  public Clock getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new Clock();
    }

    return uniqueInstance;
  }

  public void tick() {
    setChanged();
    notifyObservers();
  }
}
