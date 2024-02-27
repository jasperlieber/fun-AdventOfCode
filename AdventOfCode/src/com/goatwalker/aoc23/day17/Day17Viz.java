package com.goatwalker.aoc23.day17;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

import com.goatwalker.aoc23.day17.Day17BothParts.Algorithm;
import com.goatwalker.aoc23.day17.Day17BothParts.D17Crucible;
import com.goatwalker.aoc23.day17.Day17BothParts.D17Grid;
import com.goatwalker.utils.IntPair;
import com.goatwalker.utils.MyCardinalDirection;
import com.goatwalker.utils.Pair;

import processing.core.PApplet;

/**
 * Make use of processing.org to generate an animated visualization of AOC 23
 * Day 17.
 *
 */
public class Day17Viz extends PApplet {

  private static final String datafile = "src/com/goatwalker/aoc23/day17/data/input.txt";
//  private static final String datafile = "src/com/goatwalker/aoc23/day17/data/test_input3.txt";

  // window size
  private int winSize = 790;

  // how much time to allocate before displaying results & writing a frame to disk
  long computeTime = 460;

  // delay milliseconds for first part of animation, to allow people to see the
  // action slowly
  private int longLookMillis = 0;
  // day time for rest of animation, to proceed quickly
  private int shortLookMillis = 0;

  // location for frame saves
  private String saveFrameFilename = "c:/temp/path/f" + winSize + "-#####.jpg";

  // border around grid
  private static final int border = 30;

  // global junk
  HashMap<Algorithm, Algo> d17algs;
  Day17BothParts d17;
  D17Grid d17grid;
  IntPair goalLoc;
  int canvasX, canvasY, tileWd, tileHt;
  private int last_y = -1;
  private int last_x = 0;

  ////////////////////////////////////////////////////////////////////////////////////

  public class Algo {
    public Algo(int i, int j, int k, int l, int m, int n) {
      lineRed = i;
      lineGreen = j;
      lineBlue = k;
      endRed = l;
      endGreen = m;
      endBlue = n;
    }

    D17Crucible startCrucible = null;
    D17Crucible finalCrucible = null;
    D17Crucible crucible;

    PriorityQueue<Pair<Integer, D17Crucible>> priorityQueue;
    HashMap<D17Crucible, Pair<Integer, D17Crucible>> minHeatLossMap;
    public int lineBlue;
    public int lineGreen;
    public int lineRed;
    public int endBlue;
    public int endGreen;
    public int endRed;
  }

  ////////////////////////////////////////////////////////////////////////////////////

  private void restartViz() {
    try {
      d17grid = d17.loadData(datafile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    goalLoc = new IntPair(d17grid.numCols - 1, d17grid.numRows - 1);
    d17grid.numVisits[0][0] = 5;
//    computeTime = 40;// Math.max((long) (d17grid.numCols * d17grid.numRows * .0001), 5);
    System.out.println("computerTime = " + computeTime);

    for (Algo a : d17algs.values()) {

      a.priorityQueue = new PriorityQueue<Pair<Integer, D17Crucible>>((aa, b) -> aa.x - b.x);
      a.minHeatLossMap = new HashMap<D17Crucible, Pair<Integer, D17Crucible>>();

      a.startCrucible = new D17Crucible(new IntPair(0, 0), MyCardinalDirection.East, 0);
      a.minHeatLossMap.put(a.startCrucible, new Pair<Integer, D17Crucible>(0, null));
      a.priorityQueue.add(new Pair<Integer, D17Crucible>(0, a.startCrucible));

      a.startCrucible = new D17Crucible(new IntPair(0, 0), MyCardinalDirection.South, 0);
      a.minHeatLossMap.put(a.startCrucible, new Pair<Integer, D17Crucible>(0, null));
      a.priorityQueue.add(new Pair<Integer, D17Crucible>(0, a.startCrucible));

      a.finalCrucible = null;
      a.crucible = null;
    }

  }

  ////////////////////////////////////////////////////////////////////////////////////////

  public static void main(String args[]) {
    PApplet.main(new String[] { Day17Viz.class.getName() });
  }

  ////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public void settings() {
    d17 = new Day17BothParts();
    d17algs = new HashMap<Algorithm, Algo>();
    d17algs.put(Algorithm.part1PathRestriction, new Algo(150, 0, 50, 250, 0, 100));
    d17algs.put(Algorithm.part2PathRestriction, new Algo(0, 150, 0, 0, 250, 0));
    d17algs.put(Algorithm.noPathRestriction, new Algo(0, 150, 150, 0, 250, 250));
    d17algs.put(Algorithm.heatFence, new Algo(150, 0, 150, 250, 0, 250));

    size(winSize, winSize);
  }

  ////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public void setup() {
    surface.setResizable(true);
    surface.setLocation(50, 50);
    check_focus();
  }

  ////////////////////////////////////////////////////////////////////////////////////////

  void check_focus() {
    if (!focused)
      ((java.awt.Canvas) surface.getNative()).requestFocus();
  }
  ////////////////////////////////////////////////////////////////////////////////////

  private void drawPath(Algo a, D17Crucible crucible, int rrr, int ggg, int bbb) {

    for (D17Crucible c = crucible; c != null;) {
      D17Crucible c2 = a.minHeatLossMap.get(c).getSecond();

      if (c2 == null)
        break;

      int x2, y2;

      int x1 = c.loc.x * tileWd + canvasX + tileWd / 2;
      int y1 = c.loc.y * tileHt + canvasY + tileHt / 2;
      x2 = c2.loc.x * tileWd + canvasX + tileWd / 2;
      y2 = c2.loc.y * tileHt + canvasY + tileHt / 2;

      stroke(rrr, ggg, bbb);
      strokeWeight(Math.max(tileWd / 10, 2));

      line(x1, y1, x2, y2);

      c = c2;
    }
  }

