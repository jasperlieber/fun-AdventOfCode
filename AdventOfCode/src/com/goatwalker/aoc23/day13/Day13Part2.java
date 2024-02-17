package com.goatwalker.aoc23.day13;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * See Part 1 for some discussion. Rather than a 2D array of chars, this
 * solution treats each tile as a binary value, and builds integer values for
 * each row & column of the grid. Then looking for reflections is much easier,
 * and there's a cool trick: the bit count of the XOR of two integers is the
 * number of differing tiles between the two values. To look for smudged
 * solutions, it is sufficient to look for a sum of bitcounts equal to 1,
 * indicating there was one smudged cell when comparing two values.
 */
public class Day13Part2 {

  String datafile = "2023_data\\day13.txt";

  public static void main(String[] args) throws Exception {
    Day13Part2 puzzle = new Day13Part2();
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

    int rCol = landscape.getReflectionColumn(false);
    int rRow = landscape.getReflectionRow(false);
    long ans = rRow * 100 + rCol;
    System.out.println(" clean reflection, column = " + rCol + " row = " + rRow + " ans = " + ans);

    rCol = landscape.getReflectionColumn(true);
    rRow = landscape.getReflectionRow(true);
    ans = rRow * 100 + rCol;
    System.out
        .println(" smudged reflection, column = " + rCol + " row = " + rRow + " ans = " + ans);
    System.out.println();
    return ans;

  }

  private class Landscape {

    private long[] rowSums; // binary representations of rows
    private long[] colSums; // binary representations of cols
    private final int numCols, numRows;

    public Landscape(ArrayList<String> lines) {
      System.out.println("123456789");
      numRows = lines.size();
      numCols = lines.get(0).length();
      rowSums = new long[numRows];
      colSums = new long[numCols];
      int jj = 0;
      for (String line : lines) {
        int kk = 0;
        for (char ch : line.toCharArray()) {
          rowSums[jj] = (rowSums[jj] << 1) + (ch == '.' ? 0 : 1);
          colSums[kk] = (colSums[kk] << 1) + (ch == '.' ? 0 : 1);
          kk++;
        }
        System.out.println(line);
        jj++;
      }
    }

    public int getReflectionColumn(boolean smudged) throws Exception {
      return findReflection("cols", colSums, smudged ? 1 : 0);
    }

    public int getReflectionRow(boolean smudged) throws Exception {
      return findReflection("rows", rowSums, smudged ? 1 : 0);
    }

    /**
     * Given an array of longs, look for a reflection within that array. Allow for
     * smudges via a deltaBits parameter, as follows. During comparison of integer
     * values, sum up the number of bits that are different between the values. If
     * the sum is 0, then there was a perfect reflection (i.e. no smudges). If that
     * sum is 1, then there was one tile that was different, which is the goal of
     * part 2 of the puzzle.
     * 
     * 
     * @param string
     * @param sums
     * @param deltaBits
     * @return
     * @throws Exception
     */
    private int findReflection(String string, long[] sums, int deltaBits) throws Exception {
      int numSums = sums.length;

      ArrayList<Integer> reflections = new ArrayList<Integer>();

      for (int jj = 0; jj < numSums - 1; jj++) {
        int ll, rr, numBits = 0;
        for (int kk = 0; (ll = jj - kk) >= 0 && (rr = jj + kk + 1) < numSums
            && numBits <= deltaBits; kk++) {
          int bitCount = Long.bitCount(sums[ll] ^ sums[rr]);
          numBits += bitCount;
//          System.out.printf("   jj=%d kk=%d ll=%d rr=%d bitCount=%d numBits=%d\n", jj, kk, ll, rr,
//              bitCount, numBits);
        }
        if (numBits == deltaBits) {
          reflections.add(jj);
        }
      }

//      System.out.printf("  in %s with dBits=%d, found %d reflections: %s\n", Arrays.toString(sums),
//          deltaBits, reflections.size(), reflections);

      int numReflections = reflections.size();
      if (numReflections > 1)
        throw new Exception();

      return numReflections == 0 ? 0 : (reflections.get(0) + 1);

    }
  }

}
