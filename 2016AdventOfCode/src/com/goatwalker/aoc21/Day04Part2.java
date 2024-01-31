package com.goatwalker.aoc21;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Day04Part2 {

//  private static final boolean debug = false;

  public static void main(String[] args) throws Exception {
    Day04Part2 me = new Day04Part2();
    me.run();
  }

  @SuppressWarnings("serial")
  public class Entry extends SimpleEntry<Integer, Boolean> {

    public Entry(int key, Boolean value) {
      super(key, value);
      // TODO Auto-generated constructor stub
    }
  }

  private int bingoScore = 0;
  private Set<Entry[][]> bingoedBoards = null;

  private void run() throws Exception {

//    String dfile = "2021_data\\d04test.txt";
    String dfile = "2021_data\\d04p1.txt";

    Path path = Paths.get(dfile);
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

//    int[] drawInts = Arrays.stream(lines.get(0).split(",")).mapToInt(Integer::parseInt)
//        .toArray();

    List<Entry[][]> boards = new ArrayList<Entry[][]>();
    for (int boardRow = 2; boardRow < lines.size(); boardRow += 6) {
      Entry[][] board = new Entry[5][5];
      for (int row = 0; row < 5; row++) {
        String boardRowVals = lines.get(boardRow + row);
        for (int col = 0; col < 5; col++) {
          board[row][col] = new Entry(
              Integer.parseInt(boardRowVals.substring(col * 3, col * 3 + 2).strip()),
              false);
        }
      }
//      System.out.println(dump(board));
      boards.add(board);
    }

    Stream<String> drawStream = Arrays.stream(lines.get(0).split(","));
    
    bingoedBoards = new HashSet<Entry[][]>();

    drawStream.filter(draw -> lookForLastBingo(draw, boards)).findFirst();
    
    System.out.println(bingoScore);

  }

  private boolean lookForLastBingo(String drawstring, List<Entry[][]> boards) {
    int draw = Integer.parseInt(drawstring);
    
//    System.out.println("Drawing " + draw);
    
    for (Entry[][] board : boards) {
      if (bingoedBoards.contains(board)) continue;
      
      boolean[] bingoRowsCols = {true, true, true, true, true, true, true, true, true, true};

      for (int row = 0; row < 5; row++) {
        for (int col = 0; col < 5; col++) {
          Entry e = board[row][col];
          if (e.getKey() == draw)
            e.setValue(true);
          bingoRowsCols[row] &= e.getValue();
          bingoRowsCols[col + 5] &= e.getValue();
        }
      }
      
//      System.out.println(dump(board));
      
      for (boolean foundBingo : bingoRowsCols) {
        if (foundBingo) {
          bingoedBoards.add(board);
          break;
        }
      }
      
      if (bingoedBoards.size() == boards.size()) {
        int sumUnmarked = 0;
        for (int row = 0; row < 5; row++) {
          for (int col = 0; col < 5; col++) {
            Entry e = board[row][col];
            if (!e.getValue())
              sumUnmarked +=e.getKey();
          }
        }
        bingoScore = sumUnmarked * draw;
        return true;
      }

    }
    return false;
  }

  @SuppressWarnings("unused")
  private String dump(Entry[][] board) {
    StringBuffer out = new StringBuffer();
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        out.append(String.format("%2d%s ", board[row][col].getKey(),
            board[row][col].getValue() ? "t" : "f"));
      }
      out.append("\n");
    }
    return out.toString();
  }

}
