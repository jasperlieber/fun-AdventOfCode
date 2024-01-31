package com.goatwalker.aoc21;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

public class Day03Part2 {

  private enum Chem {
    oxy, co2
  }

  private static final boolean debug = false;

  public static void main(String[] args) throws Exception {
    Day03Part2 me = new Day03Part2();
    me.run();
  }

  private void run() throws Exception {

    String dfile = "2021_data\\d03p1.txt";

    Path path = Paths.get(dfile);
    List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

//    String[] lines = { "00100", "11110", "10110", "10111", "10101", "01111", "00111",
//        "11100", "10000", "11001", "00010", "01010" };

    HashSet<char[]> readings = new HashSet<char[]>();

    for (String line : lines)
      readings.add(line.toCharArray());

    HashSet<char[]> oxSet = new HashSet<char[]>(readings);
    HashSet<char[]> coSet = new HashSet<char[]>(readings);

    boolean done = false;
    for (int pos = 0; !done; pos++) {
      done = process(oxSet, pos, Chem.oxy);
    }

    done = false;
    for (int pos = 0; !done; pos++) {
      done = process(coSet, pos, Chem.co2);
    }

    int oxRate = 0;
    for (char[] oxy : oxSet)
      oxRate = Integer.parseInt(String.valueOf(oxy), 2);
    int coRate = 0;
    for (char[] co2 : coSet)
      coRate = Integer.parseInt(String.valueOf(co2), 2);


    System.out.println(oxRate * coRate);
  }

  private boolean process(HashSet<char[]> readings, int pos, Chem chem) throws Exception {
    int onesCnt = 0;
    for (char[] reading : readings) {
      if (reading[pos] == '1')
        onesCnt++;
    }
    
    if (debug) System.out.format("into process: chem=%s pos=%d \n", chem, pos);

    int zerosCnt = readings.size() - onesCnt;

    char keepBit = onesCnt >= zerosCnt ? '1' : '0';
    if (chem == Chem.co2)
      keepBit = keepBit == '1' ? '0' : '1';

//    if (chem == Chem.oxy) {
//      if (onesCnt >= zerosCnt)
//        keepBit = 1;
//      else
//        keepBit = 0;
//    } else {
//      if (onesCnt >= zerosCnt)
//        keepBit = 0;
//      else
//        keepBit = 1;
//    }

//    int keepBit = (chem == Chem.oxy ? 
//    if (cnt + cnt >= readings.size())
    
    if (debug) System.out.format("zeroCnt=%d onesCnt=%d keepBit=%s\n", zerosCnt, onesCnt, keepBit);

    HashSet<char[]> reduction = new HashSet<char[]>();
    for (char[] reading : readings) {
      if (reading[pos] == keepBit)
        reduction.add(reading);
    }
    
    if (debug) System.out.format("readings =%s\n", dump(readings));
    if (debug) System.out.format("reduction=%s\n\n", dump(reduction));


    readings.clear();
    readings.addAll(reduction);
    
    if (readings.size() == 0)
      throw new Exception("all gone");

    return readings.size() == 1;
  }

  private String dump(HashSet<char[]> readings) {
    StringBuffer out = new StringBuffer();
    for (char[] reading : readings) out.append(String.valueOf(reading) + ",");
    return out.toString();
  }

}
