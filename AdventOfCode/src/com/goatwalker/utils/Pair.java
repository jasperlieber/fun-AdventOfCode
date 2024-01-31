package com.goatwalker.utils;

public class Pair<A, B> {
    public A x;
    public B y;

    public Pair(A first, B second) {
        super();
        this.x = first;
        this.y = second;
    }

    public int hashCode() {
        int hashFirst = x != null ? x.hashCode() : 0;
        int hashSecond = y != null ? y.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair<?, ?> otherPair = (Pair<?, ?>) other;
            return 
            ((  this.x == otherPair.x ||
                ( this.x != null && otherPair.x != null &&
                  this.x.equals(otherPair.x))) &&
             (  this.y == otherPair.y ||
                ( this.y != null && otherPair.y != null &&
                  this.y.equals(otherPair.y))) );
        }

        return false;
    }

    public String toString()
    { 
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