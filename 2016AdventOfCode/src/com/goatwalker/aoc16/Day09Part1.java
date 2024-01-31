package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09Part1 
{
	private static final boolean verbose = true;

	public static void main(String[] args) throws Exception 
	{
		Day09Part1 game = new Day09Part1();
		game.doit();
	}
	
	private void doit() throws Exception {
		init();
		String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//				+ "day09_test.txt";
				+ "day09.txt"; // CDNERYVHKP
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line = br.readLine();
		    
		    while (line != null) {
		    	
		    	processLine(line);
		        //System.out.println("read " + line);
		        
		        line = br.readLine();
		    }
		}
	}

	Pattern pAxB;
	
	private void init() {
		pAxB = Pattern.compile("\\((\\d+)x(\\d+)\\)");
	}

	private void processLine(String line) throws Exception 
	{
		System.out.println("Input:  " + line);
		StringBuffer out = new StringBuffer();
		Matcher matcher;
		boolean notdone = true;
		int pt2 = 0, pt3 = 0;
		
		do
		{
			line = line.substring(pt3);
			if (verbose) System.out.println("substring line = " + line);
			if (notdone = (matcher = pAxB.matcher(line)).find())
			{
				pt2 = matcher.start();
				pt3 = matcher.end();
				if (verbose) System.out.format("I found the text" + " \"%s\" starting at "
						+ "index %d and ending at index %d.%n", matcher.group(),
						pt2, pt3);
	        	if (matcher.groupCount() != 2) 
	        		throw new Exception("matcher cnt not 2, is " + matcher.groupCount());
	        	int patLen = Integer.parseInt(matcher.group(1));
	        	int patRepCnt = Integer.parseInt(matcher.group(2));
	        	
	        	String add = line.substring(0,pt2);
	        	if (verbose) System.out.println("Adding " + add);
	        	out.append(add);

        		add = line.substring(pt3,pt3+patLen);
	        	if (verbose) System.out.println("Adding " + patRepCnt + " copies of " + add);
	        	
	        	for (int jj = 0; jj < patRepCnt; jj++)
	        	{
	        		out.append(add);
	        	}
	        	
	        	pt3 += patLen;
			}
			else
			{
	        	if (verbose) System.out.println("Adding " + line);
	        	out.append(line);
			}
		}
		while (notdone);
		
		System.out.println("Result: " + out.toString() + "\n");
		System.out.println("Len = " + out.toString().length());
	}

}
