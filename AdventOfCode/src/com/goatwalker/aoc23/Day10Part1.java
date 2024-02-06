package com.goatwalker.aoc23;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Day10Part1 {

  String datafile = "2023_data\\d10p1.txt";

  long answer;

  public static void main(String[] args) throws Exception {
    Day10Part1 game = new Day10Part1();
    game.doit();
  }

  private void doit() throws Exception {
    loadData();
    System.out.printf("start is at [%d %d]\n", startRow, startCol);
    findLoop();
    System.out.println("Answer = " + answer);
  }

  private void loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);
    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        processLine(line);
      }
    } finally {
      scanner.close();
    }
  }

  enum Dir {
    NORTH,
    EAST,
    SOUTH,
    WEST,
  };

  ArrayList<String> lines = new ArrayList<String>();
  int startRow = -1, startCol = -1;
  int rowCnt = 0;
  int colCnt;
  int curRow, curCol;
  Dir curDir;
  char[][] map;
  char curSym;

  private void processLine(String line) throws Exception {
    System.out.println(line);
    lines.add(line);
    int tmp;
    if ((tmp = line.indexOf('S')) != -1) {
      startCol = tmp;
      startRow = rowCnt;
      colCnt = line.length();
    }
    rowCnt++;
  }

  private void findLoop() throws Exception {
    map = makeMap();
    curRow = startRow;
    curCol = startCol;
    int pathLen = 0;

    curDir = getStartDir();

    System.out.println("Start dir = " + curDir);

    do {
      System.out.printf("walk: pathlen=%d pos=%d %d, ch=%c, curDir=%s\n", pathLen, curCol, curRow,
          map[curRow][curCol], curDir);
      pathLen++;
      walk();
    } while (curSym != 'S');
    answer = pathLen / 2;

  }

  private void walk() throws Exception {
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
    try {
      getNextDir();
    } catch (Exception e) {
      System.out.printf("getNextDir problem: pos=%d %d, ch=%c, curDir=%s\n", curCol, curRow,
          map[curRow][curCol], curDir);
      throw e;
    }
  }

  private void getNextDir() throws Exception {
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
  }

  private Dir getStartDir() throws Exception {
    char c;
    // try north
    c = map[curRow - 1][curCol];
    if (c == '|' || c == 'F' || c == '7')
      return Dir.NORTH;
    // try south
    c = map[curRow + 1][curCol];
    if (c == '|' || c == 'L' || c == 'J')
      return Dir.SOUTH;
    // try east
    c = map[curRow][curCol + 1];
    if (c == '-' || c == 'J' || c == '7')
      return Dir.EAST;
    // try west
    c = map[curRow - 1][curCol];
    if (c == '-' || c == 'L' || c == 'F')
      return Dir.WEST;
    throw new Exception("couldn't figure start dir");

  }

  private char[][] makeMap() {
    char[][] map = new char[rowCnt][];
    int row = 0;
    for (String line : lines) {
      map[row++] = line.toCharArray();
    }
    return map;
  }
}
