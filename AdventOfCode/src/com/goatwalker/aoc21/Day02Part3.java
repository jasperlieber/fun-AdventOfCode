package com.goatwalker.aoc21;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Day02Part3 {

  public static void main(String[] args) throws Exception {

    String dfile = "2021_data\\d02p1.txt";

    Path path = Paths.get(dfile);
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
    
    int hh = 0;
    int vv = 0;
    for (String line : lines) {
      String[] comm = line.split(" ");
      String dir = comm[0];
      int amt = Integer.parseInt(comm[1]);
      if (dir.equals("forward")) hh += amt;
      else if (dir.equals("up")) vv -= amt;
      else if (dir.equals("down")) vv += amt;
      else throw new Exception("unknown: " + comm);
        
      }
    
    System.out.println(hh * vv);
    }
  
    
}
