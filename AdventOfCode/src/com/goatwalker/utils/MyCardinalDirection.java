package com.goatwalker.utils;

/**
 * A way to represent the cardinal directions, allowing boolean unions for
 * multiple directions
 *
 */
public class MyCardinalDirection {

  public byte dir = 0;

  // boolean values for directions
  public static final byte none = 0b0000;
  public static final byte north = 0b0001;
  public static final byte south = 0b0010;
  public static final byte west = 0b0100;
  public static final byte east = 0b1000;

  @Override
  public String toString() {
    switch (dir) {
    case north:
      return "north";
    case east:
      return "east";
    case south:
      return "south";
    case west:
      return "west";
    default:
      return "other";
    }
  }

  public static final MyCardinalDirection North = new MyCardinalDirection(
      MyCardinalDirection.north);
  public static final MyCardinalDirection West = new MyCardinalDirection(MyCardinalDirection.west);
  public static final MyCardinalDirection South = new MyCardinalDirection(
      MyCardinalDirection.south);
  public static final MyCardinalDirection East = new MyCardinalDirection(MyCardinalDirection.east);

  public MyCardinalDirection() {
  }

  public MyCardinalDirection(byte dir) {
    this.dir = dir;
  }

  public String dirStr() {
    String str = "";
    if (isNorth())
      str += "N";
    if (isEast())
      str += "E";
    if (isSouth())
      str += "S";
    if (isWest())
      str += "W";
    return str == "" ? "none" : str;
  }

  public boolean isWest() {
    return (dir & west) == west;
  }

  public boolean isSouth() {
    return (dir & south) == south;
  }

  public boolean isEast() {
    return (dir & east) == east;
  }

  public boolean isNorth() {
    return (dir & north) == north;
  }

  public boolean isEastOrWest() {
    return isEast() || isWest();
  }

  public boolean isNorthOrSouth() {
    return isNorth() || isSouth();
  }

  /**
   * @return New direction if left turn from this's direction
   */
  public MyCardinalDirection leftTurn() {
    switch (dir) {
    case north:
      return West;
    case east:
      return North;
    case south:
      return East;
    case west:
      return South;
    default:
      return null;
    }
  }

  /**
   * 
   * @return New direction if right turn from this's direction
   */
  public MyCardinalDirection rightTurn() {
    switch (dir) {
    case north:
      return East;
    case east:
      return South;
    case south:
      return West;
    case west:
      return North;
    default:
      return null;
    }
  }

  public char getCharDir() {
    switch (dir) {
    case north:
      return '^';
    case south:
      return 'v';
    case west:
      return '<';
    case east:
      return '>';
    default:
      return ' ';
    }
  }

}