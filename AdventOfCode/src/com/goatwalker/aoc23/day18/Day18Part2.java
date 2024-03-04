package com.goatwalker.aoc23.day18;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.utils.LongPair;
import com.goatwalker.utils.MyCardinalDirection;

/**
 * Puzzle description: https://adventofcode.com/2023/day/18.
 * 
 * For part 1, I spent time trying to be smart about creating lat/long regions,
 * but eventually gave up and just created a big 2D array for the path and then
 * reused logic from Day 10 to count internal spots.
 * 
 * Of course, a 2D array won't work for Day 2, lol. Instead, I learned about
 * https://en.wikipedia.org/wiki/Shoelace_formula. But I still had
 * incorrectness, and resorted to
 * https://www.ericburden.work/blog/2023/12/18/advent-of-code-day-18/ to get the
 * final trick: shoelace gives the internal area, but need half plus one of the
 * perimeter to be included in order to get the final solution. The reason for
 * this: The shoelace algorithm counts half of the outside wall trench tiles,
 * but since the lava goes all the way to the outer walls of the trench, half
 * the perimeter needs to be added in. Additionally, there are 4 corner tiles
 * where an additional quarter tile quarter tile needs to be included, thus the
 * +1. (Plus & minus quarters for internal corners all balance out.)
 * 
 */
public class Day18Part2 {

//  String datafile = "src/com/goatwalker/aoc23/day18/data/test.txt";
  String datafile = "src/com/goatwalker/aoc23/day18/data/input.txt";

  public static void main(String[] args) throws Exception {
    Day18Part2 game = new Day18Part2();
    game.doit();

  }

  private void doit() throws Exception {
    D18Part2Map map = loadData();
    System.out.println("area with perimeter: " + (map.calcInnerArea() + map.perimeterLen / 2 + 1));

  }

  private D18Part2Map loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);

    final String regex = "^([UDRL]) (\\d+) \\(#(\\w+)\\)$";

    final Pattern pattern = Pattern.compile(regex);

    LongPair cursor = new LongPair();
    D18Part2Map map = new D18Part2Map();

    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        final Matcher matcher = pattern.matcher(line);
        if (!matcher.find())
          throw new Exception("huh? line = " + line);

        String color = matcher.group(3);
        long length = Long.parseLong(color.substring(0, 5), 16);
        String dirChar = color.substring(5);

        MyCardinalDirection dir = getDir(dirChar);

        map.perimeterLen += length;
        map.add(new LongPair(cursor));

        if (dir.isNorth()) {
          cursor.y += length; // note north is up
        } else if (dir.isSouth()) {
          cursor.y -= length;
        } else if (dir.isEast()) {
          cursor.x += length;
        } else if (dir.isWest()) {
          cursor.x -= length;
        }
      }
    } finally {
      scanner.close();
    }

    map.add(cursor);

    return map;
  }

  /**
   * Map direction values to cardinal directions
   * 
   * @param dir string 0 - 3
   * @return direction
   */
  private MyCardinalDirection getDir(String dir) {
    switch (dir) {
    case "0":
      return MyCardinalDirection.East;
    case "1":
      return MyCardinalDirection.South;
    case "2":
      return MyCardinalDirection.West;
    case "3":
      return MyCardinalDirection.North;
    }
    return null;
  }

  class D18Part2Map extends ArrayList<LongPair> {
    private static final long serialVersionUID = 1L;

    long perimeterLen = 0;

    /**
     * Use shoelace algorithm to get internal area
     * 
     * From internet:
     * 
     * Let 'vertices' be an array of N pairs (x,y), indexed from 0
     * 
     * Let 'area' = 0.0
     * 
     * for i = 0 to N-1, do
     * 
     * Let j = (i+1) mod N
     * 
     * Let area = area + vertices[i].x * vertices[j].y
     * 
     * Let area = area - vertices[i].y * vertices[j].x
     * 
     * end for
     * 
     * Return 'area'
     * 
     * If the vertices of your polygon are specified in counter-clockwise order
     * (i.e. by the right-hand rule), then the area will be positive. Otherwise, the
     * area will be negative, assuming the polygon has non-zero area to begin with.
     * 
     * @return number of internal cells
     */
    public long calcInnerArea() throws Exception {
      long area = 0;
      // first coordinate is 0,0 -- can remove it or not, since doesn't impact area
      // sum.
      // To avoid the the expensive remove, I'm initializing last to known value
      // (0,0).
      LongPair last = new LongPair(0L, 0L);// this.remove(0);
      for (LongPair next : this) {
//        System.out.println(next);
        area += last.x * next.y - last.y * next.x;
        last = next;
      }
      System.out.println("signed double area = " + area);
      area = Math.abs(area) / 2;
      System.out.println("area = " + area);
      return area;
    }
  }
}
