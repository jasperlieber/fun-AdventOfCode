package com.goatwalker.aoc23.day21;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

import com.goatwalker.utils.IntPair;

/**
 * Puzzle description: https://adventofcode.com/2023/day/21
 * 
 */
public class Day21Part1 {

  String datafile = "src/com/goatwalker/aoc23/day21/data/test.txt";
//  String datafile = "src/com/goatwalker/aoc23/day21/data/input.txt";

  public static void main(String[] args) throws Exception {
    Day21Part1 game = new Day21Part1();
    game.doit();

  }

  /////////////////////////////////////////////////////////////////////////////////

  private void doit() throws Exception {
    Landscape landscape = loadData();

    System.out.println(landscape.numRows + " " + landscape.start);

    int tileCnt = landscape.walk(64);

  }

  private class Landscape {

    final char[][] pattern;
    final int numCols, numRows;
    final IntPair start;// = new IntPair();

    int[][] walkDist;

    public Landscape(ArrayList<String> lines) {
      numRows = lines.size();
      numCols = lines.get(0).length();
      pattern = new char[numRows][];
      walkDist = new int[numRows][numCols];

      IntPair s = new IntPair();
      int row = 0;
      for (String line : lines) {
        pattern[row] = line.toCharArray();
        int sCol = line.indexOf('S');
        if (sCol != -1) {
          s.x = row;
          s.y = sCol;
        }
        System.out.println(line);
        row++;
      }
      start = s;
    }

    public int walk(int stepCnt) {
      LinkedList<IntPair> fromTiles = new LinkedList<IntPair>();
      fromTiles.add(start);
      int evenTileCnt = 0;
      for (int step = 1; step <= stepCnt; step++) {
        LinkedList<IntPair> nextTiles = new LinkedList<IntPair>();
        while (!fromTiles.isEmpty()) {
          IntPair tile = fromTiles.pop();
          nextTiles.addAll(getAndMarkNeighbors(tile, step));
        }
        if (step % 2 == 0)
          evenTileCnt += nextTiles.size();
        showNums();
        System.out.printf("Step %d: cnt=%d evenCnt=%d\n", step, nextTiles.size(), evenTileCnt);
        fromTiles = nextTiles;
      }
      return 0;
    }

    private void showNums() {
      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          int n = walkDist[row][col];
          System.out.print(n == 0 ? "." : Character.toString('a' + n));
        }
        System.out.println();
      }

    }

    private Collection<IntPair> getAndMarkNeighbors(IntPair tile, int step) {
      HashSet<IntPair> neighbors = new HashSet<IntPair>();
      addCheckedTile(step, neighbors, tile.x - 1, tile.y);
      addCheckedTile(step, neighbors, tile.x + 1, tile.y);
      addCheckedTile(step, neighbors, tile.x, tile.y - 1);
      addCheckedTile(step, neighbors, tile.x, tile.y + 1);
      return neighbors;
    }

    public void addCheckedTile(int step, HashSet<IntPair> neighbors, int row, int col) {
      if (row >= 0 && row < numRows && col >= 0 && col < numCols && pattern[row][col] != '#'
          && walkDist[row][col] == 0) {
        walkDist[row][col] = step;
        neighbors.add(new IntPair(row, col));
      }
    }

  }

  /////////////////////////////////////////////////////////////////////////////////

  private Landscape loadData() throws Exception {
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

    return new Landscape(lines);

  }

}