  ////////////////////////////////////////////////////////////////////////////////////

  private void drawRect(int cc, int rr) {

    // low yellow 97, 71, 1
    // hi yellow 255, 187, 3

    int x = canvasX + cc * tileWd;
    int y = canvasY + rr * tileHt;

    double hh = (d17grid.heatLoss[cc][rr] - 1) / 8e0; // range 0 - 1

    int colB = d17grid.numVisits[cc][rr] + 20;
    int colR = ((int) (hh * (255 - 97 - 40)) + 96) - colB;
    int colG = ((int) (hh * (187 - 71 - 40)) + 70) - colB;

    stroke(colR, colG, colB + 150);
    strokeWeight(0);

    fill(colR, colG, colB);
    final int buf = 1; // Math.min(3, tileWd / 20);
    final int rad = buf;
    rect(x + buf, y + buf, tileWd - buf - buf, tileHt - buf - buf, rad, rad, rad, rad);
  }

  ////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public void draw() {

    background(0, 0, 50);

    int frame_x = max(0, width - border * 2);
    int frame_y = max(0, height - border * 2);

    if (last_x != frame_x || last_y != frame_y) {

      restartViz();

      last_x = frame_x;
      last_y = frame_y;

      float scale_x = (float) frame_x / d17grid.numCols;
      float scale_y = (float) frame_y / d17grid.numRows;

      // force square
      scale_x = Math.min(scale_x, scale_y);
      scale_y = scale_x;

      tileWd = (int) (scale_x);
      tileHt = (int) (scale_y);

      canvasX = (frame_x - d17grid.numCols * tileWd) / 2 + border;
      canvasY = (frame_y - d17grid.numRows * tileHt) / 2 + border;

      frameCount = 0;

    }
    for (int rr = 0; rr < d17grid.numRows; rr++) {
      for (int cc = 0; cc < d17grid.numCols; cc++) {
        drawRect(cc, rr);
      }
    }

    long start = System.currentTimeMillis();
    if (frameCount > 1) {
      do {
        for (Map.Entry<Algorithm, Algo> entry : d17algs.entrySet()) {
          Algo a = entry.getValue();
          Algorithm algorithm = entry.getKey();
          if (a.finalCrucible == null) {
            a.finalCrucible = iterateBestPath(algorithm);
          }
        }
      } while (System.currentTimeMillis() - start < computeTime);
    }

    boolean done = true;
    for (Algo a : d17algs.values()) {
      if (a.finalCrucible == null) {
        done = false;
        drawPath(a, a.crucible, a.lineRed, a.lineGreen, a.lineBlue);
      } else {
        drawPath(a, a.finalCrucible, a.endRed, a.endGreen, a.endBlue);
      }
    }

    textSize(12);
    fill(200, 200, 20);
    text(
        "Advent of Code 2023 Day 17: Part 1 in red, Part 2 in green, Dijkstra unrestricted in cyan, 6 limit fence is pink.",
        50, 30);

    if (!done) {
      int lookTime = ((frameCount > 1 && frameCount < 7) ? longLookMillis : shortLookMillis);
      long delta = System.currentTimeMillis() - start;
      if (delta < lookTime)
        delay((int) (lookTime - delta));
//      System.out.println("delta = " + delta + " dealy = " + (lookTime - delta));

    }

    saveFrame(saveFrameFilename);

  }

  /**
   * iterate the crucible in this algorithm
   * 
   * @param alg
   * @return null if not done
   */
  private D17Crucible iterateBestPath(Algorithm alg) {

    Algo a = d17algs.get(alg);

    if (a.priorityQueue.isEmpty()) {
      return a.crucible;
    }

    Pair<Integer, D17Crucible> p = a.priorityQueue.poll();
    int crucibleHeatLoss = p.x;
    a.crucible = p.y;

    d17grid.numVisits[a.crucible.loc.x][a.crucible.loc.y] += 1;

    drawRect(a.crucible.loc.x, a.crucible.loc.y);

    if (a.crucible.loc.equals(goalLoc)) {
      return a.crucible;
    }

    HashSet<D17Crucible> neighbors = a.crucible.getNeighbors(alg);
    d17grid.checkBounds(neighbors, alg);

    for (D17Crucible neighbor : neighbors) {

      int newHeatLoss = crucibleHeatLoss + d17grid.heatLoss[neighbor.loc.x][neighbor.loc.y];
      final Pair<Integer, D17Crucible> defaultVal = new Pair<Integer, D17Crucible>(
          Integer.MAX_VALUE, null);

      int prevMinHeatLoss = a.minHeatLossMap.getOrDefault(neighbor, defaultVal).getFirst();

      if (newHeatLoss < prevMinHeatLoss) {
        Pair<Integer, D17Crucible> minPrevious = new Pair<Integer, D17Crucible>(newHeatLoss,
            a.crucible);
        a.minHeatLossMap.put(neighbor, minPrevious);
        Pair<Integer, D17Crucible> minNeighbor = new Pair<Integer, D17Crucible>(newHeatLoss,
            neighbor);
        a.priorityQueue.add(minNeighbor);
      }
    }
    return null;
  }

}