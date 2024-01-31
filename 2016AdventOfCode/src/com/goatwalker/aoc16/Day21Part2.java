package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day21Part2 
{
    static final String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
          + "day21.txt"; 
//          + "day21 - Copy.txt"; 
    
	public static void main(String[] args) throws Exception 
	{
		Day21Part2 game = new Day21Part2();
		game.undoit();
	}
	
	StringBuffer text = new StringBuffer("fbgdceah");
	
	private void undoit() throws Exception 
	{
		init();
		LinkedList<String> lines = new LinkedList<String>();
        String line;
        
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) 
        {
            while ((line = br.readLine()) != null) 
            {
                lines.addFirst(line);
            }
        }
        
        while ((line = lines.pollFirst()) != null)
        {
	    	processLine(line);
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
                case 0: undoSwapPos(matcher); break;
                case 1: undoSwapLetter(matcher); break;
                case 2: undoRotateDir(matcher); break;
                case 3: undoRotateLetter(matcher); break;
                case 4: undoReverse(matcher); break;
                case 5: undoMove(matcher); break;
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
        
        // fhbgaecd, dbgfahec wrong
	}


     /* 
     * move position X to position Y means that the letter which is at index X
     * should be removed from the string, then inserted such that it ends up at
     * index Y.
     */
    private void undoMove(Matcher matcher)
    {
        int y = Integer.parseInt(matcher.group(1));
        int x = Integer.parseInt(matcher.group(2));
        String nt = text.substring(0,x) + text.substring(x+1);
        nt = nt.substring(0,y) + text.substring(x,x+1) + nt.substring(y);
        System.out.format("move(%d,%d): %s -> %s\n",x,y,text,nt);
        text = new StringBuffer(nt);
    }


    /* 
    * reverse positions X through Y means that the span of letters at indexes X
    * through Y (including the letters at X and Y) should be reversed in order.
    */
    private void undoReverse(Matcher matcher)
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
    * as determined before this instruction undoes any rotations. Once the index
    * is determined, rotate the string to the right one time, plus a number of
    * times equal to that index, plus one additional time if the index was at
    * least 4.
    * 
    * let L = length of string.  example rot(a) with abcdefgh (L = 8)
    *    forward:  0-1 1-3 2-5 3-7 4-10/2 5-12/4 6-14/6 7-16/0 
    *    ends in   1   3   5   7   2      4      6      0
    *              
    *    undo      0 1 2 3 4 5 6 7
    *    goal =   {7,0,4,1,5,2,6,3}
    *    rotRight  7,7,2,6,1,5,0,4
    * undo X -- if X in pos 0 --> rot % L = 1 --> rot - 6 = L + 1 --> rot = L + 7
    */ 
    private void undoRotateLetter(Matcher matcher) throws Exception
    {                    // 0 1 2 3 4 5 6 7
        final int[] undo = {7,7,2,6,1,5,0,4};
        String lookFor = matcher.group(1);
        int ii = text.indexOf(lookFor);
        if (ii < 0) throw new Exception();
        int rotCnt = undo[ii];
        undoRotation("left", rotCnt);
    }

    /* 
    * rotate left/right X steps means that the whole string should be rotated;
    * for example, one right rotation would turn abcd into dabc.
    */ 
    private void undoRotateDir(Matcher matcher)
    {
        String rotDir = matcher.group(1);
        int rotCnt = Integer.parseInt(matcher.group(2));
        
        undoRotation(rotDir, rotCnt);
    }

    private void undoRotation(String rotDir, int rotCnt)
    {
        int ptr = rotDir.equals("left") //"right")
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
    private void undoSwapLetter(Matcher matcher)
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
    private void undoSwapPos(Matcher matcher)
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
