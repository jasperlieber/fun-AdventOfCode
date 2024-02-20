package com.goatwalker.aoc23.day17;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.goatwalker.utils.IntPair;
import com.goatwalker.utils.MyCardinalDirection;
import com.goatwalker.utils.MyFileReader;
import com.goatwalker.utils.Pair;

/**
 * Puzzle description: https://adventofcode.com/2023/day/15
 */
public class Day17Part1 {

  String datafile = "src/com/goatwalker/aoc23/day17/test_input.txt";

  D17Grid d17grid;

  public static void main(String[] args) throws Exception {
    Day17Part1 puzzle = new Day17Part1();
    puzzle.doit();
  }

  private void doit() throws Exception {
    d17grid = loadData();
    int heatLoss = d17grid.findBestPath();
    System.out.println("heat loss = " + heatLoss);
  }

  private D17Grid loadData() throws FileNotFoundException {
    ArrayList<String> lines = MyFileReader.readFile(datafile);
    return new D17Grid(lines);
  }

////////////////////////////////////////////////////////////

  private class D17Crucible {

    final IntPair loc;
    final MyCardinalDirection dir;
    final int runLength;

    public D17Crucible(IntPair loc, MyCardinalDirection dir, int run) {
      this.loc = loc;
      this.dir = dir;
      runLength = run;
    }

    public HashSet<D17Crucible> getNeighbors() {
      HashSet<D17Crucible> neighbors = new HashSet<D17Crucible>();
      if (runLength < 2) {
        neighbors.add(goForward(dir, runLength + 1));
      }
      neighbors.add(goForward(dir.leftTurn(), 0));
      neighbors.add(goForward(dir.rightTurn(), 0));

      return neighbors;
    }

    private D17Crucible goForward(MyCardinalDirection dir, int runLength) {
      IntPair newLoc = new IntPair(loc);
      switch (dir.dir) {
      case MyCardinalDirection.north: // note north is "down"
        newLoc.y--;
        break;
      case MyCardinalDirection.south:
        newLoc.y++;
        break;
      case MyCardinalDirection.west:
        newLoc.x--;
        break;
      case MyCardinalDirection.east:
        newLoc.x++;
        break;
      }
      return new D17Crucible(newLoc, dir, runLength);
    }

    @Override
    public String toString() {
      return "[" + loc + ", dir=" + dir + ", runLength=" + runLength + "]";
    }

    // NOTE: It is necessary to implement hashCode() and equals() or the program
    // fails with a non-terminating condition.
    //
    // But why?? And I checked -- default hashCode() is just as unique as the one
    // below, i.e. there's a 1:1 relationship between super & this.hashCode.
    //
    // equals() is always different, which is also strange.
    //
    //
//    HashMap<Integer, Integer> hashes = new HashMap<Integer, Integer>();

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((dir == null) ? 0 : dir.hashCode());
      result = prime * result + ((loc == null) ? 0 : loc.hashCode());
      result = prime * result + runLength;

//      int sres = super.hashCode();
//
//      Integer prev = hashes.put(result, sres);
//      if (prev != null && prev != sres)
//        System.out.println("diff hash: " + this + " " + sres + " " + prev + " " + result);

      return result;
    }

    @Override
    public boolean equals(Object obj) {

      boolean eq1 = myEquals(obj);
//      boolean eq2 = super.equals(obj);
//
//      if (eq1 != eq2)
//        System.out.println("diff eq: " + obj + " --------- " + this + " " + eq1 + " " + eq2);
      return eq1;

    }

    private boolean myEquals(Object obj) {

      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      D17Crucible other = (D17Crucible) obj;
      if (dir != other.dir)
        return false;
      if (loc == null) {
        if (other.loc != null)
          return false;
      } else if (!loc.equals(other.loc))
        return false;
      if (runLength != other.runLength)
        return false;
      return true;
    }

  }

