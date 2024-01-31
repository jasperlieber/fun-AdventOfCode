package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class Day02Part1 
{
	private static int nextPos(int val, char dir) throws Exception {
		switch (dir)
		{
		case 'U':
			if (val < 4) return val;
			else return val - 3;
			
		case 'D':
			if (val > 6) return val;
			else return val + 3;
			
		case 'L':
			if (val == 1 || val == 4 || val == 7) return val;
			else return val - 1;
			
		case 'R':
			if (val == 3 || val == 6 || val == 9) return val;
			else return val + 1;
			
		default: throw new Exception("bad dir '" + dir + "'");
		}
	}


	public static void main(String[] args) throws Exception 
	{
		Vector<String> lines = new Vector<String>();
		
		String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
			//	+ "day2part1_test.txt";
			  + "day2part1.txt";
		
		int pos = 5;
		
		String code = "";
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line = br.readLine();
		    
		    while (line != null) {
		        lines.add(line);
		        System.out.println("read " + line);
		        
		        for (int jj = 0; jj < line.length(); jj++)
		        {
		        	pos = nextPos(pos,line.charAt(jj));
		        }
		        System.out.println("Pos = " + pos);
		        
		        code += pos;
		        
		        line = br.readLine();
		    }
		}
		
		System.out.println("Final code = " + code);
        
        
        
	}

}
