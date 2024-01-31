package com.goatwalker.aoc21;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Day06Part2 {

//  private static final boolean debug = false;

  public static void main(String[] args) throws Exception {
    Day06Part2 me = new Day06Part2();
    me.run();
  }

  private void run() throws Exception {

//    String dfile = "2021_data\\d06test.txt";
    String dfile = "2021_data\\d06.txt";

    Path path = Paths.get(dfile);
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    String[] starts = lines.get(0).split(",");

    int cnts[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // 9 spots

    for (String start : starts) {
      int age = Integer.parseInt(start);
      cnts[age]++;
    }
    System.out.println("Day 0: " + Arrays.toString(cnts));

    int p0 = 0;
    int p6 = 6;
    int p8 = 8;
    for (int day = 1; day <= 80; day++) {
      int spawnCnt = cnts[p0];
      p0 = (p0 + 1) % 9;
      p6 = (p6 + 1) % 9;
      p8 = (p8 + 1) % 9;
      cnts[p6] += spawnCnt;
      cnts[p8] = spawnCnt;
      System.out.println(
          "Day " + day + ": " + Arrays.toString(cnts) + " -- total = " + totals(cnts));

    }
  }

  private int totals(int[] cnts) {
    int sum = 0;
    for (int cnt : cnts)
      sum += cnt;
    return sum;
  }

}
