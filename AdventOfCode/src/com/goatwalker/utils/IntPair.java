package com.goatwalker.utils;

public class IntPair extends Pair<Integer, Integer> {

  public static final IntPair origin = new IntPair(0, 0);

  public IntPair(Integer x, Integer y) {
    super(x, y);
  }

  public IntPair(IntPair loc) {
    super(loc.x, loc.y);
  }

  public IntPair() {
    super(0, 0);
  }

  @Override
  public String toString() {
    return String.format("[%d %d]", x, y);
  }

}
