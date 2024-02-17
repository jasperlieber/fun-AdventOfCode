package com.goatwalker.aoc23.day10;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.goatwalker.utils.Pair;

public class Day10Part2 {

  String datafile = "2023_data\\d10p1.txt";

  public static void main(String[] args) throws Exception {
    Day10Part2 game = new Day10Part2();
    game.doit();
  }

  private void doit() throws Exception {
    loadData();
    System.out.printf("start is at [%d %d]\n", startRow, startCol);
    findLoop();
    int answer = countInternals();
    System.out.println("Internal count = " + answer);
  }

  private void loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);
    ArrayList<String> lines = new ArrayList<String>();
    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        processLine(lines, line);
      }
    } finally {
      scanner.close();
    }
    makeMaps(lines);
  }

  private enum Dir {
    NORTH,
    EAST,
    SOUTH,
    WEST;
  };

  private char[][] map; // symbols on on the map
  private boolean[][] loopCells; // true if cell is part of loop

  // size of map
  private int rowCnt = 0;
  private int colCnt;

  // state values used while finding the loop
  private int startRow = -1, startCol = -1;
  private int curRow, curCol;
  private Dir curDir;
  private char curSym;

  // To find internal cells, examine horizontal transects of the map.
  // For each cell that is not on the loop, determine it's IN/OUT state, and count
  // the cells that are IN.
  // When looking at a transect, start in the OUT state.
  // If scanning finds a vertical bar, switch to IN.
  // If scanning finds a turning cell, must determine if the transect crosses the
  // loop, or if it just touches the loop (which does not count as crossing). If
  // it's just a touch, state returns to the lastState.

  private enum State {
    OUT,
    IN,
    NE_RUN, // F
    SE_RUN, // L
    SW_RUN, // 7
    NW_RUN // J
  }

  /**
   * Use logic outlined above to count the number of internal cells to the loop.
   * 
   * @return number of internal cells
   * @throws Exception Can throw if error in map.
   */
  private int countInternals() throws Exception {
    int inCount = 0;
    for (int row = 0; row < rowCnt; row++) {
      State state = State.OUT;
      State lastInOutState = State.OUT;
      for (int col = 0; col < colCnt; col++) {
        char sym = map[row][col];
        boolean inLoop = loopCells[row][col];

//        System.out.printf("find: inCount=%d [%d %d]=%c inLoop=%b lastInOut=%s state=%s\n", inCount,
//            col, row, sym, inLoop, lastInOut, state);

        if (!inLoop) {
          if (state == State.IN) {
            inCount++;
          }
          continue;
        }

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
              map[curRow][curCol], state);
          throw e;
        }
      }
    }
    return inCount;
  }

  /**
   * Read a line of input. Add it to the lines array. Look for the start symbol,
   * and record if seen. Increment row count.
   * 
   * @param lines ArrayList to contain all lines.
   * @param line  Single line of input to add to lines.
   */
  private void processLine(ArrayList<String> lines, String line) {
//    System.out.println(line);
    lines.add(line);
    int tmp;
    if ((tmp = line.indexOf('S')) != -1) {
      startCol = tmp;
      startRow = rowCnt;
      colCnt = line.length();
    }
    rowCnt++;
  }

  /**
   * Given the map and starting spot, do the following: find the two starting
   * directions, and choose one. Then walk in that direction the path returns to
   * the starting spot. Then update the starting spot to the correct symbol (to
   * accommodate the logic to determine internals cells).
   * 
   * @throws Exception Various subroutines can throw an exception, which is just
   *                   passed up by this routine.
   */
  private void findLoop() throws Exception {
    curRow = startRow;
    curCol = startCol;
    int pathLen = 0;

    Pair<Dir, Dir> startDirs = getStartDirs();
    curDir = startDirs.getFirst();

    do {
//      System.out.printf("walk: pathlen=%d [%d %d]=%c, curDir=%s\n", pathLen, curCol, curRow,
//          map[curRow][curCol], curDir);
      pathLen++;
      walk();
    } while (curSym != 'S');

    assert (curRow == startRow);
    assert (curCol == startCol);

    updateStartSymbol(startDirs);
    System.out.println(
        "Finished walk - pathlen=" + pathLen + ".  start symbol updated to " + map[curRow][curCol]);
  }

  /**
   * Given the pair of directions that feed into the starting position, determine
   * the correct symbol for the start.
   * 
   * @param startDirs
   * @throws Exception Can throw, probably only if there's an error in my logic.
   */
  private void updateStartSymbol(Pair<Dir, Dir> startDirs) throws Exception {
    Dir dir1 = startDirs.getFirst();
    Dir dir2 = startDirs.getSecond();

    try {
      // update start symbol
      switch (dir1) {
      case NORTH:
        switch (dir2) {
        case NORTH:
          throw new Exception();
        case EAST:
          map[curRow][curCol] = 'F';
          break;
        case SOUTH:
          map[curRow][curCol] = '|';
          break;
        case WEST:
          map[curRow][curCol] = '7';
          break;
        }
        break;
      case EAST:
        switch (dir2) {
        case NORTH:
          map[curRow][curCol] = 'L';
          break;
        case EAST:
          throw new Exception();
        case SOUTH:
          map[curRow][curCol] = 'F';
          break;
        case WEST:
          map[curRow][curCol] = '-';
          break;
        }
        break;
      case SOUTH:
        switch (dir2) {
        case NORTH:
          map[curRow][curCol] = '|';
          break;
        case EAST:
          map[curRow][curCol] = 'F';
          break;
        case SOUTH:
          throw new Exception();
        case WEST:
          map[curRow][curCol] = '7';
          break;
        }
        break;
      case WEST:
        switch (dir2) {
        case NORTH:
          map[curRow][curCol] = 'J';
          break;
        case EAST:
          map[curRow][curCol] = '-';
          break;
        case SOUTH:
          map[curRow][curCol] = '7';
          break;
        case WEST:
          throw new Exception();
        }
      }
    } catch (Exception e) {
      System.out.println("Bad pair of start dirs: " + startDirs);
      throw e;
    }
  }

  /**
   * Use curDir to update curRow & curCol. Call getNextDir() to update curDir.
   * Also update this cell of loopCells to true.
   * 
   * @throws Exception Can throw if a logic error in getting next direction.
   */
  private void walk() throws Exception {
    loopCells[curRow][curCol] = true;
    switch (curDir) {
    case NORTH:
      curRow--;
      break;
    case EAST:
      curCol++;
      break;
    case SOUTH:
      curRow++;
      break;
    case WEST:
      curCol--;
      break;
    }
    if ((curSym = map[curRow][curCol]) == 'S')
      return;
    getNextDir();
  }

  /**
   * Given the current direction and the new current symbol, determine the next
   * direction.
   * 
   * @throws Exception Can throw if there's an error in the map.
   */
  private void getNextDir() throws Exception {
    try {
      switch (curDir) {
      case NORTH:
        switch (curSym) {
        case '|':
          break;
        case 'F':
          curDir = Dir.EAST;
          break;
        case '7':
          curDir = Dir.WEST;
          break;
        default:
          throw new Exception();
        }
        break;
      case SOUTH:
        switch (curSym) {
        case '|':
          break;
        case 'L':
          curDir = Dir.EAST;
          break;
        case 'J':
          curDir = Dir.WEST;
          break;
        default:
          throw new Exception();
        }
        break;
      case EAST:
        switch (curSym) {
        case '-':
          break;
        case 'J':
          curDir = Dir.NORTH;
          break;
        case '7':
          curDir = Dir.SOUTH;
          break;
        default:
          throw new Exception();
        }
        break;
      case WEST:
        switch (curSym) {
        case '-':
          break;
        case 'L':
          curDir = Dir.NORTH;
          break;
        case 'F':
          curDir = Dir.SOUTH;
          break;
        default:
          throw new Exception();
        }
        break;
      }
    } catch (Exception e) {
      System.out.printf("getNextDir problem: [%d %d]=%c, curDir=%s.  Can't walk that way?\n",
          curCol, curRow, curSym, curDir);
      throw e;
    }
  }

  /**
   * Determine the two directions that feed into the starting position.
   * 
   * @return a pair of directions.
   * @throws Exception Can throw if there's an error in the map.
   */
  private Pair<Dir, Dir> getStartDirs() throws Exception {
    char c;
    Dir d1 = null, d2 = null;

    // try north
    if (startRow > 0) {
      c = map[startRow - 1][startCol];
      if (c == '|' || c == 'F' || c == '7')
        d1 = Dir.NORTH;
    }

    // try south
    if (startRow < rowCnt) {
      c = map[startRow + 1][startCol];
      if (c == '|' || c == 'L' || c == 'J') {
        if (d1 != null)
          d2 = d1;
        d1 = Dir.SOUTH;
      }
    }

    // try east
    if (startCol < colCnt) {
      c = map[startRow][startCol + 1];
      if (c == '-' || c == 'J' || c == '7') {
        if (d1 != null)
          d2 = d1;
        d1 = Dir.EAST;
      }
    }

    // try west
    if (startCol > 0) {
      c = map[startRow][startCol - 1];
      if (c == '-' || c == 'L' || c == 'F') {
        if (d1 != null)
          d2 = d1;
        d1 = Dir.WEST;
      }
    }

    if (d1 == null || d2 == null)
      throw new Exception("couldn't figure start dirs");

    System.out.println("start dirs = " + d1 + " & " + d2);

    return new Pair<Dir, Dir>(d1, d2);
  }

  /**
   * Given a lines in the map, convert to character array. Allocate the boolean
   * array for cells on the loop.
   * 
   * @param lines
   */
  private void makeMaps(ArrayList<String> lines) {
    loopCells = new boolean[rowCnt][];
    map = new char[rowCnt][];
    int row = 0;
    for (String line : lines) {
      loopCells[row] = new boolean[colCnt];
      map[row] = line.toCharArray();
      row++;
    }
  }
}
