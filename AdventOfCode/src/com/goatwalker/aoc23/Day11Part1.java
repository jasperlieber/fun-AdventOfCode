package com.goatwalker.aoc23;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.goatwalker.utils.IntCoord;

public class Day11Part1 {

  String datafile = "2023_data\\d11p1.txt";

//  char[][] map;
  ArrayList<IntCoord> galaxyCoords = new ArrayList<IntCoord>(); // col, row
  int answer;

  public static void main(String[] args) throws Exception {
    Day11Part1 game = new Day11Part1();
    game.doit();
  }

  private void doit() throws Exception {

    ArrayList<String> lines = loadData();
    adjustGalaxyLocations(lines);
    calculateIntergalacticDistances();
    System.out.println("Sum of shortest distances is " + answer);
  }

  private void calculateIntergalacticDistances() {
    answer = 0;
    int numGals = galaxyCoords.size();
    for (int jj = 0; jj < numGals - 1; jj++) {
      for (int kk = jj + 1; kk < numGals; kk++) {
        int dist = Math.abs(galaxyCoords.get(jj).x - galaxyCoords.get(kk).x)
            + Math.abs(galaxyCoords.get(jj).y - galaxyCoords.get(kk).y);
        answer += dist;
      }
    }
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
   * Given an ArryList of lines, look for galaxies, and adjust the coordinates due
   * to expansion
   * 
   * @param lines
   */
  private void adjustGalaxyLocations(ArrayList<String> lines) {

    TreeSet<Integer> galaxyColumns = new TreeSet<Integer>();

    // while scanning lines, collect a set of column locations of galaxies
    // and get an expanded rowCnt (for rows without galaxies).
    int rowCnt = 0;
    for (String line : lines) {
      System.out.println(line);
      int galaxyCol = line.indexOf('#');
      if (galaxyCol == -1) {
        // in no galaxies in this row, increment rowCnt an extra time
        rowCnt++;
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

    // Get a list of columns that need to be expanded.
    // First make a set containing all column numbers
    int numCols = lines.get(0).length();
    TreeSet<Integer> expandedColumns = new TreeSet<Integer>(
        IntStream.range(0, numCols).boxed().collect(Collectors.toSet()));

    // then remove all columns that have galaxies in them. the resulting set is
    // columns without galaxies.
    expandedColumns.removeAll(galaxyColumns);

    System.out.printf("found galaxies in %s so adding columns %s\n", galaxyColumns,
        expandedColumns);

    // update the column value for all the galaxy coordinates
    for (IntCoord coord : galaxyCoords) {
      int curCol = coord.getFirst();
      // get a count (into "plus") of how many columns are inserted before the column
      // of this galaxy.
      int plus = 0;
      for (int expandedCol : expandedColumns) {
        if (expandedCol < curCol)
          plus++;
      }
      // move that galaxy over by the "plus" amount
      coord.x += plus;
//      System.out.println("Expanded galaxy to " + coord + " by adding " + plus);
    }
  }
}
