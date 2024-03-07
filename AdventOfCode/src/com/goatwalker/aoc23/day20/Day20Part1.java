package com.goatwalker.aoc23.day20;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import com.goatwalker.utils.Pair;

/**
 * Puzzle description: https://adventofcode.com/2023/day/20
 * 
 * This puzzle has a list of modules that send hi/lo pulses across a network of
 * connections. There a few types of modules, that change state and transmission
 * behavior according to the puzzle specifications. The puzzle asks for the
 * product of the hi/lo pulse count after a the input button is pushed 1000
 * times.
 * 
 * 
 */
public class Day20Part1 {

//  String datafile = "src/com/goatwalker/aoc23/day20/data/test.txt";
//  String datafile = "src/com/goatwalker/aoc23/day20/data/test2.txt";
  String datafile = "src/com/goatwalker/aoc23/day20/data/input.txt";

  public static void main(String[] args) throws Exception {
    Day20Part1 game = new Day20Part1();
    game.doit();

  }

  /////////////////////////////////////////////////////////////////////////////////

  /**
   * State data: d20net holds the network of module names and module info. The
   * pulseQueue holds all the pulses currently being processed. loCnt & hiCnt
   * accumulate the count of pulses.
   */
  D20Network d20net = new D20Network();
  LinkedList<D20Pulse> d20pulseQueue = new LinkedList<D20Pulse>();
  int loCnt = 0, hiCnt = 0;

  /**
   * Load the data, than push the button 1000 times. Report the product of the lo
   * & hi pulse counts.
   * 
   * @throws Exception for encountering bad state
   */
  private void doit() throws Exception {
    loadData();

//    System.out.println(d20net);

    int pushCnt = 0;
    do {
      pushButton();
      pushCnt++;
      System.out.printf("pushCnt=%d loCnt=%d hiCnt=%d answer=%d\n", pushCnt, loCnt, hiCnt,
          (long) loCnt * hiCnt);
    } while (d20net.getStateValue() != 0 && pushCnt < 1000);

  }

  /////////////////////////////////////////////////////////////////////////////////

  /**
   * Pushing the button involves sending a low pulse to the broadcaster, which
   * initiates a chain of pulses held in the pulse queue. The queue is processed
   * until it's empty.
   * 
   * @throws Exception if pulse throws an exception
   */
  private void pushButton() throws Exception {

//    System.out.println(d20net.showState());

    D20Pulse d20PulseButton = new D20Pulse("broadcaster", false);
    d20pulseQueue.add(d20PulseButton);

    while (!d20pulseQueue.isEmpty()) {
//      printPulseQueue();

      D20Pulse d20Pulse = d20pulseQueue.removeFirst();

      if (d20Pulse.y)
        hiCnt++;
      else
        loCnt++;

//      System.out.printf("  pulse: %s -> %s\n\n", d20Pulse.y ? "hi" : "lo", d20Pulse.x);

      d20net.pulse(d20Pulse);
//      System.out.println(d20net.showState());

    }

  }

  private void printPulseQueue() {
    System.out.print("  PulseQueue: ");
    for (D20Pulse p : d20pulseQueue) {
      System.out.print(p);
    }
    System.out.println();

  }

  /////////////////////////////////////////////////////////////////////////////////

  /**
   * A pulse has a target and a lo/hi value
   */
  class D20Pulse extends Pair<String, Boolean> {
    public D20Pulse(String string, boolean b) {
      super(string, b);
    }
  }

  /////////////////////////////////////////////////////////////////////////////////

  /**
   * The network is an map of module names to D20Module modules.
   * 
   * The core of this puzzle is processing a pulse according to the puzzle rules.
   * The switch statement in pulse() does that work.
   *
   */
  class D20Network extends TreeMap<String, D20Module> {
    private static final long serialVersionUID = 1L;

