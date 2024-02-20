package com.goatwalker.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MyFileReader {

  /**
   * Read all the lines in the supplied filename
   * @param String filename
   * @return an ArrayList<String> of lines in the file
   * @throws FileNotFoundException
   */
  public static ArrayList<String> readFile(String filename) throws FileNotFoundException {
    ArrayList<String> lines = new ArrayList<String>();
    File file = new File(filename);
    Scanner scanner = new Scanner(file);
    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        lines.add(line);
      }

    } finally {
      scanner.close();
    }
    return lines;
  }

  /**
   * Demo that this class can read itself
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    String datafile = "src/com/goatwalker/utils/FileReader.java";
    ArrayList<String> lines = readFile(datafile);
    for (String line : lines)
      System.out.println(line);
  }
}
