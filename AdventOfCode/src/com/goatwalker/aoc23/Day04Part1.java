package com.goatwalker.aoc23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04Part1 {

  public static void main(String[] args) throws Exception {
    Day04Part1 game = new Day04Part1();
    game.doit();
  }

  String datafile = "2023_data\\d04p1.txt";

  long answer;

  private void doit() throws Exception {
    answer = 0;
    loadData();
    System.out.println("Answer: " + answer);
  }

  private void loadData() throws Exception {
    int row = 0;
    BufferedReader br = new BufferedReader(new FileReader(datafile));
    String line = br.readLine();
    while (line != null) {
      System.out.format("  %-2d: %s ", row, line);
      processLine(line, row++);
      line = br.readLine();
    }
    System.out.println("processed " + row + " lines");
    br.close();
  }

  private void processLine(String line, int row) throws Exception {
    final String regex = "Card +(\\d+): ([\\d ]+) \\| ([\\d ]+)";
    final Pattern pattern = Pattern.compile(regex);
    final Matcher matcher = pattern.matcher(line);

    System.out.print("  --  ");

    if (!matcher.find() || matcher.groupCount() != 3)
      throw new Exception("syntax? line = " + line);

    List<String> wins = Arrays.asList(matcher.group(2).split(" +"));
    ArrayList<String> givens = new ArrayList<String>(Arrays.asList(matcher.group(3).split(" +")));
    givens.retainAll(wins);
    int reward = givens.size();
    if (reward > 0)
      reward = 1 << (reward - 1);
//    givens.forEach(s -> {
//      System.out.print("[" + s + "] ");
//    });
    System.out.println(givens.size() + " wins, reward = " + reward);

    answer += reward;
  }
}
