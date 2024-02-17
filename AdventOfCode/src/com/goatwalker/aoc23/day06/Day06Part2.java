package com.goatwalker.aoc23.day06;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day06Part2 {

  String datafile = "2023_data\\d06p1.txt";

  public static void main(String[] args) throws Exception {
    Day06Part2 game = new Day06Part2();
    game.doit();
  }

  ArrayList<Integer> d6Times = new ArrayList<Integer>();
  ArrayList<Integer> d6Dists = new ArrayList<Integer>();

  private void doit() throws FileNotFoundException {
    loadData();

    long answer = 1;

    for (int jj = 0; jj < d6Times.size(); jj++) {
      int tt = d6Times.get(jj);
      int dd = d6Dists.get(jj);
      int sqr = tt * tt - 4 * dd;
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
//      Scanner scanner = new Scanner(line);
    scanner.next();
    while (scanner.hasNextInt())
      d6Times.add(scanner.nextInt());

    scanner.next();
    while (scanner.hasNextInt())
      d6Dists.add(scanner.nextInt());
    scanner.close();

    System.out.println(d6Times);
    System.out.println(d6Dists);
  }
}
