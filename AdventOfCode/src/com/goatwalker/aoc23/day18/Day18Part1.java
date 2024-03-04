package com.goatwalker.aoc23.day18;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.aoc23.day18.Day18Part1.D18Diggings.D18Run;
import com.goatwalker.utils.IntPair;
import com.goatwalker.utils.MyCardinalDirection;

/**
 * Puzzle description: https://adventofcode.com/2023/day/18.
 * 
 * For part 1, I spent time trying to be smart about creating lat/long regions,
 * but eventually gave up and just created a big 2D array for the path and then
 * reused logic from Day 10 to count internal spots.
 * 
 */
public class Day18Part1 {

//  String datafile = "src/com/goatwalker/aoc23/day18/data/test.txt";
  String datafile = "src/com/goatwalker/aoc23/day18/data/input.txt";

  public static void main(String[] args) throws Exception {
    Day18Part1 game = new Day18Part1();
    game.doit();

  }

  private void doit() throws Exception {
    D18Diggings d18diggings = loadData();
    D18Part1Map map = new D18Part1Map(d18diggings);
    System.out.println(map.countInternals());

  }

  private D18Diggings loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);

    D18Diggings d18diggings = new D18Diggings();

    final String regex = "^([UDRL]) (\\d+) \\(#(\\w+)\\)$";

    final Pattern pattern = Pattern.compile(regex);

    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        final Matcher matcher = pattern.matcher(line);
        if (!matcher.find())
          throw new Exception("huh? line = " + line);
        d18diggings.add(matcher.group(1), Integer.parseInt(matcher.group(2)));
      }
    } finally {
      scanner.close();
    }

    return d18diggings;
  }

  public class D18Diggings {

    class D18Run {

      public D18Run(MyCardinalDirection dir, int length) {
        this.dir = dir;
        this.length = length;
      }

      MyCardinalDirection dir;
      int length;
    }

    IntPair cursor;
    IntPair min, max, mapSize;
    List<D18Run> diggings = new LinkedList<D18Run>();

    public D18Diggings() {
      cursor = new IntPair(0, 0);
      min = new IntPair(0, 0);
      max = new IntPair(0, 0);
      mapSize = new IntPair(0, 0);
    }

    private MyCardinalDirection getDir(String dir) {
      switch (dir) {
      case "U":
        return MyCardinalDirection.North;
      case "D":
        return MyCardinalDirection.South;
      case "R":
        return MyCardinalDirection.East;
      case "L":
        return MyCardinalDirection.West;
      }
      return null;
    }

    public void add(String dir, int len) {

      D18Run dig = new D18Run(getDir(dir), len);
      diggings.add(dig);
      moveCursor(dig);
    }

    private void moveCursor(D18Run dig) {

      if (dig.dir.isNorth())
        cursor.y += dig.length; // note north is up
      else if (dig.dir.isSouth())
        cursor.y -= dig.length;
      else if (dig.dir.isEast())
        cursor.x += dig.length;
      else if (dig.dir.isWest())
        cursor.x -= dig.length;

      if (cursor.x < min.x)
        min.x = cursor.x;
      if (cursor.x > max.x)
        max.x = cursor.x;
      if (cursor.y < min.y)
        min.y = cursor.y;
      if (cursor.y > max.y)
        max.y = cursor.y;

    }

  }

  class D18Part1Map {
    char[][] map;

    public D18Part1Map(D18Diggings d18diggings) throws Exception {

      char[][] map;

      IntPair mapSize = new IntPair();

      System.out.println(" bounds: " + d18diggings.min + " " + d18diggings.max);
      mapSize.x = d18diggings.max.x - d18diggings.min.x + 1;
      mapSize.y = d18diggings.max.y - d18diggings.min.y + 1;
      int xoff = -d18diggings.min.x;
      int yoff = -d18diggings.min.y;
      map = new char[mapSize.y][mapSize.x];
      for (int xx = 0; xx < mapSize.x; xx++)
        for (int yy = 0; yy < mapSize.y; yy++)
          map[yy][xx] = '.';

      // HACK -- need to calculate start map character
      map[yoff][xoff] = '-';

      MyCardinalDirection lastdir = null;
      int cx = xoff, cy = yoff;
      for (D18Run dig : d18diggings.diggings) {
        if (lastdir != null)
          map[cy][cx] = getTurnSym(lastdir, dig.dir);
        if (dig.dir.isNorth()) {
          for (int jj = 0; jj < dig.length; jj++)
            map[++cy][cx] = '|';
        } else if (dig.dir.isSouth()) {
          for (int jj = 0; jj < dig.length; jj++)
            map[--cy][cx] = '|';
        } else if (dig.dir.isEast()) {
          for (int jj = 0; jj < dig.length; jj++)
            map[cy][++cx] = '-';
        } else if (dig.dir.isWest()) {
          for (int jj = 0; jj < dig.length; jj++)
            map[cy][--cx] = '-';
        }
        lastdir = dig.dir;

      }
      map[yoff][xoff] = getTurnSym(lastdir, d18diggings.diggings.get(0).dir);

      this.map = map;
    }

    public void printMap() {
      for (int rr = map.length; --rr >= 0;)
        System.out.println(map[rr]);

    }

    private char getTurnSym(MyCardinalDirection lastdir, MyCardinalDirection dir) throws Exception {
      switch (lastdir.dir) {
      case MyCardinalDirection.north:
        switch (dir.dir) {
        case MyCardinalDirection.east:
          return 'F';
        case MyCardinalDirection.west:
          return '7';
        default:
          throw new Exception();
        }
      case MyCardinalDirection.east:
        switch (dir.dir) {
        case MyCardinalDirection.north:
          return 'J';
        case MyCardinalDirection.south:
          return '7';
        default:
          throw new Exception();
        }
      case MyCardinalDirection.south:
        switch (dir.dir) {
        case MyCardinalDirection.east:
          return 'L';
        case MyCardinalDirection.west:
          return 'J';
        default:
          throw new Exception();
        }
      case MyCardinalDirection.west:
        switch (dir.dir) {
        case MyCardinalDirection.north:
          return 'L';
        case MyCardinalDirection.south:
          return 'F';
        default:
          throw new Exception();
        }
      default:
        throw new Exception();
      }
    }

    private enum State {
      OUT,
      IN,
      NE_RUN, // F
      SE_RUN, // L
      SW_RUN, // 7
      NW_RUN, // J

    }

    // To find internal cells, examine horizontal transects of the map.
    // For each cell that is not on the loop, determine it's IN/OUT state, and count
    // the cells that are IN.
    // When looking at a transect, start in the OUT state.
    // If scanning finds a vertical bar, switch to IN.
    // If scanning finds a turning cell, must determine if the transect crosses the
    // loop, or if it just touches the loop (which does not count as crossing). If
    // it's just a touch, state returns to the lastState.

    /**
     * Use logic outlined above to count the number of tiles on or inside the path.
     * 
     * @return number of internal cells
     * @throws Exception Can throw if error in map.
     */
    public int countInternals() throws Exception {
      IntPair mapSize = new IntPair(map.length, map[0].length);
      int inCount = 0;
      for (int row = 0; row < mapSize.x; row++) {
        State state = State.OUT;
        State lastInOutState = State.OUT;
        for (int col = 0; col < mapSize.y; col++) {
          char sym = map[row][col];
          boolean symOnPath = sym != '.';

//        System.out.printf("find: inCount=%d [%d %d]=%c symOnPath=%b lastInOut=%s state=%s\n",
//            inCount, col, row, sym, symOnPath, lastInOutState, state);

          if (!symOnPath) {
            if (state == State.IN) {
              inCount++;
            }
            continue;
          }

          inCount++;

          // found a cell on the loop -- adjust state.
          try {
            switch (sym) {
            case '-':
              // ignore horizontal loop segments - they don't change in/out state.
              break;
            case '|':
              // toggle state
              switch (state) {
              case IN:
                state = State.OUT;
                break;
              case OUT:
                state = State.IN;
                break;
              default:
                throw new Exception();
              }
              break;

            case 'L':
              // transect encounters an easterly corner of the loop, coming in from below.
              // store current state, update state to a SE_RUN.
              switch (state) {
              case IN:
              case OUT:
                lastInOutState = state;
                state = State.SE_RUN;
                break;
              default:
                throw new Exception();
              }
              break;

            case 'F':
              // transect encounters an easterly corner of the loop, coming in from below.
              // store current state, update state to a NE_RUN.
              switch (state) {
              case IN:
              case OUT:
                lastInOutState = state;
                state = State.NE_RUN;
                break;
              default:
                throw new Exception();
              }
              break;

            case 'J':
              // transect encounters a westerly corner of the loop, heading north. If state is
              // NE_RUN, this means the transect has crossed the loop. Update the last state
              // to IN or OUT.
              switch (state) {
              case NE_RUN:
                // crossed the loop
                state = lastInOutState == State.IN ? State.OUT : State.IN;
                break;
              case SE_RUN:
                // touch but not cross of the loop
                state = lastInOutState;
                break;
              default:
                throw new Exception();
              }
              break;

            case '7':
              // transect encounters a westerly corner of the loop, heading south. If state is
              // SE_RUN, this means the transect has crossed the loop. Update the last state
              // to IN or OUT.
              switch (state) {
              case SE_RUN:
                // crossed the loop
                state = lastInOutState == State.IN ? State.OUT : State.IN;
                break;
              case NE_RUN:
                // touch but not cross of the loop
                state = lastInOutState;
                break;
              default:
                throw new Exception();
              }
              break;

            default:
              throw new Exception();
            }

          } catch (Exception e) {
            System.out.printf("findInternals problem: pos=[%d %d]=%c state=%s\n", col, row,
                map[row][col], state);
            throw e;
          }
        }
      }
      return inCount;
    }

  }

}
