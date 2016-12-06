package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.TreeSet;

import com.goatwalker.utils.CharCount;



public class Day04Part2 {
	
	private static final String[] alpha = {"a","b","c","d","e","f","g","h","i","j","k","l",
			"m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	

	public static void main(String[] args) throws Exception {

		String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//				 + "day4part2_test.txt";
				+ "day4part1.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line = br.readLine();

			while (line != null) {

				processLine(line);
//				 System.out.println("read " + line);

				line = br.readLine();
			}
		}

		 System.out.println("sectorSum = " + sectorSum);
	}
	
	static int sectorSum = 0;
	

	public static void processLine(String line) {
		String[] parts = line.split("[\\[\\]]");
		
		HashMap<Character, Integer> charCountMap 
			= new HashMap<Character, Integer>();

		for (int jj = 0; jj < parts[0].length(); jj++) {
			Character cc = parts[0].charAt(jj);
			if (cc < 'a' || cc > 'z') continue;
			Integer numOccurrence = charCountMap.get(cc);
			if (numOccurrence == null) {
				// first count
				charCountMap.put(cc, 1);
			} else {
				charCountMap.put(cc, ++numOccurrence);
			}
		}
		
		TreeSet<CharCount> tree = new TreeSet<CharCount>();
		
		for (Character character : charCountMap.keySet())
		{
			Integer cnt = charCountMap.get(character);
//			System.out.println("Character " + character + " occurs " 
//					+ cnt + " times");
			
			CharCount cc = new CharCount(character,cnt);
			tree.add(cc);
		}
		
		int jj = 0;
		String encrypt = "";
		for (CharCount cc : tree)
		{
			//System.out.println(jj + ": " + cc);
			encrypt += cc.character;
			if (++jj > 4) break;
		}
		
		boolean match = encrypt.equals(parts[1]);
		System.out.print(line + ": " + encrypt + ", " + match);
		
		if (match) 
		{
			String[] p2 = parts[0].split("-");
			int sector = Integer.parseInt(p2[p2.length-1]);
			sectorSum += sector;
			
			String decrypted = "";
			for (int ii = 0; ii < p2.length-1; ii++)
			{
				if (ii > 0) decrypted += " ";
				for(int kk = 0; kk < p2[ii].length(); kk++)
				{
					char cc = p2[ii].charAt(kk);
					decrypted += "" + alpha[(cc - 'a' + sector) % 26];// + 'a');
				}
			}
			
			
			System.out.println(", decrypted = '" + decrypted + "'");
			
		}
		else
			System.out.println();
		
//		CharCountSet ccSet = new CharCountSet();
//		for()
//		ccSet.add(new CharCount())

		// System.out.println(line + ": " + checkSum);

	}
	

}

//@SuppressWarnings("serial")
//public class CharCountMapElem extends HashMap<Character, Integer> {};




/**/

//	@SuppressWarnings("serial")
//	public class CharCountSet extends TreeSet<CharCount> {
//
//	}


 /**/
