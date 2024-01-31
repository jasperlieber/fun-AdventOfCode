package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07Part2 {

	static public Vector<HashMap<Character, Integer>> charCountMapArray = new Vector<HashMap<Character, Integer>>();

	public static void main(String[] args) throws Exception {
		
		System.out.println("hi");
		
		pattern = Pattern.compile("\\[[^\\[\\]]*\\]");

		String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//				+ "day07part2_test.txt";
		 		+ "day7part1.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line = br.readLine();

			int cnt = 1;
			while (line != null)// && cnt < 20) 
			{
				boolean supported = processLine(line);
				if (supported) answer++;
//				System.out.println(supported);
				System.out.println(cnt++ + ": read " + line + ", supported=" 
						+ supported + "\n");

				line = br.readLine();
			}
		}

		System.out.println("answer = " + answer);
	}

	static int answer = 0;

	static Pattern pattern;
	static HashSet<String> abaSet = new HashSet<String>();
	static HashSet<String> babSet = new HashSet<String>();

	public static boolean processLine(String line) throws Exception {
		
		System.out.println("Checking " + line);

		Matcher matcher = pattern.matcher(line);
		
		HashSet<String> res;
		
		abaSet.clear();
		babSet.clear();
		
		int pt1 = 0;
		
		while (matcher.find()) {
			int pt2 = matcher.start();
			int pt3 = matcher.end();
//			System.out.format("I found the text" + " \"%s\" starting at "
//					+ "index %d and ending at index %d.%n", matcher.group(),
//					pt2, pt3);

			res = getBabList(line.substring(pt2+1,pt3-1),false);
			
			babSet.addAll(res);

			res = getBabList(line.substring(pt1,pt2),true);
			
			abaSet.addAll(res);
			
			pt1 = pt3;
		}
		


		res = getBabList(line.substring(pt1),true);
		
		abaSet.addAll(res);
		
		boolean supported = false;
		
		for(String aba : abaSet)
		{
			for (String bab : babSet)
			{
				supported |= aba.equals(bab);
				
				System.out.println("Comparing " + aba + " with " + bab
						+ ", supported = " + supported);
			}
		}
		
		
		return supported;
	}

	private static HashSet<String> getBabList(String txt, boolean reverse) {
		HashSet<String> res = new HashSet<String>();
		for (int jj = 0; jj < txt.length()-2; jj++)
		{
			char c1 = txt.charAt(jj);
			char c2 = txt.charAt(jj+1);
			char c3 = txt.charAt(jj+2);
			if (c1 == c3 && c1 != c2)
			{
				String add = reverse ? ("" + c2 + c3) : ("" + c1 + c2);
				res.add(add);
				System.out.println((reverse ? "ABA" : "BAB") + ": " + add);
			}
		}
		return res;
	}
}
