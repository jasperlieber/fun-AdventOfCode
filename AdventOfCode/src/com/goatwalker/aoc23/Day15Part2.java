package com.goatwalker.aoc23;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Puzzle description: https://adventofcode.com/2023/day/15
 * 
 * This was pretty easily solved with java's LinkedHashMap class.
 */
public class Day15Part2 {

  String datafile = "2023_data\\day15.txt";

  D15Box[] d15boxes = new D15Box[256];

  public static void main(String[] args) throws Exception {
    Day15Part2 puzzle = new Day15Part2();
    puzzle.doit();
  }

  private void doit() throws Exception {
    for (int jj = 0; jj < 256; jj++)
      d15boxes[jj] = new D15Box();
    long answer = loadData();
    System.out.println("answer = " + answer);
  }

  private long loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);
    try {
      scanner.useDelimiter(",");
      while (scanner.hasNext()) {
        String chunk = scanner.next();
        process_chunk(chunk);
      }

    } finally {
      scanner.close();
    }

    printIt();
    long power = focusPower();
    return power;
  }

  final String regex = "^(\\w+)([=-])(\\d*)$";
  final Pattern pattern = Pattern.compile(regex);

  private void process_chunk(String chunk) {

    final Matcher matcher = pattern.matcher(chunk);

    matcher.find();

    String label = matcher.group(1);
    int boxId = d15hash(label);

    if (matcher.group(2).equals("-")) {
      d15boxes[boxId].remove(label);
    } else {
      int focalLength = Integer.parseInt(matcher.group(3));
      d15boxes[boxId].put(label, focalLength);
    }
  }

  private void printIt() {
    for (int jj = 0; jj < 256; jj++) {
      if (d15boxes[jj].size() != 0)
        System.out.println("  " + jj + ": " + d15boxes[jj]);
    }
  }

  private long focusPower() {
    long sum = 0;
    for (int jj = 0; jj < 256; jj++) {
      if (d15boxes[jj].size() != 0) {
        int cnt = 1;
        for (int v : d15boxes[jj].values()) {
          int pow = (jj + 1) * cnt++ * v;
          sum += pow;
        }
      }
    }
    return sum;
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

  public class D15Box extends LinkedHashMap<String, Integer> {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();
      int cnt = 1;
      for (Map.Entry<String, Integer> entry : entrySet()) {
        String k = entry.getKey();
        int v = entry.getValue();
        int pow = (d15hash(k) + 1) * cnt * v;
        sb.append(String.format("[%s:%d pow=%d] ", entry.getKey(), entry.getValue(), pow));
        cnt++;
      }
      return sb.toString();
    }
  }
}
