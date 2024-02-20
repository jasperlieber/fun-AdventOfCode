package com.goatwalker.utils;

public class Pair<A, B> {
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((x == null) ? 0 : x.hashCode());
    result = prime * result + ((y == null) ? 0 : y.hashCode());
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
    Pair<?, ?> other = (Pair<?, ?>) obj;
    if (x == null) {
      if (other.x != null)
        return false;
    } else if (!x.equals(other.x))
      return false;
    if (y == null) {
      if (other.y != null)
        return false;
    } else if (!y.equals(other.y))
      return false;
    return true;
  }

  public A x;
  public B y;

  public Pair(A first, B second) {
    super();
    this.x = first;
    this.y = second;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  public A getFirst() {
    return x;
  }

//    public void setFirst(A first) {
//        this.first = first;
//    }

  public B getSecond() {
    return y;
  }

//    public void setSecond(B second) {
//        this.second = second;
//    }
}