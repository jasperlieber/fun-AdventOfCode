package com.goatwalker.aoc23;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.goatwalker.utils.IntCoord;

public class Day11Part2 {

  String datafile = "2023_data\\d11p1.txt";

  char[][] map;
  ArrayList<IntCoord> galaxyCoords = new ArrayList<IntCoord>(); // col, row
  private TreeSet<Integer> expandedColumns;
  private TreeSet<Integer> expandedRows;

  private static final long expansionAmount = 999999L;

  long answer;

  public static void main(String[] args) throws FileNotFoundException {
    Day11Part2 game = new Day11Part2();
    game.doit();
  }

  private void doit() throws FileNotFoundException {

    ArrayList<String> lines = loadData();
    makeExpandedMap(lines);
    calculateIntergalacticDistances();
    System.out.println("Sum of shortest distances is " + answer);
  }

  private void calculateIntergalacticDistances() {
    answer = 0;
    int numGals = galaxyCoords.size();
    for (int gal1 = 0; gal1 < numGals - 1; gal1++) {
      for (int gal2 = gal1 + 1; gal2 < numGals; gal2++) {
        long dist = calculateIntergalacticDistance(gal1, gal2);
        answer += dist;
      }
    }
  }

  private long calculateIntergalacticDistance(int gal1, int gal2) {
    Integer x1 = galaxyCoords.get(gal1).x;
    Integer x2 = galaxyCoords.get(gal2).x;
    Integer y1 = galaxyCoords.get(gal1).y;
    Integer y2 = galaxyCoords.get(gal2).y;

    long dist = Math.abs(x1 - x2) + Math.abs(y1 - y2);

    int numExpCols = 0;
    for (int expCol : expandedColumns) {
      if (expCol > Math.min(x1, x2) && expCol < Math.max(x1, x2))
        numExpCols++;
    }
    dist += expansionAmount * numExpCols;

    int numExpRows = 0;
    for (int expRow : expandedRows) {
      if (expRow > Math.min(y1, y2) && expRow < Math.max(y1, y2))
        numExpRows++;
    }
    dist += expansionAmount * numExpRows;

//    System.out.printf("dist from %s to %s is %d, numExpCols=%d numExpRows=%d\n",
//        galaxyCoords.get(gal1), galaxyCoords.get(gal2), dist, numExpCols, numExpRows);
    return dist;
  }

  private ArrayList<String> loadData() throws FileNotFoundException {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);
    ArrayList<String> lines = new ArrayList<String>();
    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        lines.add(line);
      }
    } finally {
      scanner.close();
    }
    return lines;
  }

  /**
   * Given an ArryList of lines in the map, find location of expansion rows & cols
   * 
   * @param lines
   * @return char[][] map
   */
  private void makeExpandedMap(ArrayList<String> lines) {

    TreeSet<Integer> galaxyColumns = new TreeSet<Integer>();
    expandedRows = new TreeSet<Integer>();

    // while collecting lines, collect a set of column locations of galaxies and a
    // set of row numbers with no galaxies
    int rowCnt = 0;
    for (String line : lines) {
      System.out.println(line);
      // always add one row - maybe add another if no galaxy on this line.
      int galaxyCol = line.indexOf('#');

      // in no galaxies, add this line again
      if (galaxyCol == -1) {
        expandedRows.add(rowCnt);
      } else {
        // collect all column locations of galaxies
        while (galaxyCol >= 0) {
          galaxyCoords.add(new IntCoord(galaxyCol, rowCnt));
          galaxyColumns.add(galaxyCol);
          galaxyCol = line.indexOf('#', galaxyCol + 1);
        }
      }
      rowCnt++;
    }

    System.out.println("expanded row = " + expandedRows);

    // Get size of expanded universe.
    int numCols = lines.get(0).length();

    // Get a list of columns that need to be expanded.
    expandedColumns = new TreeSet<Integer>(
        IntStream.range(0, numCols).boxed().collect(Collectors.toSet()));
    expandedColumns.removeAll(galaxyColumns);

    System.out.printf("adding columns %s\n", expandedColumns);

  }

}
