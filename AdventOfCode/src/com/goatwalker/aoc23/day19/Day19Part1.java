package com.goatwalker.aoc23.day19;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Puzzle description: https://adventofcode.com/2023/day/19
 * 
 * The solution to this involved feeding each input value into a the rules. The
 * rules are iteratively surfed until the input is rejected or accepted, and
 * accepted inputs are put on an accepted list.
 */
public class Day19Part1 {

  String datafile = "src/com/goatwalker/aoc23/day19/data/test.txt";
//  String datafile = "src/com/goatwalker/aoc23/day19/data/input.txt";

  public static void main(String[] args) throws Exception {
    Day19Part1 game = new Day19Part1();
    game.doit();

  }

  private void doit() throws Exception {
    loadData();
    processInput();
    reportResults();
  }

  D19Rules d19rules = new D19Rules();
  D19Input d19input = new D19Input();
  D19Input d19accepted = new D19Input();

  private void reportResults() {
    long answer = 0;
    for (int[] input : d19accepted) {
      answer += input[0] + input[1] + input[2] + input[3];
    }
    System.out.println("Answer = " + answer);

  }

  /**
   * For each input, follow the rules to discover if accepted or rejected. "rules"
   * contains the current set of rules being used, and "ruleIndex" is the current
   * rule.
   * 
   * @throws Exception
   */
  public void processInput() throws Exception {
    for (int[] input : d19input) {

      String[] rules = d19rules.get("in");
      int ruleIndex = 0;
      String rule = rules[0];

      System.out.println(
          "  Processing " + Arrays.toString(input) + " with rules " + Arrays.toString(rules) + ":");

      while (true) {
        if (rule.equals("A")) {
          System.out.println("    accepted");
          d19accepted.add(input);
          break;
        } else if (rule.equals("R")) {
          System.out.println("    rejected");
          break;
        } else if (rule.matches("^\\w+$")) {
          rules = d19rules.get(rule);
          if (rules == null)
            throw new Exception("huh?  rule = " + rule);
          ruleIndex = 0;
          System.out.println("    xfer to " + rule + " with rules " + Arrays.toString(rules));
          rule = rules[0];
        } else {
          final String regex = "^([xmas])([<>])(\\d+):(\\w+)$";
          final Pattern pattern = Pattern.compile(regex);
          final Matcher matcher = pattern.matcher(rule);
          if (!matcher.find())
            throw new Exception("huh?  rule = " + rule);
          char xmasChar = matcher.group(1).charAt(0);
          char angle = matcher.group(2).charAt(0);
          int num = Integer.parseInt(matcher.group(3));
          String ifThen = matcher.group(4);
          int cmp = input[getIndex(xmasChar)] - num;
          boolean test = angle == '<' ? (cmp < 0) : (cmp > 0);
          System.out.println("    rule '" + rule + "' " + (test ? "passes" : "fails"));
          rule = test ? ifThen : rules[++ruleIndex];
        }
      }
    }
  }

  /**
   * Given a xmas character, return the 0-3 index of the input values array
   * 
   * @param char of x-m-a-s
   * @return index of 0-1-2-3
   */
  private int getIndex(char c) {
    switch (c) {
    case 'x':
      return 0;
    case 'm':
      return 1;
    case 'a':
      return 2;
    case 's':
      return 3;
    }
    return -1;
  }

  /**
   * Map of rule name to array of rules
   */
  class D19Rules extends HashMap<String, String[]> {
    private static final long serialVersionUID = 1L;
  }

  /**
   * Array of all the input, each of which are just 4 ints
   *
   */
  class D19Input extends ArrayList<int[]> {
    private static final long serialVersionUID = 1L;
  }

  private void loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);

    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        final String regex = "^(\\w+)\\{(.*)\\}$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(line);
        if (!matcher.find())
          break;
        String[] deets = matcher.group(2).split(",");
        String label = matcher.group(1);
        d19rules.put(label, deets);
      }
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

        final String regex = "^\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)\\}$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(line);
        if (!matcher.find())
          throw new Exception("huh? " + line);
        int[] xmas = new int[4];
        for (int jj = 0; jj < 4; jj++) {
          xmas[jj] = Integer.parseInt(matcher.group(jj + 1));
        }
        d19input.add(xmas);
      }

    } finally {
      scanner.close();
    }
  }
}
