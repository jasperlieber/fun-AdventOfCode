package com.goatwalker.aoc21;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Day03Part1 {

  public static void main(String[] args) throws Exception {

    String dfile = "2021_data\\d03p1.txt";

    Path path = Paths.get(dfile);
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

    int numLines = lines.size();
    int numbs = "000001110001".length();
    int[] counts = new int[numbs];

    for (String line : lines) {
      char[] chars = line.toCharArray();
      for (int jj = 0; jj < numbs; jj++)
        if (chars[jj] == '1')
          counts[jj]++;
    }

    StringBuilder gamma = new StringBuilder();
    StringBuilder epsilon = new StringBuilder();
    for (int count : counts) {
      if (count * 2 == numLines)
        throw new Exception("count " + count);
      boolean b = count * 2 > numLines;
      gamma.append(b ? "1" : "0");
      epsilon.append(b ? "0" : "1");
    }

    int gammaRate = Integer.parseInt(gamma.toString(), 2);
    int epsilonRate = Integer.parseInt(epsilon.toString(), 2);

    System.out.println(gammaRate * epsilonRate);
  }

}
