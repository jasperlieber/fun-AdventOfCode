package com.goatwalker.aoc23;

import java.io.BufferedReader;
import java.io.FileReader;

public class Day01Part1 {

  public static void main(String[] args) throws Exception {
    Day01Part1 game = new Day01Part1();
    game.doit();
  }

  int sum = 0;

  private void doit() throws Exception {

    String dfile = "2023_data\\d01p1.txt";
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

    int lineLen = line.length();
    int d1 = -1, d2 = -1;

    for (int i = 0; i < lineLen; i++) {
      char c = line.charAt(i);
      if (Character.isDigit(c)) {
        d1 = Character.getNumericValue(c);
        break;
      }
    }

    for (int i = lineLen; --i >= 0;) {
      char c = line.charAt(i);
      if (Character.isDigit(c)) {
        d2 = Character.getNumericValue(c);
        break;
      }
    }

    if (d1 == -1 || d2 == -1)
      throw new Exception("did not find 2 digits");

    sum += 10 * d1 + d2;

    System.out.println("digits are " + d1 + " & " + d2 + ", sum = " + sum);
  }
}
