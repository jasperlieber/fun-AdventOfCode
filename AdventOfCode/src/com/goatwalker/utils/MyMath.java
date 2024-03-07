package com.goatwalker.utils;

import java.util.ArrayList;
import java.util.Collection;

public class MyMath {
  public static long lcm(long number1, long number2) {
    if (number1 == 0 || number2 == 0) {
      return 0;
    }
    long absNumber1 = Math.abs(number1);
    long absNumber2 = Math.abs(number2);
    long absHigherNumber = Math.max(absNumber1, absNumber2);
    long absLowerNumber = Math.min(absNumber1, absNumber2);
    long lcm = absHigherNumber;
    while (lcm % absLowerNumber != 0) {
      lcm += absHigherNumber;
    }
    return lcm;
  }

  public static long lcmOfSet(Collection<Long> nums) {
    ArrayList<Long> list = new ArrayList<Long>(nums);
    long lcm = lcm(list.get(0), list.get(1));
    for (int jj = 2; jj < list.size(); jj++)
      lcm = lcm(lcm, list.get(jj));
    return lcm;
  }

}
