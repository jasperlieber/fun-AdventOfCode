package com.goatwalker.aoc21;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day07Part1 {

//  private static final boolean debug = false;

  public static void main(String[] args) throws Exception {
    Day07Part1 me = new Day07Part1();
    me.run();
  }

  private void run() throws Exception {

    String dfile = "2021_data\\d07test.txt";
//    String dfile = "2021_data\\d07.txt";

    Path path = Paths.get(dfile);
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

    List<Integer> spots = Stream.of(lines.get(0).split(",")).map(Integer::parseInt)
        .collect(Collectors.toList());

//    System.out.println(Arrays.toString(spots.toArray()));

    Collections.sort(spots);
    System.out.println(Arrays.toString(spots.toArray()));
//    System.out.println(spots.size());
    Integer median = spots.get(spots.size() / 2);

    int fuel = 0;
    for (int spot : spots) {
      fuel += Math.abs(median - spot);
    }
    System.out.println(fuel);

  }

}
