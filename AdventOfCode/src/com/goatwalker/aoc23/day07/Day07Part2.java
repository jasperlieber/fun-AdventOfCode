package com.goatwalker.aoc23.day07;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Day07Part2 {

  String datafile = "2023_data\\d07p1.txt";

  public static void main(String[] args) throws Exception {
    Day07Part2 game = new Day07Part2();
    game.doit();
  }


  private void doit() throws Exception {

    loadData();
    System.out.println("ranked:");

    long answer = 0;
    int cnt = 0;
    for (D7Hand hand : allHands) {
      System.out.println(hand);
      answer += ++cnt * hand.handBid;
    }

    System.out.println("Answer: " + answer);
  }

  private void loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);
    while (scanner.hasNext()) {
      String hand = scanner.next();
      int bid = scanner.nextInt();
      processLine(hand, bid);
    }
    scanner.close();

  }

  TreeSet<D7Hand> allHands = new TreeSet<D7Hand>();

  private void processLine(String handString, int bid) throws Exception {
    D7Hand hand = new D7Hand(handString, bid);
    allHands.add(hand);

  }

  ////////////////////////////////////////////////////////////////

  private final TreeMap<Character, Integer> cardMap = new TreeMap<Character, Integer>();

  private Day07Part2() {
    cardMap.put('A', 14);
    cardMap.put('K', 13);
    cardMap.put('Q', 12);
    cardMap.put('J', 1);
    cardMap.put('T', 10);
    cardMap.put('9', 9);
    cardMap.put('8', 8);
    cardMap.put('7', 7);
    cardMap.put('6', 6);
    cardMap.put('5', 5);
    cardMap.put('4', 4);
    cardMap.put('3', 3);
    cardMap.put('2', 2);
  }

  ////////////////////////////////////////////////////////////////

  private final static int FIVE_KIND = 7;
  private final static int FOUR_KIND = 6;
  private final static int FULL_HOUSE = 5;
  private final static int THREE_KIND = 4;
  private final static int TWO_PAIR = 3;
  private final static int ONE_PAIR = 2;
  private final static int HIGH_CARD = 1;
  private final static int UNKNOWN = 0;

  private final static String[] handTypes = { "UNKNOWN", "HIGH_CARD", "ONE_PAIR", "TWO_PAIR",
      "THREE_KIND", "FULL_HOUSE", "FOUR_KIND", "FIVE_KIND" };

  ////////////////////////////////////////////////////////////////

  private class D7Hand implements Comparable<D7Hand> {

    public int handBid;
    private String handString;
    private int[] cards;
    private int handType = UNKNOWN;
    private long handValue;

    public D7Hand(String hand, int bid) throws Exception {
      handBid = bid;
      handString = hand;
      buildHand();
      determineHandType();
      handValue += handType * 1e10;
    }

    private void buildHand() throws Exception {
      cards = new int[5];
      char[] chars = handString.toCharArray();
      int jj = 0;
      handValue = 0L;
      for (char ch : chars) {
        if (!cardMap.containsKey(ch))
          throw new Exception("bad card symbol '" + ch + "'");
        int cardValue = cardMap.get(ch);
        cards[jj++] = cardValue;
        handValue = handValue * 100L + cardValue;
      }
    }

    @Override
    public int compareTo(D7Hand o) {
      return this.handValue < o.handValue ? -1 : (this.handValue == o.handValue ? 0 : 1);
    }

    @Override
    public String toString() {
      return "hand: " + handString + " " + Arrays.toString(cards) + " " + handValue + " type:"
          + handTypes[handType] + " bid=" + handBid;
    }

    private void determineHandType() throws Exception {
      int[] cnt = new int[15];
      int cPrimary = 0;
      int cSecondary = 0;
      for (int card : cards) {
        int cardCnt = ++cnt[card];
        if (cardCnt == 5) {
          handType = FIVE_KIND;
          break;
        }
        if (cardCnt == 4) {
          handType = FOUR_KIND;
          continue;
        }
        if (cardCnt == 3) {
          if (cPrimary != card) {
            cSecondary = cPrimary;
            cPrimary = card;
          }
          handType = cSecondary != 0 ? FULL_HOUSE : THREE_KIND;
          continue;
        }
        if (cardCnt == 2) {
          switch (handType) {
          case THREE_KIND:
            handType = FULL_HOUSE;
            break;
          case ONE_PAIR:
            handType = TWO_PAIR;
            cSecondary = card;
            break;
          case HIGH_CARD:
            handType = ONE_PAIR;
            cPrimary = card;
            break;
          default:
            throw new Exception("logic problem on card " + card + " with hand " + this);
          }
          continue;
        }
        if (cardCnt == 1) {
          if (handType == UNKNOWN) {
            handType = HIGH_CARD;
          }
        }
      }

      int jackCnt = cnt[1];
      if (jackCnt > 0) {
        switch (handType) {
        case FIVE_KIND:
        case FOUR_KIND:
        case FULL_HOUSE:
          handType = FIVE_KIND;
          break;
        case THREE_KIND:
          handType = FOUR_KIND;
          break;
        case TWO_PAIR:
          handType = jackCnt == 2 ? FOUR_KIND : FULL_HOUSE;
          break;
        case ONE_PAIR:
          handType = THREE_KIND;
          break;
        case HIGH_CARD:
          handType = ONE_PAIR;
          break;
        default:
          throw new Exception("logic!  hand = " + this);
        }
      }
    }
  }
}
