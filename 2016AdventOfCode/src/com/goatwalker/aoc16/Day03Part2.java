package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Vector;

public class Day03Part2 
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
		
		col1.addAll(col2);
		col1.addAll(col3);
		
		for (int jj = 0; jj < col1.size(); jj+=3)
		{

            int[] tt = {
            		(col1.elementAt(jj)),
            		(col1.elementAt(jj+1)),
            		(col1.elementAt(jj+2)) };       	

            
            Arrays.sort(tt);
            
            if (tt[0] + tt[1] > tt[2]) poss++;
        }
		
		
		System.out.println("Final count = " + poss);
        
        
        
	}
	
	private static int poss = 0;
//	private static Vector<Integer>[] cols;
	
	private static Vector<Integer> col1 = new Vector<Integer>();
	private static Vector<Integer> col2 = new Vector<Integer>();
	private static Vector<Integer> col3 = new Vector<Integer>();

	private static void processLine(String line) {
        String[] nums = line.trim().split("\\s\\s*");
        
        col1.add(Integer.parseInt(nums[0]));
        col2.add(Integer.parseInt(nums[1]));
        col3.add(Integer.parseInt(nums[2]));
        
	}

}
