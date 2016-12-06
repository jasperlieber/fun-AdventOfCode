package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Vector;



public class Day06Part1 {

	static public Vector<HashMap<Character, Integer>> charCountMapArray
		= new Vector<HashMap<Character, Integer>>();

	
	public static void main(String[] args) throws Exception {

		String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//				+ "day06part1_test.txt";
			+ "day06part1.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line = br.readLine();

			while (line != null) {

				processLine(line);
				//System.out.println("read " + line);

				line = br.readLine();
			}
		}
		
		String answer = "";
		for (HashMap<Character,Integer> charCountMap : charCountMapArray)
		{
			int maxCnt = 0;
			char maxChar = '-';
			
			for (HashMap.Entry<Character,Integer> entry : charCountMap.entrySet()) 
			{
			    char key = entry.getKey();
			    int  cnt = entry.getValue();
			    
			    if (cnt > maxCnt)
			    {
			    	maxCnt = cnt;
			    	maxChar = key;
			    }
			}
			
			answer += maxChar;
		}

		System.out.println("answer = " + answer);
	}
	
	static int sectorSum = 0;
	
	public static void processLine(String line) {
		
		for (int jj = 0; jj < line.length(); jj++) {
			
			if (charCountMapArray.size() <= jj)
				charCountMapArray.add(new HashMap<Character, Integer>());
			
			Character cc = line.charAt(jj);
			Integer numOccurrence = charCountMapArray.get(jj).get(cc);
			if (numOccurrence == null) {
				// first count
				charCountMapArray.get(jj).put(cc, 1);
			} else {
				charCountMapArray.get(jj).put(cc, ++numOccurrence);
			}
		}

	}
}
