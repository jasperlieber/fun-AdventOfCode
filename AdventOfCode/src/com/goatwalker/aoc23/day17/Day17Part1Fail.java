package com.goatwalker.aoc23.day17;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import com.goatwalker.utils.IntPair;
import com.goatwalker.utils.MyFileReader;

/**
 * Puzzle description: https://adventofcode.com/2023/day/15
 * 
 * This attempt failed because I was keeping minimum scores in the grid, which
 * doesn't allow for multiple paths to each grid location.
 * 
 * It does have a nice implementation of A*
 */
public class Day17Part1Fail {
  protected enum Dir {
    north,
    south,
    east,
    west
  }

  String datafile = "src/com/goatwalker/aoc23/day17/test_input.txt";

  D17Grid d17grid;

  public static void main(String[] args) throws Exception {
    Day17Part1Fail puzzle = new Day17Part1Fail();
    puzzle.doit();
  }

  private void doit() throws Exception {
    d17grid = loadData();

//    System.out.println(d17grid);

    IntPair startLoc = new IntPair(0, 0);
    IntPair goalLoc = new IntPair(d17grid.numCols - 1, d17grid.numRows - 1);
    LinkedList<D17Block> path = d17grid.findBestPath(startLoc, goalLoc);
//    System.out.println("best path: " + path.toString());
    System.out.println();

    int heatLoss = 0;
    for (D17Block b : path) {
      heatLoss += b.val;
      System.out.print(b.loc + " ");
    }
    System.out.println("\n\nheat loss = " + heatLoss);

  }

  private D17Grid loadData() throws FileNotFoundException {
    ArrayList<String> lines = MyFileReader.readFile(datafile);
    return new D17Grid(lines);
  }

////////////////////////////////////////////////////////////

  private class D17Block implements Comparable<D17Block> {
    final private IntPair loc;
    final private byte val;
    private int gScore = Integer.MAX_VALUE;
    private int fScore = Integer.MAX_VALUE;
    // node immediately preceding with lowest known cost to start
    private D17Block cameFrom = null;
    private int heuristic = -1;
    private Dir dir;
    private int runLength = 0;

//    public Dir dir = null;

    public D17Block(IntPair l, byte b) {
      loc = l;
      val = b;
    }

    @Override
    public int compareTo(D17Block o) {
      int cmp = fScore < o.fScore ? -1 : (fScore == o.fScore ? 0 : 1);
//      System.out.printf("  compareTo(%s,%s)=%d\n", this, o, cmp);
      return cmp;
    }

    @Override
    public String toString() {
      return String.format("[%s v=%d g=%d f=%d heur=%d cameFrom=(%s) dir=%s run=%d]", loc, val,
          gScore, fScore, heuristic,
          cameFrom == null ? "null" : (cameFrom.loc.x + " " + cameFrom.loc.y), dir, runLength);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((loc == null) ? 0 : loc.hashCode());
      return result;
    }

