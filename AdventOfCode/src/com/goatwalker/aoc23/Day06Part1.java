package com.goatwalker.aoc23;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day06Part1 {

  String datafile = "2023_data\\d06p1.txt";

  public static void main(String[] args) throws Exception {
    Day06Part1 game = new Day06Part1();
    game.doit();
  }

  ArrayList<Long> d6Times = new ArrayList<Long>();
  ArrayList<Long> d6Dists = new ArrayList<Long>();

  private void doit() throws Exception {
    loadData();

    long answer = 1;

    for (int jj = 0; jj < d6Times.size(); jj++) {
      long tt = d6Times.get(jj);
      long dd = d6Dists.get(jj);

      if (Math.sqrt(Long.MAX_VALUE) < tt)
        throw new Exception("bounds");
      long sqr = tt * tt - 4 * dd;
      if (sqr < 0)
        System.out.printf("no solutions for %d & %d\n", tt, dd);
      else {
        double sqrRoot = Math.sqrt(sqr);
        int sqrRootInt = (int) Math.round(sqrRoot);
        boolean perfectSqr = sqrRootInt * sqrRootInt == sqr;
        double root1 = (tt - sqrRoot) / 2d;
        double root2 = (tt + sqrRoot) / 2d;

        int min = (int) (Math.ceil(root1));
        int max = (int) (Math.floor(root2));
        if (perfectSqr) {
          min += 1;
          max -= 1;
        }
        System.out.printf("roots for %d & %d are %f & %f, perfect=%b, so min/max are %d & %d \n",
            tt, dd, root1, root2, perfectSqr, min, max);

        answer *= (max - min + 1);
      }
    }
    System.out.println("Answer: " + answer);
  }

  private void loadData() throws FileNotFoundException {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);

    String timeStr = collateNums(scanner);
    String distStr = collateNums(scanner);

    long time = Long.parseLong(timeStr);
    long dist = Long.parseLong(distStr);

    d6Times.add(time);
    d6Dists.add(dist);

    scanner.close();

  }

  private String collateNums(Scanner scanner) {
    Scanner numScanner = new Scanner(scanner.nextLine());
    numScanner.next();
    String num = "";
    while (numScanner.hasNext())
      num += numScanner.next();
    System.out.println(num);
    numScanner.close();
    return num;
  }
}
