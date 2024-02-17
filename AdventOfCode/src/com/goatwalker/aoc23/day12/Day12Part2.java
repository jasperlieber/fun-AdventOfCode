package com.goatwalker.aoc23.day12;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12Part2 {

  private static final boolean log = false;
  private static final boolean log2 = false;

  String datafile = "2023_data\\day12.txt";

  public static void main(String[] args) throws Exception {
    Day12Part2 game = new Day12Part2();
    game.doit();
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
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.length() == 0)
          return -1;
        answer += processLine(line);
      }
    } finally {
      scanner.close();
    }
    return answer;
  }

  private long processLine(String line) throws Exception {

    String[] reports = line.split(" ");
    if (log)
      System.out.printf("%s %s\n", reports[0], reports[1]);

    String badSpringsReport = reports[0];

    List<Integer> intList = Stream.of(reports[1].split(",")).map(Integer::parseInt)
        .collect(Collectors.toList());
    ArrayList<Integer> springLengths = new ArrayList<Integer>(intList);

    // For Part 2, multiply input by 5
    badSpringsReport = badSpringsReport + "?" + badSpringsReport + "?" + badSpringsReport + "?"
        + badSpringsReport + "?" + badSpringsReport;
    springLengths.addAll(intList);
    springLengths.addAll(intList);
    springLengths.addAll(intList);
    springLengths.addAll(intList);

    long fitCnt = getCountOfPossibilites(badSpringsReport, springLengths);
    if (log)
      System.out.println(fitCnt);

    return fitCnt;
  }

  /**
   * My method for counting possibilities for Part One was to use recursion to
   * generate all possible positions for the damaged springs, which worked for
   * small enough sample data. But Part Two quintuples the input data, and this
   * caused excessively long run times. So, thanks to a detailed description of a
   * solution using dynamic programming from
   * https://www.ericburden.work/blog/2023/12/12/advent-of-code-day-12/, here's my
   * version of a DP solution.
   * 
   * @param badSpringReport
   * @param springLengths
   * @return count of possible ways to fit bad springs into the badSpringsReport
   */
  private long getCountOfPossibilites(String badSprinsgReport, ArrayList<Integer> springLengths) {

    // can remove from initial operational springs since we are trying to find out
    // how to pack damaged springs. But add one initial operational spring to
    // support the counting algorithm.
    badSprinsgReport = "." + badSprinsgReport.replaceAll("^\\.+", "");
    int reportLen = badSprinsgReport.length();

    // Dynamic programming considers solutions under increasing constraints. The
    // first constraint is no damaged springs, then the constraints incrementally
    // increase by the damaged groups provided in the springLengths list. The list
    // also includes an extra initial hypothetical operational springs, so that
    // algorithm works smoothly.

    // The initial state of possibilities is all 1s up to the first damaged spring.
    // Starting Conditions : [., ?, ?, ?, ., #, #, #]
    // Possible Combinations: [1, 1, 1, 1, 1, 1, 0, 0, 0]

    long[] possibilites = new long[reportLen + 1];
    possibilites[0] = 1;
    boolean damaged = false;
    for (int jj = 1; jj <= reportLen; jj++) {
      damaged |= badSprinsgReport.charAt(jj - 1) == '#';
      possibilites[jj] = damaged ? 0 : 1;
    }
    if (log2)
      System.out.println(badSprinsgReport + " " + Arrays.toString(possibilites));

    // Use each group size in badSprinsgReport to incrementally increase the
    // constraints.
    for (int groupSize : springLengths) {

      // build a list of possibilities for springLengths of groupSize length.

      long[] nextPoss = new long[reportLen + 1]; // java conveniently inits these all to zero

      int possiblyDamagedRunSize = 0;

      for (int jj = 1; jj < reportLen; jj++) {
        possiblyDamagedRunSize = badSprinsgReport.charAt(jj) == '.' ? 0
            : (possiblyDamagedRunSize + 1);

        boolean groupCanFit = possiblyDamagedRunSize >= groupSize;
        int precedingIndex = jj - groupSize;
        if (precedingIndex < 0)
          precedingIndex = 0;
        boolean notContiguousDamage = badSprinsgReport.charAt(precedingIndex) != '#';
        boolean isNotDamaged = badSprinsgReport.charAt(jj) != '#';
        boolean damageCanFit = groupCanFit && notContiguousDamage;
        if (isNotDamaged && damageCanFit) {
          nextPoss[jj + 1] = nextPoss[jj] + possibilites[precedingIndex];
        } else if (badSprinsgReport.charAt(jj) == '#' && damageCanFit) {
          nextPoss[jj + 1] = possibilites[precedingIndex];
        } else if (isNotDamaged) {
          nextPoss[jj + 1] = nextPoss[jj];
        } else {
          nextPoss[jj + 1] = 0;
        }
        if (log2)
          System.out.printf(
              "jj=%d ch=%c possiblyDamagedRunSize=%d groupCanFit=%5b precedingIndex=%d notContiguousDamage=%5b isNotDamaged=%5b damageCanFit-%5b\n",
              jj, badSprinsgReport.charAt(jj), possiblyDamagedRunSize, groupCanFit, precedingIndex,
              notContiguousDamage, isNotDamaged, damageCanFit);
      }

      possibilites = nextPoss;

      if (log)
        System.out.println("after len=" + groupSize + ", poss: " + Arrays.toString(possibilites));

    }

    return possibilites[reportLen];
  }

}
