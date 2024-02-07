package com.goatwalker.aoc23;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12Part1 {

//  String datafile = "2023_data\\day12p0.txt";
  String datafile = "2023_data\\day12.txt";
//  String datafile = "2023_data\\day12test1.txt";

  long answer = 0;

  public static void main(String[] args) throws FileNotFoundException {
    Day12Part1 game = new Day12Part1();
    game.doit();
  }

  private void doit() throws FileNotFoundException {
    loadData();
    System.out.println("answer = " + answer);
  }

  private void loadData() throws FileNotFoundException {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);
    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        answer += processLine(line);
      }
    } finally {
      scanner.close();
    }
  }

  private int processLine(String line) {
//    System.out.println(line);
    String[] reports = line.split(" ");
    System.out.printf("%s %s\n", reports[0], reports[1]);

    String badSpringReport = reports[0];
    int space = badSpringReport.length();

    List<Integer> intList = Stream.of(reports[1].split(",")).map(Integer::parseInt)
        .collect(Collectors.toList());
    ArrayList<Integer> springLengths = new ArrayList<Integer>(intList);

    depth = 0;
    ArrayList<ArrayList<Integer>> arrangements = getArangements(springLengths, space, 0);

//    System.out.println(" springLengths: " + springLengths);
//    System.out.println("  arrangements: " + arrangements);

    int fitCnt = 0;
    for (ArrayList<Integer> arrangement : arrangements) {
//      System.out.println(" checking " + arrangement);
      if (permFitsReport(arrangement, springLengths, badSpringReport)) {
        fitCnt++;
      }
//      System.out.println();
    }

    System.out.printf("fitCnt = %d\n\n", fitCnt);

    return fitCnt;
  }

  private boolean permFitsReport(ArrayList<Integer> arrangement, ArrayList<Integer> springLengths,
      String badSpringReport) {

    boolean fit = true;

    int nextPos = arrangement.get(0);

    if (nextPos > 0) {
      // verify preceeding dots
      int dotPos = nextPos;
      String substr = badSpringReport.substring(0, dotPos);
      fit &= substr.matches("[\\?\\.]{" + dotPos + "}");

//      System.out.printf("     dots %b - '%s' from report[%d,%d], dotCnt=%d\n", fit, substr, dotPos,
//          nextPos, dotPos);

      if (!fit)
        return false;
    }

    for (int jj = 0; jj < arrangement.size(); jj++) {
      int springLength = springLengths.get(jj);
      int position = arrangement.get(jj);

      String substr = badSpringReport.substring(position, position + springLength);
      fit &= substr.matches("[\\?#]{" + springLength + "}");

//      System.out.printf("  springs %b - '%s' from report[%d,%d]\n", fit, substr, position,
//          position + springLength);

      if (!fit)
        return false;

      // verify succeeding dots
      nextPos = (jj != arrangement.size() - 1) ? arrangement.get(jj + 1) : badSpringReport.length();
      int dotPos = position + springLength;
      substr = badSpringReport.substring(dotPos, nextPos);
      int dotCnt = nextPos - position - springLength;
      assert (dotCnt > 0);
      fit &= substr.matches("[\\?\\.]{" + dotCnt + "}");

//      System.out.printf("     dots %b - '%s' from report[%d,%d], dotCnt=%d\n", fit, substr, dotPos,
//          nextPos, dotCnt);

      if (!fit)
        return false;
    }
    return true;
  }

  int depth;

  private ArrayList<ArrayList<Integer>> getArangements(List<Integer> lengths, int space,
      int offset) {

    ArrayList<ArrayList<Integer>> ans = new ArrayList<ArrayList<Integer>>();
    ++depth;
    int lenCnt = lengths.size();
//    System.out.printf("  in %d: lengths=%s space=%d offset=%d\n", depth, lengths, space, offset);

    int len0 = lengths.get(0);

    if (lenCnt == 1) {
      if (len0 <= space) {
        for (int jj = offset; jj <= space + offset - len0; jj++) {

          ArrayList<Integer> an = new ArrayList<Integer>();
          an.add(jj);
          ans.add(an);
        }
      }
    } else {

      List<Integer> subList = lengths.subList(1, lenCnt);

      for (int jj = offset;; jj++) {

        int newSpace = space - len0 - 1 - jj + offset;
        int newOffset = len0 + 1 + jj;

        ArrayList<ArrayList<Integer>> recurses = getArangements(subList, newSpace, newOffset);
        if (recurses.size() == 0)
          break;

        for (ArrayList<Integer> recurse : recurses) {

          ArrayList<Integer> an = new ArrayList<Integer>();
          an.add(jj);
          an.addAll(recurse);
          ans.add(an);
        }
      }
    }
    --depth;
//    System.out.printf(" out %d: ans=%s\n", depth, ans);

    return ans;
  }

}
