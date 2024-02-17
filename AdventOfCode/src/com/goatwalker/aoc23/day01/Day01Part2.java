package com.goatwalker.aoc23.day01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day01Part2 {

  public static void main(String[] args) throws Exception {
    Day01Part2 game = new Day01Part2();
    game.doit();
  }

  int sum = 0;
  Pattern pDigit;

  // initialization
  public Day01Part2() {
    pDigit = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");
  }

  private void doit() throws Exception {

    String dfile = "src/com/goatwalker/aoc23/day1/day1.txt";
    try (BufferedReader br = new BufferedReader(new FileReader(dfile))) {
      String line = br.readLine();
      while (line != null) {
        System.out.println("line: " + line);
        processLine(line);
        line = br.readLine();
      }
    }
  }

  private void processLine(String line) throws Exception {

    int cnt = 0;
    int start = 0;
    String first = "", last = "";

    Matcher matcher = pDigit.matcher(line);
    while (matcher.find(start)) {
      start = matcher.start() + 1;

      last = matcher.group();
      if (++cnt == 1)
        first = last;
//      System.out.println("  " + cnt + ": " + matcher.group());
    }

    if (cnt == 0)
      throw new Exception("no number found in " + line);

    int d1 = getVal(first);
    int d2 = getVal(last);

    sum += 10 * d1 + d2;

    System.out.println("  val is " + d1 + d2 + ", sum = " + sum);

  }

  private int getVal(String str) throws Exception {
    if (str.length() == 1)
      return Integer.parseInt(str);
    switch (str) {
    case "one":
      return 1;
    case "two":
      return 2;
    case "three":
      return 3;
    case "four":
      return 4;
    case "five":
      return 5;
    case "six":
      return 6;
    case "seven":
      return 7;
    case "eight":
      return 8;
    case "nine":
      return 9;
    default:
      throw new Exception("huh?  " + str);
    }
  }
}
