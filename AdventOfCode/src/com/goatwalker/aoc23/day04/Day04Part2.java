package com.goatwalker.aoc23.day04;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04Part2 {

  public static void main(String[] args) throws Exception {
    Day04Part2 game = new Day04Part2();
    game.doit();
  }

  String datafile = "2023_data\\d04p1.txt";

  ArrayList<Integer> winList = new ArrayList<Integer>();
  ArrayList<Integer> copyCntList = new ArrayList<Integer>();

  private void doit() throws Exception {
    loadData();

//    winList.forEach(s -> {
//      System.out.print(s + " ");
//    });
//    System.out.println();
//    System.out.println();
//    System.out.print("copies: ");
//    copyCntList.forEach(s -> {
//      System.out.print(s + " ");
//    });
//    System.out.println();

    for (int jj = 0; jj < winList.size(); jj++) {
      int winCnt = winList.get(jj);
      int add = copyCntList.get(jj);
      for (int kk = 0; kk < winCnt; kk++) {
        int index = jj + kk + 1;
        int curCnt = copyCntList.get(index);
        copyCntList.set(index, curCnt + add);
//        System.out.format("for index %d, added %d to oldval=%d\n", index, add, curCnt);
      }
//      System.out.print("adding = " + add + ", copies: ");
//      copyCntList.forEach(s -> {
//        System.out.print(s + " ");
//      });
//      System.out.println();
    }

    long answer = 0;
    for (int jj = 0; jj < copyCntList.size(); jj++) {
      int copyCnt = copyCntList.get(jj);
      System.out.format("  Card %-3d: %d copies\n", jj, copyCnt);
      answer += copyCnt;

    }
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

    if (!matcher.find() || matcher.groupCount() != 3)
      throw new Exception("syntax? line = " + line);

    List<String> winNums = Arrays.asList(matcher.group(2).split(" +"));
    ArrayList<String> givens = new ArrayList<String>(Arrays.asList(matcher.group(3).split(" +")));
    givens.retainAll(winNums);
    int winCnt = givens.size();
    System.out.println("  --  winCt = " + winCnt);
    winList.add(winCnt);
    copyCntList.add(1);
  }
}
