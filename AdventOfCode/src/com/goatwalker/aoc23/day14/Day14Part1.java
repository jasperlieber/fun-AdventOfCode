package com.goatwalker.aoc23.day14;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * Puzzle description: https://adventofcode.com/2023/day/14
 * 
 * The Platform class holds the position of the rocks, and has methods to roll
 * the rocks north and calculate the loads.
 * 
 * The algorithm to roll the rocks north is slightly inefficient - it could be
 * more efficient to move blocks of round rocks together. But this would make
 * the code a bit more complicated, and since the input data is not gigantic, I
 * felt that this works well enough.
 */
public class Day14Part1 {

  String datafile = "2023_data\\day14.txt";

  public static void main(String[] args) throws Exception {
    Day14Part1 puzzle = new Day14Part1();
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
        if (line.length() == 0)
          break;
        lines.add(line);
      }
    } finally {
      scanner.close();
    }
    answer += processLines(lines);
    return answer;
  }

  private long processLines(ArrayList<String> lines) throws Exception {

    Platform platform = new Platform(lines);

//    System.out.println(platform);

//    System.out.println("tilted:");

    platform.tiltNorth();
    System.out.println(platform);
    long answer = platform.calculateLoad();
    return answer;

  }

  private class Platform {
    private char[][] platform; // rows, cols
    private final int numRows, numCols;

    public Platform(ArrayList<String> lines) {

      numRows = lines.size();
      numCols = lines.get(0).length();
      platform = new char[numRows][];
      int jj = 0;
      for (String line : lines) {
        platform[jj++] = line.toCharArray();
//        System.out.println("  " + jj + " " + line);
      }
    }

    public long calculateLoad() {
      long ans = 0;

      for (int col = 0; col < numCols; col++) {
        for (int row = 0; row < numRows; row++) {
          if (platform[row][col] == 'O') {
            ans += numRows - row;
          }
        }
      }
      return ans;
    }

    public void tiltNorth() {
      for (int col = 0; col < numCols; col++) {
        for (int row = 0; row < numRows; row++) {
          if (platform[row][col] == '.') {
            int seek = row + 1;
            char found = '.';
            for (; seek < numRows && (found = platform[seek][col]) == '.'; seek++) {
              // look for rock
              ;
            }
            if (found == 'O') {
              // found a round rock -- move it
              platform[row][col] = 'O';
              platform[seek][col] = '.';
            } else {
              row = seek;
            }
          }
        }
      }
    }

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();

      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          sb.append(platform[row][col]);
        }
        sb.append("\n");
      }
      return sb.toString();
    }
  }
}