    /**
     * For equality, look at the location and the cameFrom.
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      D17Block other = (D17Block) obj;
      if (loc == null) {
        if (other.loc != null)
          return false;
      } else if (!loc.equals(other.loc))
        return false;

      if (cameFrom == null) {
        if (other.cameFrom != null)
          return false;
      } else if (!cameFrom.equals(other.cameFrom))
        return false;

      return true;
    }

//    public ArrayList<D17Block> getNeighbors() {
//      System.out.print(" neighbors of " + this + ":");
//      ArrayList<D17Block> neighbors = new ArrayList<D17Block>();
//      int xx = loc.x;
//      int yy = loc.y;
//      int xMinus4 = 0;
//      int yMinus4 = 0;
//      if (cameFrom != null && cameFrom.cameFrom != null && cameFrom.cameFrom.cameFrom != null
//          && cameFrom.cameFrom.cameFrom.cameFrom != null) {
//        xMinus4 = xx - cameFrom.cameFrom.cameFrom.cameFrom.loc.x;
//        yMinus4 = yy - cameFrom.cameFrom.cameFrom.cameFrom.loc.y;
//      }
//      if (xx > 0 && xMinus4 != -4)
//        neighbors.add(d17grid.blocks[xx - 1][yy]);
//      if ((xx < d17grid.numCols - 1) && xMinus4 != 4)
//        neighbors.add(d17grid.blocks[xx + 1][yy]);
//      if (yy > 0 && yMinus4 != -4)
//        neighbors.add(d17grid.blocks[xx][yy - 1]);
//      if ((yy < d17grid.numRows - 1) && yMinus4 != 4)
//        neighbors.add(d17grid.blocks[xx][yy + 1]);
//      System.out.println(" - all = " + neighbors.toString());
//      return neighbors;
//    }

//    public ArrayList<D17Block> getNeighbors() {
//      System.out.print(" neighbors of " + this + ":");
//      ArrayList<D17Block> neighbors = new ArrayList<D17Block>();
//      int xx = loc.x;
//      int yy = loc.y;
//      int xMinus3 = 0;
//      int yMinus3 = 0;
//      if (cameFrom != null && cameFrom.cameFrom != null && cameFrom.cameFrom.cameFrom != null) {
//        xMinus3 = xx - cameFrom.cameFrom.cameFrom.loc.x;
//        yMinus3 = yy - cameFrom.cameFrom.cameFrom.loc.y;
//      }
//      if (xx > 0 && xMinus3 != -3)
//        neighbors.add(d17grid.blocks[xx - 1][yy]);
//      if ((xx < d17grid.numCols - 1) && xMinus3 != 3)
//        neighbors.add(d17grid.blocks[xx + 1][yy]);
//      if (yy > 0 && yMinus3 != -3)
//        neighbors.add(d17grid.blocks[xx][yy - 1]);
//      if ((yy < d17grid.numRows - 1) && yMinus3 != 3)
//        neighbors.add(d17grid.blocks[xx][yy + 1]);
//      System.out.println(" - all = " + neighbors.toString());
//      return neighbors;
//    }

//    public ArrayList<D17Block> getNeighbors() {
//      System.out.print("  neighbors of " + this + ":");
//      ArrayList<D17Block> neighbors = new ArrayList<D17Block>();
//      int xx = loc.x;
//      int yy = loc.y;
//      
//      
//      boolean sameDir3 = cameFrom != null && cameFrom.cameFrom != null
//          && cameFrom.cameFrom.cameFrom != null && cameFrom.dir == cameFrom.cameFrom.dir
//          && cameFrom.dir == cameFrom.cameFrom.cameFrom.dir;
//
//      if (xx > 0 && !(sameDir3 && cameFrom.dir == Dir.west)) {
//        neighbors.add(d17grid.blocks[xx - 1][yy]);
//      }
//      if ((xx < d17grid.numCols - 1) && !(sameDir3 && cameFrom.dir == Dir.east)) {
//        neighbors.add(d17grid.blocks[xx + 1][yy]);
//      }
//      if (yy > 0 && !(sameDir3 && cameFrom.dir == Dir.south)) {
//        neighbors.add(d17grid.blocks[xx][yy - 1]);
//      }
//      if ((yy < d17grid.numRows - 1) && !(sameDir3 && cameFrom.dir == Dir.north)) {
//        neighbors.add(d17grid.blocks[xx][yy + 1]);
//      }
//      System.out.println(" - all = " + neighbors.toString());
//      return neighbors;
//    }

    /**
     * get direction from this block to the destination block
     * 
     * @param dest
     * @throws Exception
     */
    public Dir getDirTo(D17Block dest) throws Exception {
      if (loc.x + 1 == dest.loc.x)
        return Dir.east;
      if (loc.x - 1 == dest.loc.x)
        return Dir.west;
      if (loc.y + 1 == dest.loc.y)
        return Dir.south;
      if (loc.y - 1 == dest.loc.y)
        return Dir.north;
      throw new Exception();
    }

    public HashSet<D17Block> getNeighbors() throws Exception {
      HashSet<D17Block> neighbors = new HashSet<D17Block>();
      int xx = loc.x;
      int yy = loc.y;
      if (xx > 0) {
        neighbors.add(d17grid.blocks[xx - 1][yy]);
      }
      if ((xx < d17grid.numCols - 1)) {
        neighbors.add(d17grid.blocks[xx + 1][yy]);
      }
      if (yy > 0) {
        neighbors.add(d17grid.blocks[xx][yy - 1]);
      }
      if ((yy < d17grid.numRows - 1)) {
        neighbors.add(d17grid.blocks[xx][yy + 1]);
      }

      if (runLength == 3) {
        D17Block badNeighbor = null;
        for (D17Block neighbor : neighbors) {
          Dir dirToNeighbor = getDirTo(neighbor);
          if (dir == dirToNeighbor) {
            badNeighbor = neighbor;
            break;
          }
        }
        if (badNeighbor != null)
          neighbors.remove(badNeighbor);
      }
//      System.out.print("  neighbors of " + this + ":");
//      System.out.println(" - all = " + neighbors.toString());
      return neighbors;
    }

