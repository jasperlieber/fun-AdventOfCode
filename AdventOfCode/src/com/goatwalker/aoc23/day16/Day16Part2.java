package com.goatwalker.aoc23.day16;

import java.util.ArrayList;
import java.util.LinkedList;

import com.goatwalker.utils.MyCardinalDirection;
import com.goatwalker.utils.MyFileReader;

/**
 * Puzzle description: https://adventofcode.com/2023/day/16
 */
public class Day16Part2 {

  String datafile = "src/com/goatwalker/aoc23/day16/day16.txt";

  public static void main(String[] args) throws Exception {
    Day16Part2 puzzle = new Day16Part2();
    puzzle.doit();
  }

  private void doit() throws Exception {
    ArrayList<String> lines = MyFileReader.readFile(datafile);
    int maxEnergized = 0;
    int cnt = 0;
    D16Grid origGrid = new D16Grid(lines);

    // shoot from the left
    for (int row = 0; row < origGrid.numRows; row++) {
      D16Grid d16grid = origGrid.clone();
      cnt = d16grid.rayTrace(new D16Ray(-1, row, MyCardinalDirection.east));
      if (cnt > maxEnergized)
        maxEnergized = cnt;
    }

    // shoot from the bottom
    for (int col = 0; col < origGrid.numCols; col++) {
      D16Grid d16grid = origGrid.clone();
      cnt = d16grid.rayTrace(new D16Ray(col, d16grid.numRows, MyCardinalDirection.north));
      if (cnt > maxEnergized)
        maxEnergized = cnt;
    }

    // shoot from the right
    for (int row = 0; row < origGrid.numRows; row++) {
      D16Grid d16grid = origGrid.clone();
      cnt = d16grid.rayTrace(new D16Ray(d16grid.numCols, row, MyCardinalDirection.west));
      if (cnt > maxEnergized)
        maxEnergized = cnt;
    }

    // shoot from the top
    for (int col = 0; col < origGrid.numCols; col++) {
      D16Grid d16grid = origGrid.clone();
      cnt = d16grid.rayTrace(new D16Ray(col, -1, MyCardinalDirection.south));
      if (cnt > maxEnergized)
        maxEnergized = cnt;
    }

    System.out.println("max = " + maxEnergized);
  }

  /////////////////////////////////////////////////////////////////////////

  /**
   * A ray has a position and a direction. A method is included to get the
   * direction of a 45 reflected ray.
   *
   */
  private class D16Ray {

    int row, col; // where the ray is coming from
    MyCardinalDirection points = new MyCardinalDirection(); // the direction the ray is
                                                            // pointing

    public D16Ray(int x, int y, byte direction) {
      row = y;
      col = x;
      points.dir = direction;
    }

    public D16Ray copy() {
      return new D16Ray(col, row, points.dir);
    }

    /**
     * Given a symbol, get the new direction for this ray.
     * 
     * @param the mirror the ray is encountering
     * @return the new direction
     * @throws Exception if bad direction
     */
    public byte getReflectedRayDir(char sym) throws Exception {
      switch (points.dir) {
      case MyCardinalDirection.north:
        return sym == '/' ? MyCardinalDirection.east : MyCardinalDirection.west;
      case MyCardinalDirection.east:
        return sym == '/' ? MyCardinalDirection.north : MyCardinalDirection.south;
      case MyCardinalDirection.south:
        return sym == '/' ? MyCardinalDirection.west : MyCardinalDirection.east;
      case MyCardinalDirection.west:
        return sym == '/' ? MyCardinalDirection.south : MyCardinalDirection.north;
      default:
        throw new Exception("unknow direction " + points);
      }
    }

    @Override
    public String toString() {
      return "[" + col + " " + row + " " + points.dirStr() + "]";
    }
  }

  /////////////////////////////////////////////////////////////////////////

  /**
   * A Tile holds a symbol and a bitwise bitwise union of directions of rays that
   * come into the tile.
   *
   */
  private class D16Tile {

    final char sym;
    // bitwise union of visited ray directions
    public MyCardinalDirection visitedDirs = new MyCardinalDirection();

    public D16Tile(char sym) {
      this.sym = sym;
    }

    @Override
    public String toString() {
      return sym + " (visitedDirs=" + visitedDirs.dirStr() + ")";
    }
  }

  /////////////////////////////////////////////////////////////////////////

  /**
   * The grid holds all the tiles, and a count of visited tiles.
   */
  private class D16Grid {
    LinkedList<D16Ray> bfsList = new LinkedList<D16Ray>();
    public int visitedCnt = 0;
    final int numRows, numCols;
    D16Tile[][] tiles; // cols, rows

    public D16Grid(int c, int r) {
      numRows = r;
      numCols = c;
    }

