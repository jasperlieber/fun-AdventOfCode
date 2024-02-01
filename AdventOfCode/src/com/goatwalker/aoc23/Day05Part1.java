package com.goatwalker.aoc23;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day05Part1 {

  public static void main(String[] args) throws Exception {
    Day05Part1 game = new Day05Part1();
    game.doit();
  }

  String datafile = "2023_data\\d05p1.txt";

  List<Long> d5seedNums;
  Map<String, D05MapEntry> d5map = new HashMap<String, D05MapEntry>();

  private void doit() throws Exception {
    loadData();

    long answer = Long.MAX_VALUE;

    for (long seedNum : d5seedNums) {
      System.out.print("Processing seed #" + seedNum);

      String mapFrom = "seed";
      long convertedSeedNum = seedNum;

      for (boolean converting = true; converting;) {

        D05MapEntry mapEntry = d5map.get(mapFrom);

        mapFrom = mapEntry.mapTo;
        converting = !mapFrom.equals("location");

        convertedSeedNum = mapEntry.convert(convertedSeedNum);

        System.out.printf(", %s %d", mapEntry.mapTo, convertedSeedNum);

      }
      System.out.println();

      if (convertedSeedNum < answer)
        answer = convertedSeedNum;
    }

    System.out.println("Answer: " + answer);
  }

  private void loadData() throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(datafile));

    try {
      String line = br.readLine();

      final String regex = "seeds: ([\\d ]+)";
      final Pattern pattern = Pattern.compile(regex);
      final Matcher matcher = pattern.matcher(line);

      if (!matcher.find() || matcher.groupCount() != 1)
        throw new Exception("syntax? line = " + line);

      d5seedNums = Arrays.stream(matcher.group(1).split(" +")).map(Long::valueOf)
          .collect(Collectors.toList());

//      System.out.println("seedNums = " + d5seedNums.toString());

      line = br.readLine();

      while (line != null) {
        line = br.readLine();

        final String regex2 = "(\\w+)-to-(\\w+) map:";
        final Pattern pattern2 = Pattern.compile(regex2);
        final Matcher matcher2 = pattern2.matcher(line);

        if (!matcher2.find() || matcher2.groupCount() != 2)
          throw new Exception("syntax? line = " + line);

        String mapFromName = matcher2.group(1);
        String mapToName = matcher2.group(2);
//        System.out.printf("creating entry for %s to %s\n", mapFromName, mapToName);

        D05MapEntry d5value = new D05MapEntry(mapToName);
        d5map.put(mapFromName, d5value);

        for (line = br.readLine(); line != null && !line.equals(""); line = br.readLine()) {

          List<Long> range = Arrays.stream(line.split(" +")).map(Long::valueOf)
              .collect(Collectors.toList());
          if (range.size() != 3)
            throw new Exception("syntax? line = " + line);

          d5value.addRange(range.get(0), range.get(1), range.get(2));
        }

      }
    } finally {
      br.close();
    }
  }

  //////////////////////////////////////////////////////////

  private class D05Range {
    long dest, src, len;

    public D05Range(long dest2, long src2, long len2) {
      dest = dest2;
      src = src2;
      len = len2;
    }

  }

  //////////////////////////////////////////////////////////

  private class D05MapEntry {
    String mapTo;
    ArrayList<D05Range> ranges = new ArrayList<D05Range>();

    public D05MapEntry(String mapTo) {
      this.mapTo = mapTo;
    }

    public long convert(long seedNum) throws Exception {
      for (D05Range range : ranges) {
        if (seedNum >= range.src && seedNum < range.src + range.len)
          return range.dest + seedNum - range.src;
      }
      return seedNum;
    }

    public void addRange(long dest, long src, long len) {
      D05Range range = new D05Range(dest, src, len);
      ranges.add(range);
//      System.out.printf("adding range %d %d %d\n", dest, src, len);
    }

  }

}
