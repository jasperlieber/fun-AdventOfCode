package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class Day02Part2 
{
	/*
    1
  2 3 4
5 6 7 8 9     a=10 b=11 c=12 d=13
  A B C
    D
	 */
	private static char nextPos(char pos, char dir) throws Exception {
//		               1   2   3   4   5   6   7   8   9   A   B   C   D
		final char up[]   = {'1','2','1','4','5','2','3','4','9','6','7','8','B'};
		final char down[] = {'3','6','7','8','5','A','B','C','9','A','D','C','D'};
		final char left[] = {'1','2','2','3','5','5','6','7','8','A','A','B','D'};
		final char rght[] = {'1','3','4','4','6','7','8','9','9','B','C','C','D'};
		
		int val = Integer.parseInt("" + pos, 16) - 1;
		
		System.out.print("Moving in dir " + dir + " from " + pos 
				+ " ( = " + val + ") goes to ");

		switch (dir)
		{
		case 'U':  return up[val];			
		case 'D':  return down[val];		
		case 'L':  return left[val];		
		case 'R':  return rght[val];
		default: throw new Exception("bad dir '" + dir + "'");
		}
	}


	public static void main(String[] args) throws Exception 
	{
		Vector<String> lines = new Vector<String>();
		
		String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//				+ "day2part1_test.txt";
			  + "day2part1.txt";
		
		char pos = '5';
		
		String code = "";
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line = br.readLine();
		    
		    while (line != null) {
		        lines.add(line);
		        //System.out.println("read " + line);
		        
		        for (int jj = 0; jj < line.length(); jj++)
		        {
		        	char dir = line.charAt(jj);
		        	pos = nextPos(pos,dir);
			        System.out.println("Pos = " + pos);
		        }
		        
		        //String hx = String.format("%X",pos);
		        
		        code += pos;
				
				System.out.println("Final code = " + code);
		        
		        line = br.readLine();
		    }
		}
		
		System.out.println("Final code = " + code);
        
        
        
	}

}
