package com.goatwalker.aoc23.day14;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Puzzle description: https://adventofcode.com/2023/day/14
 * 
 * The Platform class got some additions for this second part of the puzzle. In
 * particular, I wrote methods to tilt west, south and east. Also added a copy
 * constructor, and an equals() method to compare two Platforms.
 * 
 * To find the state of the platform after 1000000000 cycles, I repeatedly
 * cycled the platform until there was a repeat. Using the offset of the
 * repetition, and the length of the repetition cycle, the state after
 * 1000000000 can be determined using some modulo math.
 */
public class Day14Part2 {

  String datafile = "2023_data\\day14.txt";

  public static void main(String[] args) throws Exception {
    Day14Part2 puzzle = new Day14Part2();
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

    try {
      Platform platform = scanOnePlatform(scanner);

//      Platform test1 = scanOnePlatform(scanner);
//      Platform test2 = scanOnePlatform(scanner);
//      Platform test3 = scanOnePlatform(scanner);
//      platform.cycle();
//      System.out.println(test1.equals(platform));
//      platform.cycle();
//      System.out.println(test2.equals(platform));
//      platform.cycle();
//      System.out.println(test3.equals(platform));

      ArrayList<Platform> cycledPlatforms = new ArrayList<Platform>();

      boolean foundCycle = false;
      int cycleIndex = 0;

      while (!foundCycle) {
        Platform old = new Platform(platform);
        cycledPlatforms.add(old);

        platform.cycle();

        for (cycleIndex = 0; cycleIndex < cycledPlatforms.size(); cycleIndex++) {
          if (foundCycle = cycledPlatforms.get(cycleIndex).equals(platform)) {
            System.out.println("on cycle #" + cycledPlatforms.size()
                + ", found repeat at cycleIndex = " + cycleIndex);
            break;
          }
        }
      }

      int cycleLen = cycledPlatforms.size() - cycleIndex;
//      platform.cycle();
//      System.out.println("verifying cycleLen=" + (cycleLen + 1) + ": " + check.equals(platform));

      // maxint ------- 2147483647 (inputCycles can be an int)
      int inputCycles = 1000000000;
      int equivCycle = (inputCycles - cycleIndex - 1) % cycleLen + cycleIndex + 1;
      System.out.printf("cycleIndex=%d cycleLen=%d equivCycle=%d\n", cycleIndex, cycleLen,
          equivCycle);

      answer = cycledPlatforms.get(equivCycle).calculateLoad();

    } finally {
      scanner.close();
    }

    return answer;
  }

  private Platform scanOnePlatform(Scanner scanner) {
    ArrayList<String> lines = new ArrayList<String>();
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.length() == 0)
        break;
      lines.add(line);
    }

    Platform platform = new Platform(lines);
    return platform;
  }

  private class Platform {
    private char[][] platform; // rows, cols
    private final int numRows, numCols;

    /**
     * Construct a Platform based on an array of input lines.
     * 
     * @param lines
     */
    public Platform(ArrayList<String> lines) {
      numRows = lines.size();
      numCols = lines.get(0).length();
      platform = new char[numRows][];

      int jj = 0;
      for (String line : lines) {
        platform[jj++] = line.toCharArray();
      }
    }

    /**
     * A copy construction. Note the nifty way to copy the char[][] array.
     * 
     * @param p
     */
    public Platform(Platform p) {
      numRows = p.numRows;
      numCols = p.numCols;
      platform = Arrays.stream(p.platform).map(char[]::clone).toArray(char[][]::new);
    }

    /**
     * Cycle the platform nn times
     * 
     * @param nn
     */
    public void cycle(int nn) {
      for (int jj = 0; jj < nn; jj++)
        cycle();
    }

    /**
     * A "cycle" is tilting all 4 directions in NWSE order.
     */
    public void cycle() {
      tiltNorth();
      tiltWest();
      tiltSouth();
      tiltEast();
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

    public void tiltSouth() {
      for (int col = 0; col < numCols; col++) {
        for (int row = numRows - 1; row >= 0; row--) {
          if (platform[row][col] == '.') {
            int seek = row - 1;
            char found = '.';
            for (; seek >= 0 && (found = platform[seek][col]) == '.'; seek--) {
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

    public void tiltEast() {
      for (int row = 0; row < numRows; row++) {
        for (int col = numCols - 1; col >= 0; col--) {
          if (platform[row][col] == '.') {
            int seek = col - 1;
            char found = '.';
            for (; seek >= 0 && (found = platform[row][seek]) == '.'; seek--) {
              // look for rock
              ;
            }
            if (found == 'O') {
              // found a round rock -- move it
              platform[row][col] = 'O';
              platform[row][seek] = '.';
            } else {
              col = seek;
            }
          }
        }
      }
    }

    public void tiltWest() {
      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          if (platform[row][col] == '.') {
            int seek = col + 1;
            char found = '.';
            for (; seek < numCols && (found = platform[row][seek]) == '.'; seek++) {
              // look for rock
              ;
            }
            if (found == 'O') {
              // found a round rock -- move it
              platform[row][col] = 'O';
              platform[row][seek] = '.';
            } else {
              col = seek;
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

    public boolean equals(Platform o) {
      if (o.numCols != numCols || o.numRows != numRows)
        return false;

      for (int col = 0; col < numCols; col++) {
        for (int row = 0; row < numRows; row++) {
          if (platform[row][col] != o.platform[row][col]) {
//            System.out.println("diff at " + row + " " + col);
            return false;
          }
        }
      }
      return true;
    }

  }

}
