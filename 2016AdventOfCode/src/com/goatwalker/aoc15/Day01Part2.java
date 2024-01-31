package com.goatwalker.aoc15;

import java.io.BufferedReader;
import java.io.FileReader;

//import com.goatwalker.aoc16.day08.InstructionDay08;
//import com.goatwalker.aoc16.day08.InstructionDay08.InstructionEnum;

public class Day01Part2 
{
	public static void main(String[] args) throws Exception 
	{
		Day01Part2 game = new Day01Part2();
		game.doit();
	}
	
	private void doit() throws Exception {
		init();
		// CFLELOYFCS
		String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//				+ "day08part1_test.txt";
//			    + "day08part1.txt";
		+ "2015day1input.txt"; // CDNERYVHKP
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line = br.readLine();
		    
		    while (line != null) {
		    	
		    	processLine(line);
		        System.out.println("read " + line);
		        
		        line = br.readLine();
		    }
		}
		
//		System.out.println("Final count = " + lit);
        
        
	}

	private void init() {
	}

	private void processLine(String line) throws Exception 
	{
	}

}