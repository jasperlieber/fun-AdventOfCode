package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.aoc16.utils.InstructionDay08;
import com.goatwalker.aoc16.utils.InstructionDay08.InstructionEnum;

public class Day08Part1 {
  Pattern pRect, pRotRow, pRotCol;

  final int COLS = 50;
  final int ROWS = 6;
  public boolean[][] field = new boolean[ROWS][COLS];

  public static void main(String[] args) throws Exception {
    Day08Part1 game = new Day08Part1();
    game.doit();
  }

  private void doit() throws Exception {
    init();
    // CFLELOYFCS
    String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//				+ "day08part1_test.txt";
//			    + "day08part1.txt";
        + "day8input.txt"; // CDNERYVHKP

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line = br.readLine();

      while (line != null) {

        processLine(line);
        // System.out.println("read " + line);

        line = br.readLine();
      }
    }

    int lit = 0;
    for (int xx = 0; xx < ROWS; xx++) {
      for (int yy = 0; yy < COLS; yy++)
        if (field[xx][yy])
          lit++;
    }
    System.out.println("Final count = " + lit);

  }

  private void init() {
    pRect = Pattern.compile("rect (\\d+)x(\\d+)");
    pRotRow = Pattern.compile("rotate row y=(\\d+) by (\\d+)");
    pRotCol = Pattern.compile("rotate column x=(\\d+) by (\\d+)");
    for (int xx = 0; xx < ROWS; xx++)
      for (int yy = 0; yy < COLS; yy++)
        field[xx][yy] = false;

  }

  private void processLine(String line) throws Exception {
    InstructionDay08 instruction = getInstruction(line);

    processInstruction(instruction);

    display(field);
  }

  private void display(boolean[][] field2) {
    for (int xx = 0; xx < ROWS; xx++) {
      for (int yy = 0; yy < COLS; yy++)
        System.out.print(field[xx][yy] ? '#' : '.');
      System.out.println();
    }

  }

  private void processInstruction(InstructionDay08 instruction) throws Exception {
    switch (instruction.instruction) {
    case RECT:
      for (int xx = 0; xx < instruction.val2; xx++)
        for (int yy = 0; yy < instruction.val1; yy++)
          field[xx][yy] = true;
      break;

    case ROTCOL:
      boolean[] newCol = new boolean[ROWS];
      int col = instruction.val1;
      for (int jj = 0; jj < ROWS; jj++) {
        newCol[(jj + instruction.val2) % ROWS] = field[jj][col];
      }
      for (int jj = 0; jj < ROWS; jj++) {
        field[jj][col] = newCol[jj];
      }
      break;

    case ROTROW:
      boolean[] newRow = new boolean[COLS];
      int row = instruction.val1;
      for (int jj = 0; jj < COLS; jj++) {
        newRow[(jj + instruction.val2) % COLS] = field[row][jj];
      }
      for (int jj = 0; jj < COLS; jj++) {
        field[row][jj] = newRow[jj];
      }
      break;

    default:
      throw new Exception("bad case");

    }

  }

  private InstructionDay08 getInstruction(String line) throws Exception {

    Matcher matcher;

    InstructionDay08 instruction = new InstructionDay08();

    if ((matcher = pRect.matcher(line)).find()) {
      if (matcher.groupCount() != 2)
        throw new Exception("rect cnt = " + matcher.groupCount());
      instruction.val1 = Integer.parseInt(matcher.group(1));
      instruction.val2 = Integer.parseInt(matcher.group(2));
      instruction.instruction = InstructionEnum.RECT;
    } else if ((matcher = pRotRow.matcher(line)).find()) {
      if (matcher.groupCount() != 2)
        throw new Exception("pRotRow cnt = " + matcher.groupCount());
      instruction.val1 = Integer.parseInt(matcher.group(1));
      instruction.val2 = Integer.parseInt(matcher.group(2));
      instruction.instruction = InstructionEnum.ROTROW;

    } else if ((matcher = pRotCol.matcher(line)).find()) {
      if (matcher.groupCount() != 2)
        throw new Exception("pRotCol cnt = " + matcher.groupCount());
      instruction.val1 = Integer.parseInt(matcher.group(1));
      instruction.val2 = Integer.parseInt(matcher.group(2));
      instruction.instruction = InstructionEnum.ROTCOL;
    }

    System.out.println(instruction);

    return instruction;

  }
}
