package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07Part1 {

	static public Vector<HashMap<Character, Integer>> charCountMapArray = new Vector<HashMap<Character, Integer>>();

	public static void main(String[] args) throws Exception {
		
		System.out.println("hi");
		
		pattern = Pattern.compile("\\[[^\\[\\]]*\\]");

		String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//				+ "day07part1_test.txt";
		 + "day7part1.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line = br.readLine();

			while (line != null) {
				boolean supported = processLine(line);
				if (supported) answer++;
				System.out.println("read " + line + ", supported=" + supported);

				line = br.readLine();
			}
		}

		System.out.println("answer = " + answer);
	}

	static int answer = 0;

	static Pattern pattern;

	public static boolean processLine(String line) throws Exception {
		
		System.out.println("Checking " + line);

		Matcher matcher = pattern.matcher(line);
		
		boolean supported = false;
		boolean found = false;
		
		int pt1 = 0;
		
		while (matcher.find()) {
			int pt2 = matcher.start();
			int pt3 = matcher.end();
			System.out.format("I found the text" + " \"%s\" starting at "
					+ "index %d and ending at index %d.%n", matcher.group(),
					pt2, pt3);
			
			if (containsABBA(line.substring(pt2+1,pt3-1)))
				return false;
			
			supported |= containsABBA(line.substring(pt1,pt2));
			pt1 = pt3;
			found = true;
		}
		if (!found) {
			System.out.format("No match found.%n");
			return containsABBA(line);
		}
		
		supported |= containsABBA(line.substring(pt1));
		
		return supported;
		
		
	}
	

	private static boolean containsABBA(String txt) {
		boolean found = false;
		for (int jj = 0; jj < txt.length()-3 && !found; jj++)
		{
			found = txt.charAt(jj) == txt.charAt(jj+3) 
					&& txt.charAt(jj+1) == txt.charAt(jj+2)
					&& txt.charAt(jj) != txt.charAt(jj+1);
			
		}
		System.out.println(txt + " ABBA: " + found);
		return found;
	}
}
