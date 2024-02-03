package com.goatwalker.aoc23;

import java.io.File;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.utils.Pair;

public class Day08Part1 {

  String datafile = "2023_data\\d08p1.txt";
//  String datafile = "2023_data\\d08p1.txt";

  public static void main(String[] args) throws Exception {
    Day08Part1 game = new Day08Part1();
    game.doit();
  }

  private void doit() throws Exception {

    loadData();

    int answer = 0;

    String curSpot = "AAA";
    int instructionCount = d8instructions.length();
    char[] instructionArray = d8instructions.toCharArray();

    while (!curSpot.equals("ZZZ")) {
      char instruction = instructionArray[answer++ % instructionCount];
      curSpot = instruction == 'L' ? d8Map.get(curSpot).x : d8Map.get(curSpot).y;
      System.out.printf("step %d goes to %s\n", answer, curSpot);
    }

    System.out.println("Answer: " + answer);
  }

  String d8instructions;
  TreeMap<String, Pair<String, String>> d8Map = new TreeMap<String, Pair<String, String>>();

  private void loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);

    d8instructions = scanner.next();
    System.out.println("Found instructions " + d8instructions);

    scanner.nextLine();
    scanner.nextLine();

    final String regex = "(\\w+) = \\((\\w+), (\\w+)\\)";
    final Pattern pattern = Pattern.compile(regex);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
//      System.out.println("line: " + line);
      final Matcher matcher = pattern.matcher(line);

      if (!matcher.find())
        throw new Exception("syntax issue with " + line);

      String lhs = matcher.group(1);
      String rhs1 = matcher.group(2);
      String rhs2 = matcher.group(3);

      Pair<String, String> old = d8Map.put(lhs, new Pair<String, String>(rhs1, rhs2));
      if (old != null)
        throw new Exception("logic");
    }

    scanner.close();

  }

}
