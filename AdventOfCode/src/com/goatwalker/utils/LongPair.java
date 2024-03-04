package com.goatwalker.utils;

public class LongPair extends Pair<Long, Long> {

  public static final LongPair origin = new LongPair(0L, 0L);

  public LongPair(Long x, Long y) {
    super(x, y);
  }

  public LongPair(LongPair loc) {
    super(loc.x, loc.y);
  }

  public LongPair() {
    super(0L, 0L);
  }

  @Override
  public String toString() {
    return String.format("[%d %d]", x, y);
  }

}
