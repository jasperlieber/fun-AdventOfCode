package com.goatwalker.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexHarness {

  public static void main(String[] args) {
//        Console console = System.console();
//        if (console == null) {
//            System.err.println("No console.");
//            System.exit(1);
//        }
//        
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Printing the file passed in:");
//        while(sc.hasNextLine()) System.out.println(sc.nextLine());
//        while (true) {

//      Pattern pattern = Pattern.compile("rect (\\d+)x(\\d+)");
//      Matcher matcher = pattern.matcher("rect 133x3");

    Pattern pattern = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");
    Matcher matcher = pattern.matcher("0,9 -> 5,9");

    boolean found = false;
    while (matcher.find()) {
      System.out.format(
          "I found the text" + " \"%s\" starting at " + "index %d and ending at index %d.%n",
          matcher.group(), matcher.start(), matcher.end());
      found = true;
      for (int jj = 0; jj <= matcher.groupCount(); jj++)
        System.out.println("Group #" + jj + ": " + matcher.group(jj));
    }
    if (!found) {
      System.out.format("No match found.%n");
    }
//        }
  }
}
