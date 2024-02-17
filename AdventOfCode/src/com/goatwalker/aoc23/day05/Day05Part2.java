package com.goatwalker.aoc23.day05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day05Part2 {

  public static void main(String[] args) throws Exception {
    Day05Part2 game = new Day05Part2();
    game.doit();
  }

  String datafile = "2023_data\\d05p1.txt";

  TreeSet<D5Slice> d5Slices = new TreeSet<D5Slice>();
  Map<String, D5Category> d5Categories = new HashMap<String, D5Category>();

  private void doit() throws Exception {
    loadData();
    processSlices();
    System.out.println("Answer: " + d5Slices.first().start);
  }

  private void processSlices() throws Exception {

    String mapFrom = "seed";

    for (boolean converting = true; converting;) {

      System.out.print("converting from " + mapFrom);
      D5Category category = d5Categories.get(mapFrom);
      mapFrom = category.mapTo;
      System.out.println(" to " + mapFrom);
      converting = !mapFrom.equals("location");
      d5Slices = mapSlices(d5Slices, category.dests);

      collapseSlices(d5Slices);
      System.out.println("  resulting slices = " + d5Slices);
    }
  }

  private void collapseSlices(TreeSet<D5Slice> slices) {
    D5Slice curSlice = null;
    for (Iterator<D5Slice> iterator = slices.iterator(); iterator.hasNext();) {
      D5Slice nextSlice = iterator.next();
      if (curSlice == null)
        curSlice = nextSlice;
      else if (curSlice.end + 1 == nextSlice.start) {
        curSlice.end = nextSlice.end;
        iterator.remove();
      } else
        curSlice = nextSlice;
    }
  }

  private TreeSet<D5Slice> mapSlices(TreeSet<D5Slice> slices, TreeSet<D5Dest> dests)
      throws Exception {

    System.out.println("Mapping " + slices + " across " + dests);

    TreeSet<D5Slice> allSlices = new TreeSet<D5Slice>();

    TreeSet<Long> pts = new TreeSet<Long>();

    for (D5Slice curSlice : slices) {
      pts.add(curSlice.start);
      pts.add(curSlice.end + 1);
    }

    for (D5Dest dest : dests) {
      D5Slice curDest = dest.src;
      pts.add(curDest.start);
      pts.add(curDest.end + 1);
    }

    System.out.println("  pts = " + pts);

    long start = -1;
    for (long pt : pts) {
      if (start == -1)
        start = pt;
      else {
        allSlices.add(new D5Slice(start, pt - start));
        start = pt;
      }
    }

    System.out.println("  allSlices = " + allSlices);

    TreeSet<D5Slice> mappedSlices = new TreeSet<D5Slice>();

    for (D5Slice slice : allSlices) {
      D5Slice mappedSlice = doPotentialMapping(slice, slices, dests);
      if (mappedSlice != null) {
        System.out.println("    adding " + mappedSlice);
        mappedSlices.add(mappedSlice);
      }
    }

    return mappedSlices;
  }

  private D5Slice doPotentialMapping(D5Slice slice, TreeSet<D5Slice> srcSlices,
      TreeSet<D5Dest> dests) {
    boolean isSrcSlice = false;
    boolean isMapSlice = false;

    for (D5Slice srcSlice : srcSlices) {
      isSrcSlice = srcSlice.start <= slice.start && slice.end <= srcSlice.end;
      if (isSrcSlice)
        break;
    }

    long delta = 0;
    for (D5Dest dest : dests) {
      isMapSlice = dest.src.start <= slice.start && slice.end <= dest.src.end;
      delta = dest.dest - dest.src.start;
      if (isMapSlice)
        break;
    }

    if (isSrcSlice && isMapSlice) {
      System.out.print("    mapping " + slice);
      slice.start += delta;
      slice.end += delta;
      System.out.println(" to " + slice);
    }

    return isSrcSlice ? slice : null;

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

      List<Long> d5seedNums = Arrays.stream(matcher.group(1).split(" +")).map(Long::valueOf)
          .collect(Collectors.toList());

      for (int jj = 0; jj < d5seedNums.size(); jj += 2) {
        D5Slice slice = new D5Slice(d5seedNums.get(jj), d5seedNums.get(jj + 1));
        d5Slices.add(slice);
      }

      System.out.println("Initial slices: " + d5Slices);

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

        D5Category d5value = new D5Category(mapToName);
        d5Categories.put(mapFromName, d5value);

        for (line = br.readLine(); line != null && !line.equals(""); line = br.readLine()) {

          List<Long> range = Arrays.stream(line.split(" +")).map(Long::valueOf)
              .collect(Collectors.toList());
          if (range.size() != 3)
            throw new Exception("syntax? line = " + line);

          d5value.addDest(range.get(0), range.get(1), range.get(2));
        }

      }
    } finally {
      br.close();
    }
  }

  //////////////////////////////////////////////////////////

  private class D5Slice implements Comparable<D5Slice> {
    long start, end;

    public D5Slice(long start, long len) throws Exception {
      this.start = start;
      this.end = start + len - 1;
      if (this.end < this.start)
        throw new Exception("negative length error");
    }

    @Override
    public String toString() {
      return "[" + start + ":" + end + "]";
    }

    @Override
    public int compareTo(D5Slice o) {
      return start < o.start ? -1 : (start == o.start ? 0 : 1);
    }

  }

  //////////////////////////////////////////////////////////

  private class D5Dest implements Comparable<D5Dest> {
    long dest;
    D5Slice src;

    public D5Dest(long dest, long src, long len) throws Exception {
      this.dest = dest;
      this.src = new D5Slice(src, len);
    }

    @Override
    public int compareTo(D5Dest o) {
      return src.start < o.src.start ? -1 : (src.start == o.src.start ? 0 : 1);
    }

    @Override
    public String toString() {
      return src + "->" + dest;
    }

  }

  //////////////////////////////////////////////////////////

  private class D5Category {
    String mapTo;
    TreeSet<D5Dest> dests = new TreeSet<D5Dest>();

    public D5Category(String mapTo) {
      this.mapTo = mapTo;
    }

    public void addDest(long dest, long src, long len) throws Exception {
      D5Dest dest2 = new D5Dest(dest, src, len);
      dests.add(dest2);
//      System.out.printf("adding range %d %d %d\n", dest, src, len);
    }
  }
}
