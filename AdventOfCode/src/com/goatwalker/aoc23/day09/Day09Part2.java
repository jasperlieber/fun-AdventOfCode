package com.goatwalker.aoc23.day09;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Day09Part2 {

  String datafile = "2023_data\\d09p1.txt";

  long answer;

  public static void main(String[] args) throws Exception {
    Day09Part2 game = new Day09Part2();
    game.doit();
  }

  private void doit() throws Exception {
    loadData();
    System.out.println("Answer = " + answer);
  }

  private void loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);

    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        System.out.println(line);
        long[] vals = Arrays.stream(line.split(" ")).mapToLong(Long::parseLong).toArray();
        processLine(vals);
      }
    } finally {
      scanner.close();
    }
  }

  private void processLine(long[] vals) throws Exception {

    long[] temp1 = vals.clone();
    long[] temp2 = vals.clone();
    long[] startVals = vals.clone();

    int depth = 0; // vals.length;

    while (true) {
      if (depth == vals.length - 1)
        throw new Exception("bottomed out!");
      startVals[depth] = temp1[0];
      boolean done = true;
      System.out.print(" ");
      for (int jj = 0; jj < vals.length - depth - 1; jj++) {
        temp2[jj] = temp1[jj + 1] - temp1[jj];
        System.out.printf("%2d ", temp2[jj]);

        done &= temp2[jj] == 0;
      }
      System.out.println(" -- startVals[" + depth + "] = " + startVals[depth]);
      if (done)
        break;
      depth++;
      temp1 = temp2.clone();
    }

    temp1[depth + 1] = 0;

    for (int jj = depth; jj >= 0; jj--) {
      temp1[jj] = startVals[jj] - temp1[jj + 1];
      System.out.print(" s[" + jj + "]=" + startVals[jj]);
    }
    System.out.println();

    for (int jj = depth; jj >= 0; jj--) {
      System.out.print(" t[" + jj + "]=" + temp1[jj]);
    }
    System.out.println();
    System.out.println(" ans = " + temp1[0]);
    answer += temp1[0];
  }

}