    public D16Grid(ArrayList<String> lines) {

      numRows = lines.size();
      numCols = lines.get(0).length();

      tiles = new D16Tile[numCols][numRows];
      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          tiles[col][row] = new D16Tile(lines.get(row).charAt(col));
        }
      }
    }

    /**
     * Starting west of tile 0,0, shoot a ray into the grid. Use a linked list to
     * hold live rays. As long as here are rays to process, pull a ray off the front
     * of the list, and try to move in that direction. If nothing on list, we are
     * done. If hit a blank or mirror, update the ray and add back onto the list. If
     * hit a splitter, add 2 rays to the list.
     * 
     * @throws Exception
     */
    public int rayTrace(D16Ray ray) throws Exception {

      // add the initial ray to the search list
      bfsList.add(ray);

//      final Scanner scanner = new Scanner(System.in);

      while (!bfsList.isEmpty()) {

        ray = bfsList.remove();

        // moveRayForward() returns null if hits a wall or an already visited tile in
        // the ray's direction.

        if (moveRayForward(ray)) {

          // ray has been updated to the next position, but it's direction needs to be
          // updated.
          // But first, record that this tile has been visited with the ray's direction.

          addVisit(ray);

          // Get the next direction for the ray, based on the type of symbol in the grid.

          char sym = tiles[ray.col][ray.row].sym;

          // update ray's direction
          switch (sym) {

          // continue in same direction
          case '.':
            break;

          // update ray direction via a mirror reflection
          case '/':
          case '\\':
            ray.points.dir = ray.getReflectedRayDir(sym);
            break;

          // hit a splitter -- add a 2nd ray
          case '|':
            if (ray.points.isEastOrWest()) {
              ray.points.dir = MyCardinalDirection.north;
              D16Ray ray2 = ray.copy();
              ray2.points.dir = MyCardinalDirection.south;
              bfsList.add(ray2);
            }
            break;
          case '-':
            if (ray.points.isNorthOrSouth()) {
              ray.points.dir = MyCardinalDirection.west;
              D16Ray ray2 = ray.copy();
              ray2.points.dir = MyCardinalDirection.east;
              bfsList.add(ray2);
            }
            break;
          default:
            throw new Exception("Unknown symbol " + sym);
          }

          // add the new ray with its computed direction
          bfsList.add(ray);

//          System.out.print("bfsList: ");
//          bfsList.stream().forEach(System.out::print);
//          System.out.println("\n" + d16grid.showTraced());
//          scanner.nextLine();
        }
      }
//      scanner.close();

//      System.out.println(d16grid.showTraced());
      return getEnergizedCnt();
    }

    /**
     * Record that this tile pointed to by the ray has been visited in the ray's
     * direction. Increment the visitedCnt when first hit.
     * 
     * @param ray
     */
    public void addVisit(D16Ray ray) {
      if (tiles[ray.col][ray.row].visitedDirs.dir == 0)
        visitedCnt++;

      tiles[ray.col][ray.row].visitedDirs.dir |= ray.points.dir;
    }

    public int getEnergizedCnt() {
      return visitedCnt;
    }

    /**
     * Move a ray in its direction. Return false if against a wall or an already
     * visited tile.
     * 
     * @param ray - the incoming ray
     * @return false if hitting a wall or a tile that has already been visited in
     *         the rays direction.
     */
    public boolean moveRayForward(D16Ray ray) {

      switch (ray.points.dir) {
      case MyCardinalDirection.north:
        ray.row--;
        break;
      case MyCardinalDirection.east:
        ray.col++;
        break;
      case MyCardinalDirection.south:
        ray.row++;
        break;
      case MyCardinalDirection.west:
        ray.col--;
        break;
      }
      if (ray.col < 0 || ray.col >= numCols || ray.row < 0 || ray.row >= numRows)
        return false;

      if ((tiles[ray.col][ray.row].visitedDirs.dir & ray.points.dir) != 0)
        return false;

      return true;
    }

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();

      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          char sym = tiles[col][row].sym;
          sb.append(sym);
        }
        sb.append("\n");
      }
      return sb.toString();
    }

    /**
     * Return a string representation of the grid, with . for unvisited tiles, and a
     * numeric count of tiles a tile has been visited by rays.
     * 
     * @return
     */
    @SuppressWarnings("unused")
    public String showTraced() {
      StringBuilder sb = new StringBuilder();

      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          String sym = tiles[col][row].visitedDirs.dir == 0 ? "."
              : ("" + Integer.bitCount(tiles[col][row].visitedDirs.dir));
          sb.append(sym);
        }
        sb.append("\n");
      }
      return sb.toString();
    }

    @Override
    protected D16Grid clone() {
      D16Grid g = new D16Grid(numCols, numRows);
      g.tiles = new D16Tile[numCols][numRows];
      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          g.tiles[col][row] = new D16Tile(tiles[col][row].sym);
        }
      }
      return g;
    }
  }
}
