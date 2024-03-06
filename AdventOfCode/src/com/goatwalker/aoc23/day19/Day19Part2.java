package com.goatwalker.aoc23.day19;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.utils.IntPair;

/**
 * Puzzle description: https://adventofcode.com/2023/day/19
 * 
 * The solution to this problem involves walking all the logical forks of the
 * workflows, and accumulating the permissable ranges for each fork. I use a
 * queue to implement the tree walk, initialized with the "in" rule along with
 * four default ranges of 1-4000. For each fork, a trueFork and falseFork node
 * is computed, with the allowable ranges updates, and both added to the queue.
 * The queue is processed until all it's empty, with accepted nodes added to an
 * accepted list. Then the final answer can be calculated as the sum of the
 * products of all the (non-negative) ranges.
 * 
 */
public class Day19Part2 {

//  String datafile = "src/com/goatwalker/aoc23/day19/data/test.txt";
  String datafile = "src/com/goatwalker/aoc23/day19/data/input.txt";

  public static void main(String[] args) throws Exception {
    Day19Part2 game = new Day19Part2();
    game.doit();

  }

  private void doit() throws Exception {
    loadData();
    walkRules();
    reportResults();
  }

  /**
   * Hold the possible min/max ranges of x-m-a-s values that for a rule. The class
   * holds all the rules for a workflow, and the index of the the rule being
   * processed, and 4 arrays for the min/max values.
   *
   */
  class D19RuleRanges {
    @Override
    public String toString() {
      String out = Arrays.toString(rules) + ", index = " + ruleIndex + ": " + rules[ruleIndex];
      for (int jj = 0; jj < 4; jj++)
        out += " [" + minMax[jj].x + " " + minMax[jj].y + "]";

      return out;
    }

    public D19RuleRanges(String[] rules, int ruleIndex, IntPair[] minMax) {
      this.rules = rules;
      this.ruleIndex = ruleIndex;
      for (int jj = 0; jj < 4; jj++)
        this.minMax[jj] = new IntPair(minMax[jj]);
    }

    final String[] rules;
    final int ruleIndex;
    final IntPair[] minMax = new IntPair[4];
  }

  /**
   * Map of rule name to array of rules
   */
  class D19Rules extends HashMap<String, String[]> {
    private static final long serialVersionUID = 1L;
  }

  ArrayList<D19RuleRanges> d19accepted = new ArrayList<D19RuleRanges>();

  D19Rules d19rules = new D19Rules();

  /**
   * The answer can be calculated as the sum of the product of the 4 non-negative
   * ranges for each accepted rule range.
   */
  private void reportResults() {
    long answer = 0;
    System.out.println("Accepted:");
    for (D19RuleRanges ruleRanges : d19accepted) {
      System.out.println("  " + Arrays.toString(ruleRanges.minMax));
      long product = 1;
      for (int jj = 0; jj < 4; jj++) {
        long range = ruleRanges.minMax[jj].y - ruleRanges.minMax[jj].x + 1;
        if (range < 0) {
          System.out.println("neg");
          range = 0;
        }
        product *= range;
      }
      answer += product;

    }
    System.out.println("Answer = " + answer);

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

    } finally {
      scanner.close();
    }

  }

  /**
   * Do a breadth first search with a queue of all the rules, updating the min/max
   * ranges for how the rule can be reached. Place all accepted results in the
   * accepted array.
   * 
   * @throws Exception
   */
  public void walkRules() throws Exception {

    ArrayList<D19RuleRanges> ruleQueue = new ArrayList<D19RuleRanges>();

    String[] rules = d19rules.get("in");
    IntPair[] minMax = new IntPair[4];
    for (int jj = 0; jj < 4; jj++)
      minMax[jj] = new IntPair(1, 4000);

    D19RuleRanges curRule = new D19RuleRanges(d19rules.get("in"), 0, minMax);
    ruleQueue.add(curRule);

    while (true) {

      if (ruleQueue.isEmpty())
        break;

      curRule = ruleQueue.remove(0);

      System.out.println("  Processing " + curRule + ":");

      String rule = curRule.rules[curRule.ruleIndex];

      if (rule.equals("A")) {
        System.out.println("    accepted");
        d19accepted.add(curRule);
      } else if (rule.equals("R")) {
        System.out.println("    rejected");
      } else if (rule.matches("^\\w+$")) {
        rules = d19rules.get(rule);
        if (rules == null)
          throw new Exception("huh?  rule = " + rule);
        ruleQueue.add(new D19RuleRanges(rules, 0, curRule.minMax));
        System.out.println("    xfer to " + rule + " with rules " + Arrays.toString(rules));
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
        int xmasIndex = getIndex(xmasChar);

        // initialize 2 new forks with current minMax ranges
        // the minMax.x holds the minimum value allowed in the range
        // the minMax.y holds the maximum value allowed
        // define: the minMax value is inclusive of the range
        //
        String[] nextRule = d19rules.get(ifThen);
        if (nextRule == null)
          // if no rule, must be A or R
          nextRule = new String[] { ifThen };
        D19RuleRanges trueFork = new D19RuleRanges(nextRule, 0, curRule.minMax);
        D19RuleRanges falseFork = new D19RuleRanges(curRule.rules, curRule.ruleIndex + 1,
            curRule.minMax);

        if (angle == '>') {
          if (num > trueFork.minMax[xmasIndex].x + 1) {
            trueFork.minMax[xmasIndex].x = num + 1;
          }
          if (num < falseFork.minMax[xmasIndex].y) {
            falseFork.minMax[xmasIndex].y = num;
          }
        } else {
          if (num < trueFork.minMax[xmasIndex].y - 1) {
            trueFork.minMax[xmasIndex].y = num - 1;
          }
          if (num > falseFork.minMax[xmasIndex].x) {
            falseFork.minMax[xmasIndex].x = num;
          }
        }

        ruleQueue.add(trueFork);
        ruleQueue.add(falseFork);

        System.out.println("    for rule '" + rule + "', adding these two:");
        System.out.println("          true: " + trueFork);
        System.out.println("         false: " + falseFork);
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

}
