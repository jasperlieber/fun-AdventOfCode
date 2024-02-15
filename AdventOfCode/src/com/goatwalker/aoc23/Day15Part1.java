package com.goatwalker.aoc23;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Puzzle description: https://adventofcode.com/2023/day/15
 */
public class Day15Part1 {

  String datafile = "2023_data\\day15.txt";

  public static void main(String[] args) throws Exception {
    Day15Part1 puzzle = new Day15Part1();
    puzzle.doit();
  }

  private void doit() throws Exception {
    long answer = loadData();
    System.out.println("answer = " + answer);
  }

  private long loadData() throws Exception {
    long answer = 0;
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);
    try {
      scanner.useDelimiter(",");
      while (scanner.hasNext()) {
        String chunk = scanner.next();
        int hash = d15hash(chunk);
        answer += hash;
      }

    } finally {
      scanner.close();
    }

    return answer;
  }

  private int d15hash(String chunk) {
    int hash = 0;
    byte[] bytes = chunk.getBytes(StandardCharsets.US_ASCII);
    for (byte b : bytes) {
      hash += b;
      hash *= 17;
      hash %= 256;
    }
    return hash;
  }

}
