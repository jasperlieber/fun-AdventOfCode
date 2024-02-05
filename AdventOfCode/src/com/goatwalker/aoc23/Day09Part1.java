package com.goatwalker.aoc23;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Day09Part1 {

  String datafile = "2023_data\\d09p0.txt";

  long answer;

  public static void main(String[] args) throws Exception {
    Day09Part1 game = new Day09Part1();
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

    int depth = vals.length;

    while (true) {
      depth--;
      if (depth == 1)
        throw new Exception("bottomed out!");
      startVals[depth] = temp1[0];
      boolean done = true;
      System.out.print(" ");
      for (int jj = 0; jj < depth; jj++) {
        temp2[jj] = temp1[jj + 1] - temp1[jj];
        System.out.print(temp2[jj] + " ");

        done &= temp2[jj] == 0;
      }
      System.out.println(" -- startVals[" + depth + "] = " + startVals[depth]);
      if (done)
        break;
      temp1 = temp2.clone();
    }

    long sum = 0;
    System.out.print("sum = 0");

    for (int jj = depth; jj < vals.length; jj++) {
      System.out.print(" + e[" + jj + "]=" + startVals[jj]);
      sum += startVals[jj];

    }
    System.out.println(" ans = " + sum);

    answer += sum;
  }

}
