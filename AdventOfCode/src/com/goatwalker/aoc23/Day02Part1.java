package com.goatwalker.aoc23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day02Part1 {

  public static void main(String[] args) throws Exception {
    Day02Part1 game = new Day02Part1();
    game.doit();
  }

  final String dfile = "2023_data\\d02p1.txt";

  final String[] colorNames = { "red", "green", "blue" };
  final Pattern pGame;
  final Pattern pGameDataOneColor;

  long sum = 0;

  // initialization
  public Day02Part1() throws Exception {
    pGame = Pattern.compile("^Game (\\d+):");
    pGameDataOneColor = Pattern.compile("(\\d+) (red|green|blue)");
  }

  private void doit() throws Exception {
    try (BufferedReader br = new BufferedReader(new FileReader(dfile))) {
      String line = br.readLine();
      while (line != null) {
        System.out.print("  " + line);
        processLine(line);
        line = br.readLine();
      }
    }
    System.out.println("Answer: " + sum);
  }

  private void processLine(String line) throws Exception {

    long[] maxVals = { 0, 0, 0 };

    Matcher matcher = pGame.matcher(line);
    if (!matcher.find())
      throw new Exception("syntax problem with " + line);
//    int gameNum = Integer.parseInt(matcher.group(1));
    for (String gameData : line.substring(matcher.end()).split(";")) {
      for (String gameDataOneColor : gameData.split(",")) {
//        System.out.println(gameDataOneColor);
        Matcher mData = pGameDataOneColor.matcher(gameDataOneColor);
        if (!mData.find())
          throw new Exception("huh?  string = " + gameDataOneColor);
        int cnt = Integer.parseInt(mData.group(1));
        String color = mData.group(2);
        int colorIndex = color.equals("red") ? 0 : (color.equals("green") ? 1 : 2);
        if (cnt > maxVals[colorIndex])
          maxVals[colorIndex] = cnt;
      }
    }
    long power = maxVals[0] * maxVals[1] * maxVals[2];
    System.out.println(" -- power = " + power);
    sum += power;
  }

}
