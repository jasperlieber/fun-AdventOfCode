package com.goatwalker.aoc21;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day05Part1 {

//  private static final boolean debug = false;

  public static void main(String[] args) throws Exception {
    Day05Part1 me = new Day05Part1();
    me.run();
  }

  public class Point implements Comparable<Point> {
    int x, y;

//    public Segment(Pair<Integer, Integer> first, Pair<Integer, Integer> second) {
//      super(first, second);
//      // TODO Auto-generated constructor stub
//    }

    public Point(String x1, String y1) {
      x = Integer.parseInt(x1);
      y = Integer.parseInt(y1);
    }

    public Point(int i, int j) {
      x = i;
      y = j;
    }

    public Point(Point p1) {
      x = p1.x;
      y = p1.y;
    }

    @Override
    public String toString() {
      return String.format("%d,%d", x, y);
    }

    @Override
    public int compareTo(Point o) {
//      System.out.format("comparing " + this + " to " + o);
      if (x == o.x)
        return y - o.y;
      else
        return x - o.x;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getEnclosingInstance().hashCode();
      result = prime * result + Objects.hash(x, y);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Point other = (Point) obj;
      if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
        return false;
      return x == other.x && y == other.y;
    }

    private Day05Part1 getEnclosingInstance() {
      return Day05Part1.this;
    }
  }

  private void run() throws Exception {

    String dfile = "2021_data\\d05test2.txt";
//    String dfile = "2021_data\\d05p1.txt";

    Path path = Paths.get(dfile);
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

    HashMap<Point, Integer> overlaps = new HashMap<Point, Integer>();

    Pattern pattern = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");

    for (String line : lines) {
      System.out.println(line);
      Matcher matcher = pattern.matcher(line);
      if (!matcher.find())
        throw new Exception("bad line: " + line);
      Point p1 = new Point(matcher.group(1), matcher.group(2));
      Point p2 = new Point(matcher.group(3), matcher.group(4));
//      System.out.println(p1 + " -> " + p2);
      Point delta = new Point(Integer.signum(p2.x - p1.x), Integer.signum(p2.y - p1.y));
//      if (delta.x != 0 && delta.y != 0)
//        continue;
//        throw new Exception("bad delta with segment " + p1 + " -> " + p2);

      for (Point pp = new Point(p1);; pp.x += delta.x, pp.y += delta.y) {
        int cur = overlaps.getOrDefault(pp, 0);
        overlaps.put(new Point(pp), ++cur);
//        System.out.format("  setting %s to %d\n", pp, cur);
        if (pp.equals(p2))
          break;
      }
    }

    int cnt = 0;
    for (Map.Entry<Point, Integer> entry : overlaps.entrySet()) {
//      System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
      if (entry.getValue() > 1)
        cnt++;
    }

    System.out.println(cnt);

  }
//
//  @SuppressWarnings("unused")
//  private String dump(Entry[][] board) {
//    StringBuffer out = new StringBuffer();
//    for (int row = 0; row < 5; row++) {
//      for (int col = 0; col < 5; col++) {
//        out.append(String.format("%2d%s ", board[row][col].getKey(),
//            board[row][col].getValue() ? "t" : "f"));
//      }
//      out.append("\n");
//    }
//    return out.toString();
//  }

  @Override
  public String toString() {
    return "Day05Part1 []";
  }

}
