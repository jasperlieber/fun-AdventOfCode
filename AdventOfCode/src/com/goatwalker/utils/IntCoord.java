package com.goatwalker.utils;

public class IntCoord extends Pair<Integer, Integer> {

  IntCoord() {
    super(0, 0);
  }

  @Override
  public String toString() {
    return String.format("[%d %d]", x, y);
  }

  public IntCoord(Integer x, Integer y) {
    super(x, y);
  }
}