////////////////////////////////////////////////////////////

  private class D17Grid {
    final int numRows, numCols;
    final byte[][] heatLoss;
//    HashMap<D17Crucible, D17Crucible> minPath = new HashMap<D17Crucible, D17Crucible>();

    public D17Grid(ArrayList<String> lines) {
      numRows = lines.size();
      numCols = lines.get(0).length();
      heatLoss = new byte[numCols][numRows];

      int row = 0;
      for (String line : lines) {
        int col = 0;
        for (char ch : line.toCharArray()) {
          heatLoss[col++][row] = (byte) (ch - '0');
        }
        row++;
      }
    }

    /**
     * Use the dijkstra path finding algorithm to find shortest path.
     * 
     * @return minimum heat loss
     * @throws Exception if not path found
     */
    public int findBestPath() throws Exception {
      PriorityQueue<Pair<Integer, D17Crucible>> priorityQueue = new PriorityQueue<Pair<Integer, D17Crucible>>(
          (a, b) -> a.x - b.x);
      HashMap<D17Crucible, Pair<Integer, D17Crucible>> minHeatLossMap = new HashMap<D17Crucible, Pair<Integer, D17Crucible>>();

      // We can start moving east because advancing will turn south too, which
      // captures both initial possible directions.
      // It is critical that the initial run length is -1 -- this allows for the first
      // run to be four step, since
      // the first spot is not counted. This was a very difficult bug for me to figure
      // out.
      D17Crucible startCrucible = new D17Crucible(new IntPair(0, 0), MyCardinalDirection.East, -1);
      minHeatLossMap.put(startCrucible, new Pair<Integer, D17Crucible>(0, null));
      priorityQueue.add(new Pair<Integer, D17Crucible>(0, startCrucible));

      IntPair goalLoc = new IntPair(d17grid.numCols - 1, d17grid.numRows - 1);

      while (!priorityQueue.isEmpty()) {

        Pair<Integer, D17Crucible> p = priorityQueue.poll();
        int crucibleHeatLoss = p.x;
        D17Crucible crucible = p.y;

//        System.out.printf(" qsize=%d crucible=%s heat=%d\n", priorityQueue.size(), crucible,
//            crucibleHeatLoss);

        if (crucible.loc.equals(goalLoc)) {
          printPath(crucible, minHeatLossMap);
          return crucibleHeatLoss;
        }

        HashSet<D17Crucible> neighbors = crucible.getNeighbors();
        checkBounds(neighbors);

        for (D17Crucible neighbor : neighbors) {

          int newHeatLoss = crucibleHeatLoss + heatLoss[neighbor.loc.x][neighbor.loc.y];
          final Pair<Integer, D17Crucible> defaultVal = new Pair<Integer, D17Crucible>(
              Integer.MAX_VALUE, null);
          int prevMinHeatLoss = minHeatLossMap.getOrDefault(neighbor, defaultVal).getFirst();

          if (newHeatLoss < prevMinHeatLoss) {
            Pair<Integer, D17Crucible> minPrevious = new Pair<Integer, D17Crucible>(newHeatLoss,
                crucible);
            minHeatLossMap.put(neighbor, minPrevious);
            Pair<Integer, D17Crucible> minNeighbor = new Pair<Integer, D17Crucible>(newHeatLoss,
                neighbor);
            priorityQueue.add(minNeighbor);
          }
        }
      }

      throw new Exception("no path found!");
    }

    /**
     * Pretty print the path to the crucible contained in the map.
     * 
     * @param p
     * @param minPath
     */
    private void printPath(D17Crucible p,
        HashMap<D17Crucible, Pair<Integer, D17Crucible>> minPath) {
      char[][] buf1 = new char[numCols][numRows];
      char[][] buf2 = new char[numCols][numRows];
      for (int c = 0; c < numCols; c++)
        for (int r = 0; r < numRows; r++) {
          buf1[c][r] = (char) ('0' + heatLoss[c][r]);
          buf2[c][r] = (char) ('0' + heatLoss[c][r]);
        }

      int heatSum = -heatLoss[0][0];

      StringBuffer sb = new StringBuffer();

      buf2[p.loc.x][p.loc.y] = p.dir.getCharDir();

      for (D17Crucible c = p; minPath.get(c) != null; c = minPath.get(c).getSecond()) {
        buf2[c.loc.x][c.loc.y] = c.dir.getCharDir();
        heatSum += heatLoss[c.loc.x][c.loc.y];
        sb.insert(0,
            c.loc + " dir=" + c.dir + " sum=" + heatSum + " min=" + minPath.get(c).x + ", ");
      }

      sb.append(p.loc + "=" + heatSum + ", final crucible = " + p + " ");

      sb.append("\n\nheatSum = " + heatSum + "\n\n");

      for (int r = 0; r < numRows; r++) {
        for (int c = 0; c < numCols; c++)
          sb.append(buf1[c][r]);
        sb.append("  ");
        for (int c = 0; c < numCols; c++)
          sb.append(buf2[c][r]);
        sb.append("\n");
      }
      System.out.println(sb);
    }

    /**
     * Given a set of locations, cull any that are out of bounds.
     * 
     * @param neighbors
     */
    private void checkBounds(HashSet<D17Crucible> neighbors) {
      Iterator<D17Crucible> iter = neighbors.iterator();
      while (iter.hasNext()) {
        D17Crucible c = iter.next();
        if (c.loc.x < 0 || c.loc.y < 0 || c.loc.x >= numCols || c.loc.y >= numRows)
          iter.remove();
      }
    }

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("GRID numCols=" + numCols + " numRows=" + numRows + ":\n");
      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          sb.append(heatLoss[col][row]);
        }
        sb.append("\n");
      }
      return sb.toString();
    }
  }
}
