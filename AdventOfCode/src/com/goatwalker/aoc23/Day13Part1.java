package com.goatwalker.aoc23;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.IntStream;

/**
 * 
 * Puzzle description: https://adventofcode.com/2023/day/13
 * 
 * I had trouble with the part 2 of this, and got some hints from reddit and
 * made a simpler (& functional!) solution for part 2. This solution builds a 2D
 * array of characters. I wrote a function to find reflections in columns. To
 * look for reflections in row, I lamely created a transpose of the array, and
 * look for column reflections in the transpose. My part 2 solution is much
 * smarter.
 */
public class Day13Part1 {

  String datafile = "2023_data\\day13test2.txt";

  public static void main(String[] args) throws Exception {
    Day13Part1 puzzle = new Day13Part1();
    puzzle.doit();
  }

  private void doit() throws Exception {
    long answer = loadData();
    System.out.println("answer = " + answer);
  }

  private long loadData() throws Exception {
    long answer = 0;
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);

    ArrayList<String> lines = new ArrayList<String>();

    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.length() != 0) {
          lines.add(line);
        } else {
          answer += processLines(lines);
          lines = new ArrayList<String>();
        }
      }
    } finally {
      scanner.close();
    }
    answer += processLines(lines);
    return answer;
  }

  private long processLines(ArrayList<String> lines) throws Exception {

    Landscape landscape = new Landscape(lines);

    int reflectionColumn = landscape.getReflectionColumn();
    int reflectionRow = landscape.getReflectionRow();
    System.out.println(" reflecting column = " + reflectionColumn);
    System.out.println(" reflecting row = " + reflectionRow);

    System.out.println();

    if (reflectionColumn != 0 && reflectionRow != 0)
      throw new Exception("2");

    return reflectionRow * 100 + reflectionColumn;
  }

  private class Landscape {

    private char[][] pattern;
    private char[][] transpose;
    private final int numCols, numRows;

    public Landscape(ArrayList<String> lines) {
      numRows = lines.size();
      numCols = lines.get(0).length();
      pattern = new char[numRows][];
      int jj = 0;
      for (String line : lines) {
        pattern[jj] = line.toCharArray();
        System.out.println(line);
        jj++;
      }
      buildTranspose();
    }

    private void buildTranspose() {

      transpose = new char[numCols][numRows];
      for (int jj = 0; jj < numCols; jj++)
        for (int kk = 0; kk < numRows; kk++)
          transpose[jj][kk] = pattern[kk][jj];
    }

    public int getReflectionColumn() throws Exception {
      return getReflectionColumn(pattern, numRows, numCols);
    }

    public int getReflectionRow() throws Exception {
      return getReflectionColumn(transpose, numCols, numRows);
    }

    private int getReflectionColumn(char[][] pattern, int numRows, int numCols) throws Exception {

      TreeSet<Integer> reflectingCols = new TreeSet<Integer>(
          IntStream.range(0, numCols - 1).boxed().toList());

      for (int row = 0; reflectingCols.size() > 0 && row < numRows; row++) {
        for (Iterator<Integer> reflectingColIterator = reflectingCols
            .iterator(); reflectingColIterator.hasNext();) {
          int reflectingCol = reflectingColIterator.next();
          boolean reflection = true;
          int ll, rr;
          for (int col2 = 0; reflection && (ll = reflectingCol - col2) >= 0
              && (rr = reflectingCol + col2 + 1) < numCols; col2++) {
            reflection &= pattern[row][ll] == pattern[row][rr];
          }
          if (!reflection)
            reflectingColIterator.remove();
        }
      }

      if (reflectingCols.size() > 1)
        throw new Exception("found multiple reflections: " + reflectingCols);
      return reflectingCols.size() > 0 ? (reflectingCols.first() + 1) : 0;
    }
  }
}