    /**
     * Process a pulse per spec.
     * 
     * @param pulse the target module, and lo/hi value for pulse type
     * @throws Exception if unknown module name or unknown module type
     */
    public void pulse(D20Pulse pulse) throws Exception {
      String label = pulse.getFirst();
      boolean lohi = pulse.getSecond();

      D20Module mod = get(label);
      if (mod == null)
        throw new Exception("module '" + label + " not found");

      switch (mod.type) {
      case broadcaster:
        // just transmit the lo or hi pulse to all targets
        mod.transmit(label, lohi);
        break;
      case flipflop:
        // if high pulse, do nothing. Otherwise, toggle state, and transmit lo if off or
        // hi if on to all target modules
        if (lohi == false) {
          mod.onOff = !mod.onOff;
          mod.transmit(label, mod.onOff);
        }
        break;
      case conjunction:
        // if all most recent inputs were hi pulses, transmit a lo pulse. otherwise,
        // transmit a high pulse.
        boolean allHigh = true;
        for (boolean b : mod.inputs.values())
          allHigh &= b;
        mod.transmit(label, !allHigh);
        break;
      case none:
        // do nothing
        break;
      default:
        throw new Exception("unknown type '" + mod.type + "'");
      }

    }

    // an abbeviated version of toString()
    public String showState() {
      String out = "  State (state = " + getStateValue() + "):\n";
      for (Entry<String, D20Module> e : entrySet()) {
        D20Module mod = e.getValue();
        out += String.format("    %3s %-14s\n", mod.onOff ? "on" : "off", e.getKey());
      }
      return out;
    }

    @Override
    public String toString() {
      String out = "  Network (state = " + getStateValue() + "):\n";
      for (Entry<String, D20Module> e : entrySet()) {
        D20Module mod = e.getValue();
        out += String.format("    %14s -> %s\n", e.getKey(), mod);
      }
      return out;
    }

    /**
     * There was a chance that a state value was needed, to determine if the network
     * was cycling on states.
     * 
     * @return a long value representing the unique combo of on/off values for all
     *         modules in the network
     */
    public long getStateValue() {
      long state = 0;
      for (D20Module mod : values()) {
        if (mod.onOff)
          state++;
        state = state << 1;
      }
      return state;
    }

  }

  /////////////////////////////////////////////////////////////////////////////////

  /**
   * types of modules
   */
  enum D20Type {
    broadcaster,
    flipflop,
    conjunction,
    none
  };

  /**
   * A module holds on/off state, a type, a list of outputs, and a list of inputs
   * along with the most recent input pulse type (hi/lo).
   *
   */
  class D20Module {

    boolean onOff = false;
    D20Type type = D20Type.none;
    String[] outputs;
    HashMap<String, Boolean> inputs = new HashMap<String, Boolean>();

    /**
     * Send a lo or hi pulse to all the outputs. Also for each of the target
     * modules, update the pulse type on its list of source input modules.
     * 
     * @param input - the source module name
     * @param lohi  - the pulse type (lo or hi)
     */
    public void transmit(String input, boolean lohi) {
      for (String out : outputs) {
        d20pulseQueue.addLast(new D20Pulse(out, lohi));
        d20net.get(out).inputs.put(input, lohi);
      }

    }

    @Override
    public String toString() {
      String out = String.format("mod-type %-13s state=%3s outputs=%-12s", type,
          onOff ? "on" : "off", Arrays.toString(outputs));
      out += " inputs=[ ";
      for (Entry<String, Boolean> entry : inputs.entrySet()) {
        out += entry.getKey() + ":" + (entry.getValue() ? "hi" : "lo") + " ";
      }
      return out + "]";
    }

  }

  /////////////////////////////////////////////////////////////////////////////////

  private void loadData() throws Exception {
    File file = new File(datafile);
    Scanner scanner = new Scanner(file);

    try {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] parts = line.split(" -> ");
        String from = parts[0];
        String[] tos = parts[1].split(", ");
//        System.out.println(from + " -> " + Arrays.toString(tos));

        char firstChar = from.charAt(0);
        boolean special = firstChar == '%' || firstChar == '&';
        if (special) {
          from = from.substring(1);
        }

        D20Module d20mod = d20net.getOrDefault(from, new D20Module());

        if (from.equals("broadcaster")) {
          d20mod.type = D20Type.broadcaster;
        } else if (special) {
          d20mod.type = firstChar == '%' ? D20Type.flipflop : D20Type.conjunction;
        }
        d20mod.outputs = tos;
        d20net.put(from, d20mod);

        for (String to : tos) {
          d20mod = d20net.getOrDefault(to, new D20Module());
          d20mod.inputs.put(from, false);
          d20net.put(to, d20mod);
        }
      }
    } finally {
      scanner.close();
    }

  }
}
