package com.goatwalker.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day01Part1 {

  public static void main(String[] args) throws IOException {

    int increase = 0;
    try {
      String dfile = "2021_data\\d01p1.txt";
      BufferedReader reader = new BufferedReader(new FileReader(dfile));
      String line = reader.readLine();
      int last = Integer.parseInt(line);
      while (line != null) {
        int next = Integer.parseInt(line);
        if (next > last)
          increase++;
        last = next;
        line = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(increase);
  }
}
