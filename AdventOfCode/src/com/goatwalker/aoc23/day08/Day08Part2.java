package com.goatwalker.aoc23.day08;

import java.io.File;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.utils.Pair;

public class Day08Part2 {

  String datafile = "2023_data\\d08p1.txt";
//  String datafile = "2023_data\\d08p1.txt";

  public static void main(String[] args) throws Exception {
    Day08Part2 game = new Day08Part2();
    game.doit();
  }

  private void doit() throws Exception {

    loadData();

    int instructionCount = d8instructions.length();
    char[] instructionArray = d8instructions.toCharArray();

    System.out.println("startSet = " + startSet);

    BigInteger bigAnswer = BigInteger.ONE;

    // get the length for each initial node -- then take LCM of all the lengths to
    // get answer
    for (String curSpot : startSet) {

      int answer = 0;

      while (curSpot.charAt(2) != 'Z') {
        char instruction = instructionArray[answer++ % instructionCount];
        curSpot = instruction == 'L' ? d8Map.get(curSpot).x : d8Map.get(curSpot).y;
      }

      BigInteger a = BigInteger.valueOf(answer);
      bigAnswer = bigAnswer.multiply(a).divide(bigAnswer.gcd(a));

    }

    System.out.println("answer: " + bigAnswer);
  }

  String d8instructions;
  TreeMap<String, Pair<String, String>> d8Map = new TreeMap<String, Pair<String, String>>();
  HashSet<String> startSet = new HashSet<String>();

  private void loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);

    try {

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

        if (lhs.charAt(2) == 'A')
          if (!startSet.add(lhs))
            throw new Exception("dupe");
      }
    }

    finally {
      scanner.close();
    }

  }

}
