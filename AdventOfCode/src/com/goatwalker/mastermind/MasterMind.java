package com.goatwalker.mastermind;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.utils.Pair;

public class MasterMind {
  private static int numSlots = 4;
  private static int numColors = 6;

  private static final String dashes = "-----------------------------------\n";
//    private static final boolean repeatsAllowed = true;

  public static void main(String[] args) throws Exception {
    MasterMind game = new MasterMind();
    while (game.doit() != "quit")
      ;
    System.out.println("bye bye");
  }

  Puzzle puzzle;
  ArrayList<Pair<String, String>> guesses;
  ArrayList<String> possies;
  private String goal;
  private boolean solved;

  private String doit() throws Exception {
    init();
    // System.out.println(puzzle);

    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String guess;

    while (true) {
      System.out.print(dashes + " guess> ");
      System.out.flush();
      guess = in.readLine();
      if (guess != null && guess.equals("q"))
        return "quit";
      if (guess != null && guess.startsWith("n")) {
        Pattern p = Pattern.compile("n (\\d+) (\\d+)");
        Matcher m = p.matcher(guess);

        if (m.matches()) {
          numSlots = Integer.parseInt(m.group(1));
          numColors = Integer.parseInt(m.group(2));
        }
        return "newGame";
      }
      if ("?".equals(guess)) {
        // System.out.println(dashes + puzzle.showPoss());
        System.out
            .println(dashes + "guesses  hints  possibilities\n" + "-------  -----  -------------");

        int jj = 0;
        for (Pair<String, String> past : guesses)
          System.out.format("%-7s  %-5s  %s\n", past.getFirst(), past.getSecond(),
              possies.get(jj++));
      } else {
        String res = puzzle.process(puzzle.answer, guess);
        if (res == null)
          continue;
        guesses.add(new Pair<String, String>(guess, res));
        String numLeft = puzzle.numLeft();
        possies.add(puzzle.showPoss());

        System.out.println(dashes + "guesses  hints\n-------  -----");
        for (Pair<String, String> past : guesses)
          System.out.format("%-7s  %-5s\n", past.getFirst(), past.getSecond());

        if (res.equals(goal) && !solved) {
          solved = true;
          System.out.println("******* Solved! *******");
        } else
          System.out.println(numLeft);
      }
    }
  }

  private void init() {
    puzzle = new Puzzle();
    guesses = new ArrayList<Pair<String, String>>();
    possies = new ArrayList<String>();
    String poss = String.format("%10.0f", Math.pow(numColors, numSlots)).trim();
    System.out.format("%d slots, %d colors, %s possible answers\n", numSlots, numColors, poss);
    System.out.println("q for quit, n for new, ? for hints");
    solved = false;
    goal = "";
    for (int jj = 0; jj < numSlots; jj++)
      goal += "b";
  }

  private static final String alphabet = "abcdefghijklmnopqrstuvw";

  class Puzzle {
    String answer;

    @Override
    public String toString() {
      String out = "Puzzle = " + answer;
//            for (char c : answer)
//                out += "" + c;
      return out;
    }

    public Puzzle() {
      answer = getPuzzle();
//            for (int jj = 0; jj < numSlots; jj++)
//                answer.add(chars.charAt((int)(Math.random()*numColors)));

//            answer.add('a');
//            answer.add('b');
//            answer.add('b');
    }

    public String process(String answer, String guess) {
      // if (puzzle.toString().equals("Puzzle = " + guess))
      // System.out.println("(match!)");

      if (!valid(guess))
        return null;

      ArrayList<Character> a2 = new ArrayList<Character>();
      ArrayList<Character> g2 = new ArrayList<Character>();

      int nBlack = 0, nWhite = 0;

      // calc nBlack --
      for (int jj = 0; jj < numSlots; jj++) {
        char charAt = guess.charAt(jj);
        if (charAt == answer.charAt(jj))
          nBlack++;
        else {
          a2.add(answer.charAt(jj));
          g2.add(charAt);
        }
      }
      Character cc;
      while (a2.size() > 0 && g2.size() > 0) {
        cc = a2.remove(0);
        int ff = g2.indexOf(cc);
        if (ff < 0)
          continue;
        nWhite++;
        g2.remove(ff);
      }

      String out = "";// guess + ": ";
      for (int kk = 0; kk < nBlack; kk++)
        out += "b";
      for (int kk = 0; kk < nWhite; kk++)
        out += "w";
      // System.out.println(out + " -- " + numLeft());
      return out;
    }

    /**/
    ArrayList<String> possList;

    public String showPoss() {
      String out;
      int numLeft = possList.size();

      out = numLeft + " possibilit" + (numLeft == 1 ? "y" : "ities") + ": ";

      for (int jj = 0; jj < Math.min(numLeft, 6); jj++) {
        out += possList.get(jj) + " ";
      }
      if (numLeft > 6)
        out += "...";

      return out;
    }

    public String numLeft() {
//            long t2, t1 = System.currentTimeMillis();

      if (guesses.size() == 1) {
        possList = new ArrayList<String>();

        for (int jj = 0; jj < (int) Math.pow(numColors, numSlots); jj++) {
          String poss = "";
          int colors = jj;
          for (int kk = 0; kk < numSlots; kk++) {
            int ii = colors % numColors;
            poss += alphabet.charAt(ii);
            colors /= numColors;
          }

          boolean isPoss = false;

          for (Pair<String, String> guess : guesses) {
            isPoss = isPoss(poss, guess);
            if (!isPoss) {
              // System.out.format("%s (#%d) not possible with %s\n",
              // poss, totCnt, guess);
              break;
            }
          }
          if (isPoss) {
//                        System.out.format("  %s possible\n", poss);
            possList.add(poss);
          }
        }
      } else {
        ArrayList<String> possList2 = new ArrayList<String>();
        ;
        for (String poss : possList) {
          boolean isPoss = false;
          for (Pair<String, String> guess : guesses) {
            isPoss = isPoss(poss, guess);
            if (!isPoss)
              break;
          }
          if (isPoss)
            possList2.add(poss);
        }
        possList = possList2;
      }

      int numLeft = possList.size();

      return numLeft + " possibilit" + (numLeft == 1 ? "y" : "ities") + " left";
    }

    private boolean isPoss(String poss, Pair<String, String> guess) {
      String res = process(poss, guess.getFirst());
      return res.equals(guess.getSecond());
    }

    private String getPuzzle() {
      String guess = "";
      for (int jj = 0; jj < numSlots; jj++)
        guess += alphabet.charAt((int) (Math.random() * numColors));
      System.out.println("new = " + guess);
      return guess;
    }

    private boolean valid(String guess) {
      if (guess.length() != numSlots) {
        System.out.format("input should be %d chars\n", numSlots);
        return false;
      }
      for (int jj = 0; jj < numSlots; jj++) {
        char charAt = guess.charAt(jj);
        int pos = alphabet.indexOf(charAt);
        if (pos == -1 || pos >= numColors) {
          System.out.format("input should only contain chars in [%s]\n",
              alphabet.subSequence(0, numColors));
          return false;
        }
      }
      return true;
    }
  }

}