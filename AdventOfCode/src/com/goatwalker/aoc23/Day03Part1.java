package com.goatwalker.aoc23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03Part1 {

  public static void main(String[] args) throws Exception {
    Day03Part1 game = new Day03Part1();
    game.doit();
  }

  String dfile = "2023_data\\d03p1.txt";
  long answer;

  Set<GridNumber> row1nums = new HashSet<GridNumber>(), row2nums = new HashSet<GridNumber>(),
      row3nums = new HashSet<GridNumber>();
  Set<GridItem> winRow2Syms, winRow1Syms;

  private void doit() throws Exception {
    answer = 0;
    loadSchematic();
  }

  private void loadSchematic() throws Exception {
    int row = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(dfile))) {
      String line = br.readLine();
      while (line != null) {
        System.out.format("%-3d: %s ", row, line);
        processLine(line, row++);
        line = br.readLine();
      }
      processLastLine();
      System.out.println("Answer: " + answer);
    }
  }

  private void processLine(String line, int row) throws Exception {

    Set<GridNumber> rowNums = new HashSet<GridNumber>();
    final Pattern pNum = Pattern.compile("(\\d+)");
    Matcher matcher = pNum.matcher(line);
    while (matcher.find()) {
      GridNumber partNum = new GridNumber(row, matcher.group(1), matcher.start(), matcher.end());
      rowNums.add(partNum);
      System.out.format("- %s", partNum);
    }

    winRow1Syms = winRow2Syms;
    winRow2Syms = new HashSet<GridItem>();

    final Pattern pSym = Pattern.compile("([^\\.0-9])");
    matcher = pSym.matcher(line);
    while (matcher.find()) {
      GridItem sym = new GridItem(row, matcher.group(1), matcher.start());
      System.out.format("- %s", sym);
      winRow2Syms.add(sym);
    }

    row1nums = row2nums;
    row2nums = row3nums;
    row3nums = rowNums;
    System.out.println();

    if (row > 0)
      processWindow(row);
  }

  private void processLastLine() {
    System.out.println("process final 2 lines:");

    winRow2Syms.forEach(sym -> {
      row2nums.forEach(num -> num.markIfProximate(sym));
      row3nums.forEach(num -> num.markIfProximate(sym));
    });

    row2nums.forEach(num -> {
      if (num.proximate) {
        answer += num.value;
        System.out.println("  adding " + num);
      }
    });
    row3nums.forEach(num -> {
      if (num.proximate) {
        answer += num.value;
        System.out.println("  adding " + num);
      }
    });
  }

  private void processWindow(int row) {
    System.out.print("window for row " + row + ":");
    System.out.print("\n  row1: ");
    row1nums.forEach(System.out::print);
    System.out.print("\n  row2: ");
    row2nums.forEach(System.out::print);
    System.out.print("\n  row3: ");
    row3nums.forEach(System.out::print);
    System.out.print("\n  syms: ");
    winRow1Syms.forEach(System.out::print);
    System.out.println();

    winRow1Syms.forEach(sym -> {
      row1nums.forEach(num -> num.markIfProximate(sym));
      row2nums.forEach(num -> num.markIfProximate(sym));
      row3nums.forEach(num -> num.markIfProximate(sym));
    });

    row1nums.forEach(num -> {
      if (num.proximate) {
        answer += num.value;
        System.out.println("  adding " + num);
      }
    });

  }

  //////////////////////////////////////////////////////////////////////////////////

  private class GridNumber extends GridItem {

    boolean proximate;
    int end, value;

    public GridNumber(int row, String text, int start, int end) {
      super(row, text, start);
      this.end = end;
      proximate = false;
      value = Integer.parseInt(text);
    }

    public void markIfProximate(GridItem sym) {
      boolean nearby = sym.start >= start - 1 && sym.start <= end;
      System.out.println("  " + this + "IS" + (nearby ? "" : " NOT") + " next to " + sym);
      proximate |= nearby;
    }

  }

  //////////////////////////////////////////////////////////////////////////////////

  private class GridItem {
    int row, start;
    String value;

    public GridItem(int row, String val, int start) {
      this.row = row;
      this.start = start;
      this.value = val;
    }

    @Override
    public String toString() {
      return "[" + row + "," + start + "]=" + value + " ";
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Objects.hash(row, start, value);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      GridItem other = (GridItem) obj;
      return row == other.row && start == other.start && Objects.equals(value, other.value);
    }
  }
}
