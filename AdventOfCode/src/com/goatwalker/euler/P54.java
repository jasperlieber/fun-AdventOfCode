package com.goatwalker.euler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class P54 
{

    static final String filename = //"C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
            "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\2016AdventOfCode\\src\\com\\goatwalker\\euler"
//          + "day08part1_test.txt";
//          + "day08part1.txt";
            + "p054_poker.txt"; 
    
	public static void main(String[] args) throws Exception 
	{
		P54 game = new P54();
		game.doit();
	}
	
	
	private void doit() throws Exception 
	{
		init();
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line;

		    while ((line = br.readLine()) != null) {
		    	
                System.out.println("line: " + line);
		    }
        }
	}
	
	Pattern pat;

	private void init() 
	{
	    // Filesystem              Size  Used  Avail  Use%
	    // /dev/grid/node-x0-y0     94T   67T    27T   71%
        pat = Pattern.compile("(\\w\\s){10}");
	}

	@SuppressWarnings("unused")
  private boolean processLine(String line) throws Exception 
	{
        Matcher matcher = null;
        boolean found = false;
        
        found = (matcher = pat.matcher(line)).find();
            //System.out.println("found: " + line);
//        else
//            System.out.println("not found: " + line);
        
        if (!found) throw new Exception("problem with " + line);
        
        
        
        return true;
        
//        System.out.println(node);
	}
}
