package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Vector;

public class Day03Part1 
{

	public static void main(String[] args) throws Exception 
	{
		String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//				+ "day2part1_test.txt";
			  + "day3part1.txt";
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line = br.readLine();
		    
		    while (line != null) {
		    	
		    	processLine(line);
		        //System.out.println("read " + line);
		        
		        line = br.readLine();
		    }
		}
		
		System.out.println("Final count = " + poss);
        
        
        
	}
	
	private static int poss = 0;

	private static void processLine(String line) {
        String[] nums = line.trim().split("\\s\\s*");
        int[] tt = {
        		Integer.parseInt(nums[0]),
        		Integer.parseInt(nums[1]),
        		Integer.parseInt(nums[2]) };
        
        Arrays.sort(tt);
        
        if (tt[0] + tt[1] > tt[2]) poss++;
	}

}
