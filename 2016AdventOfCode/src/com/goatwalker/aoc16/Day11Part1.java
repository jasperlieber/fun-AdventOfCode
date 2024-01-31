package com.goatwalker.aoc16;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Day11Part1 
{	
	public class MoveTree {
		
		TreeSet<State> moveTree;

		public MoveTree(State s0) {
			moveTree = new TreeSet<State>();
			addState(s0);
		}

		private void addState(State s0) {
			// TODO Auto-generated method stub
			moveTree.add(s0);
		}

		public void setRoot(State s0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public class Moves {

	}

	public enum Atom {
		PR, CO, CU, RU, PL
	}
	
	public class State implements Comparable<State>
	{
		Floor[] floors = {new Floor(),new Floor(),new Floor(),new Floor()};
		int elevPos=0;
//		Moves moves;
		@Override
		public int compareTo(State o) {
			if (elevPos != o.elevPos) return o.elevPos - elevPos;
			
			return 0;
		}
	}

	public class Floor
	{
		Set<Atom> chips = new HashSet<Atom>();
		Set<Atom> gens = new HashSet<Atom>();
	}
	public static void main(String[] args) throws Exception 
	{
		Day11Part1 game = new Day11Part1();
		game.doit();
	}

	@SuppressWarnings("unused")
  private MoveTree moveTree;

	@SuppressWarnings("unused")
  private void doit() throws Exception {
		init();
		
		while (true)
		{
			Moves nextMoves = getNextMoves();
//			if (finished())
//				break;
		}
	}

	
	private Moves getNextMoves() {
		// TODO Auto-generated method stub
		
		return null;
	}

	private String displayFloors(State ss) {
		String out = "";
		for (int jj = 3; jj >= 0; jj--)
		{
			out += "F" + (jj+1) + (ss.elevPos == jj ? " E  " : " .  ");
			for (Atom gen : ss.floors[jj].gens)
				out += " g" + gen;
			for (Atom chip : ss.floors[jj].chips)
				out += " c" + chip;
			out += "\n";
		}
		return out;
	}


	public static class Log
	{
		static boolean verbose = true;
		public static void out(String string) {
			if (verbose) System.out.println(string);
			
		}
		
	}

	/*
	 * The first floor contains a promethium generator and a
	 * promethium-compatible microchip. The second floor contains a cobalt
	 * generator, a curium generator, a ruthenium generator, and a plutonium
	 * generator. The third floor contains a cobalt-compatible microchip, a
	 * curium-compatible microchip, a ruthenium-compatible microchip, and a
	 * plutonium-compatible microchip. The fourth floor contains nothing
	 * relevant.
	 */
	
	private void init() {
		State s0 = new State();
		s0.floors[0].gens.add(Atom.PR);
		s0.floors[0].chips.add(Atom.PR);
		s0.floors[1].gens.add(Atom.CO);
		s0.floors[1].gens.add(Atom.CU);
		s0.floors[1].gens.add(Atom.RU);
		s0.floors[1].gens.add(Atom.PL);
		s0.floors[2].chips.add(Atom.CO);
		s0.floors[2].chips.add(Atom.CU);
		s0.floors[2].chips.add(Atom.RU);
		s0.floors[2].chips.add(Atom.PL);
		
		Log.out("Init:\n" + displayFloors(s0));
		
		moveTree = new MoveTree(s0);
	}
	

}
