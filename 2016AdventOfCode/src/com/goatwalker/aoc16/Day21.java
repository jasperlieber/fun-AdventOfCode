package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day21 
{
    static final String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
          + "day21.txt"; 
//          + "day21 - Copy.txt"; 
    
	public static void main(String[] args) throws Exception 
	{
		Day21 game = new Day21();
		game.doit();
	}
	
	StringBuffer text = new StringBuffer("abcdefgh");
	
	private void doit() throws Exception 
	{
		init();
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line = br.readLine();

		    while (line != null) {
		    	
//                System.out.println("line: " + line);
		    	processLine(line);
		        
		        line = br.readLine();
		    }
		}
		
	}
	
	Pattern[] pats;

	private void init() 
	{
	    pats = new Pattern[] {
	            Pattern.compile("swap position (\\d+) with position (\\d+)"),
                Pattern.compile("swap letter ([a-z]) with letter ([a-z])"),
                Pattern.compile("rotate (left|right) (\\d+) steps?"),
                Pattern.compile("rotate based on position of letter ([a-z])"),
                Pattern.compile("reverse positions (\\d+) through (\\d+)"),
                Pattern.compile("move position (\\d+) to position (\\d+)")
	        
	    };
	}

	private void processLine(String line) throws Exception 
	{
        Matcher matcher = null;
        boolean found = false;
        
        for (int jj = 0; jj < pats.length; jj++)
        {
            if (found = (matcher = pats[jj].matcher(line)).find())
            {
                System.out.println("found: " + line);
                switch(jj)
                {
                case 0: doSwapPos(matcher); break;
                case 1: doSwapLetter(matcher); break;
                case 2: doRotateDir(matcher); break;
                case 3: doRotateLetter(matcher); break;
                case 4: doReverse(matcher); break;
                case 5: doMove(matcher); break;
                default: throw new Exception("jj=" + jj + ", no match: " + line);
                }
            }

            if (found)
                break;
        }
        
        if (!found)
            throw new Exception("no match: " + line);
        
        System.out.format("new text: %s\n\n", 
                text);
	}


     /* 
     * move position X to position Y means that the letter which is at index X
     * should be removed from the string, then inserted such that it ends up at
     * index Y.
     */
    private void doMove(Matcher matcher)
    {
        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));
        String nt = text.substring(0,x) + text.substring(x+1);
        nt = nt.substring(0,y) + text.substring(x,x+1) + nt.substring(y);
        text = new StringBuffer(nt);
    }


    /* 
    * reverse positions X through Y means that the span of letters at indexes X
    * through Y (including the letters at X and Y) should be reversed in order.
    */
    private void doReverse(Matcher matcher)
    {
        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));
        StringBuffer rev = new StringBuffer(text.substring(x,y+1));
        rev.reverse();
        String nt = text.substring(0,x) + rev.toString() + text.substring(y+1);
        System.out.format("rev(%d,%d): %s -> %s\n",x,y,text,nt);
        text = new StringBuffer(nt);
    }


    /* rotate based on position of letter X means that the whole string should
    * be rotated to the right based on the index of letter X (counting from 0)
    * as determined before this instruction does any rotations. Once the index
    * is determined, rotate the string to the right one time, plus a number of
    * times equal to that index, plus one additional time if the index was at
    * least 4.
    */ 
    private void doRotateLetter(Matcher matcher) throws Exception
    {
        String lookFor = matcher.group(1);
        int ii = text.indexOf(lookFor);
        if (ii < 0) throw new Exception();
        int rotCnt = 1 + ii + (ii > 3 ? 1 : 0);
        doRotation("right", rotCnt);
    }

    /* 
    * rotate left/right X steps means that the whole string should be rotated;
    * for example, one right rotation would turn abcd into dabc.
    */ 
    private void doRotateDir(Matcher matcher)
    {
        String rotDir = matcher.group(1);
        int rotCnt = Integer.parseInt(matcher.group(2));
        
        doRotation(rotDir, rotCnt);
    }

    private void doRotation(String rotDir, int rotCnt)
    {
        int ptr = rotDir.equals("right")
            ? text.length() - rotCnt 
            : rotCnt;
        
        ptr = (ptr + text.length()) % text.length();
        String nt = text.substring(ptr) + text.substring(0, ptr);

        System.out.format("rotate(%s,%d): %s -> %s\n",
                rotDir,rotCnt,text,nt);
        text = new StringBuffer(nt);
    }


    /*
    * swap letter X with letter Y means that the letters X and Y should be
    * swapped (regardless of where they appear in the string).
    */ 
    private void doSwapLetter(Matcher matcher)
    {
        char xx = matcher.group(1).charAt(0);
        char yy = matcher.group(2).charAt(0);
        
        System.out.print("swap(" + xx + "," + yy + "): " + text + " -> ");
        for (int jj = 0; jj < text.length(); jj++)
            if (text.charAt(jj) == xx)
                text.setCharAt(jj, yy);
            else if (text.charAt(jj) == yy)
                text.setCharAt(jj, xx);
        
        System.out.println(text);
                
    }

    /*
     * swap position X with position Y means that the letters at indexes X and Y
     * (counting from 0) should be swapped.
     */
    private void doSwapPos(Matcher matcher)
    {
        int xx = Integer.parseInt(matcher.group(1));
        int yy = Integer.parseInt(matcher.group(2));
        
        StringBuffer nt = new StringBuffer(text);
        nt.setCharAt(xx, text.charAt(yy));
        nt.setCharAt(yy, text.charAt(xx));
        

        System.out.format("swapPos(%d,%d): %s -> %s\n",
                xx,yy,text,nt);
        text = new StringBuffer(nt);
        
    }
}
