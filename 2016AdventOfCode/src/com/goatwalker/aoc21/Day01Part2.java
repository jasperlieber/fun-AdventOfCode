package com.goatwalker.aoc21;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class Day01Part2 {

  public static void main(String[] args) throws IOException, URISyntaxException {

    int increase = 0;
    String dfile = "2021_data\\d01p1.txt";

    Path path = Paths.get(dfile);

    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    List<Integer> ints = new Vector<Integer>();

    ints.addAll(lines.stream().map(Integer::valueOf).collect(Collectors.toList()));

    Integer[] nn = ints.toArray(new Integer[ints.size()]);

    int last = nn[0] + nn[1] + nn[2];
    for (int jj = 1; jj < lines.size() - 2; jj++) {
      int next = nn[jj] + nn[jj + 1] + nn[jj + 2];
      if (next > last)
        increase++;
      last = next;
    }
    System.out.println(increase);
  }
}