    public boolean tooStraight(D17Block neighbor) throws Exception {
      Dir dirToNeighbor = getDirTo(neighbor);
      if (dir == dirToNeighbor) {
        if (runLength == 3)
          return true;
        neighbor.runLength = runLength + 1;
      } else {
        neighbor.runLength = 1;
      }
      return false;
    }
  }

////////////////////////////////////////////////////////////

  private class D17Grid {
    final int numRows, numCols;
    D17Block[][] blocks;
    private PriorityQueue<D17Block> priorityQueue = new PriorityQueue<D17Block>();
    private D17Block startBlock;
    private D17Block goalBlock;

    public D17Grid(ArrayList<String> lines) {
      numRows = lines.size();
      numCols = lines.get(0).length();
      blocks = new D17Block[numCols][numRows];

      int row = 0;
      for (String line : lines) {
        int col = 0;
        for (char ch : line.toCharArray()) {
          D17Block b = new D17Block(new IntPair(col, row), (byte) (ch - '0'));
          blocks[col++][row] = b;
        }
        row++;
      }
    }

    public LinkedList<D17Block> findBestPath(IntPair startLoc, IntPair goalLoc) throws Exception {
      startBlock = blocks[startLoc.x][startLoc.y];
      startBlock.runLength = 0;
      startBlock.dir = Dir.north;
      goalBlock = blocks[goalLoc.x][goalLoc.y];
      startBlock.fScore = heuristic(startBlock);
      startBlock.gScore = 0;
      priorityQueue.add(startBlock);

      while (!priorityQueue.isEmpty()) {
        D17Block curBlock = priorityQueue.poll();

        System.out.println(" curBlock=" + curBlock);

        if (curBlock.loc.equals(goalBlock.loc))
          return reconstructPath(curBlock);

        HashSet<D17Block> neighbors = curBlock.getNeighbors();
        int jj = 0;
        for (D17Block neighbor : neighbors) {
          System.out.println("   neighbor #" + ++jj + ": " + neighbor);
        }

        for (D17Block neighbor : neighbors) {
          int neighborWeight = neighbor.val;
          int tentativeGScore = curBlock.gScore + neighborWeight;
          if (tentativeGScore < neighbor.gScore) {
            neighbor.cameFrom = curBlock;
            neighbor.dir = curBlock.getDirTo(neighbor);
            neighbor.runLength = neighbor.dir == curBlock.dir ? (curBlock.runLength + 1) : 1;
//            curBlock.getDirTo(neighbor);
            neighbor.gScore = tentativeGScore;
            neighbor.fScore = tentativeGScore + heuristic(neighbor);
//            if (!priorityQueue.contains(neighbor)) {
            priorityQueue.add(neighbor);
//            }

          }
        }
      }

      throw new Exception("no path found");
    }

    private LinkedList<D17Block> reconstructPath(D17Block curBlock) {
      LinkedList<D17Block> path = new LinkedList<D17Block>();
      path.add(curBlock);
      while (!(curBlock = curBlock.cameFrom).equals(startBlock)) {
//        System.out.println(" path=" + path.toString());
        path.addFirst(curBlock);
      }
      return path;
    }

    private int heuristic(D17Block c) {
      if (goalBlock.loc.equals(c.loc))
        return 0;

      if (c.heuristic > 0)
        return c.heuristic;

      int xSpan = goalBlock.loc.x - c.loc.x;
      int ySpan = goalBlock.loc.y - c.loc.y;
      double maxSpan = Math.max(xSpan, ySpan);
      int sum = 0;
//      System.out.printf("  heur(%d %d) = sum of ", c.loc.x, c.loc.y);
      for (int walk = 1; walk <= maxSpan; walk++) {
        int col = (int) (walk * (xSpan / maxSpan) + .49) + c.loc.x;
        int row = (int) (walk * (ySpan / maxSpan) + .49) + c.loc.y;
        sum += blocks[col][row].val;
//        System.out.printf("(%d %d)=%d ", col, row, blocks[col][row].val);
      }
//      System.out.println(", sum = " + sum);
      c.heuristic = sum;
      return sum;
    }

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("GRID numCols=" + numCols + " numRows=" + numRows + ":\n");
      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          sb.append(blocks[col][row].val);
        }
        sb.append("\n");
      }
      return sb.toString();
    }
  }
}
