package com.goatwalker.aoc16;

import java.util.HashSet;

import com.goatwalker.utils.Pair;

public class Day01Part2 
{
	public static void main(String[] args) throws Exception 
	{
        //System.out.println("Hello World!"); 
        
        String dirs = "R3, L5, R2, L2, R1, L3, R1, R3, L4, R3, L1, L1, R1, L3, R2, L3, L2, R1, R1, L1, R4, L1, L4, R3, L2, L2, R1, L1, R5, R4, R2, L5, L2, R5, R5, L2, R3, R1, R1, L3, R1, L4, L4, L190, L5, L2, R4, L5, R4, R5, L4, R1, R2, L5, R50, L2, R1, R73, R1, L2, R191, R2, L4, R1, L5, L5, R5, L3, L5, L4, R4, R5, L4, R4, R4, R5, L2, L5, R3, L4, L4, L5, R2, R2, R2, R4, L3, R4, R5, L3, R5, L2, R3, L1, R2, R2, L3, L1, R5, L3, L5, R2, R4, R1, L1, L5, R3, R2, L3, L4, L5, L1, R3, L5, L2, R2, L3, L4, L1, R1, R4, R2, R2, R4, R2, R2, L3, L3, L4, R4, L4, L4, R1, L4, L4, R1, L2, R5, R2, R3, R3, L2, L5, R3, L3, R5, L2, R3, R2, L4, L3, L1, R2, L2, L3, L5, R3, L1, L3, L4, L3";
    
//        dirs = "R5, L5, R5, R3";
//        dirs = "R20, R20, L20, L2";
//        dirs = "R20, R20, L20, L2";
        //dirs = "R8, R4, R4, R8";
        
        String[] steps = dirs.split(",\\s*");
        
        HashSet<Pair<Integer,Integer>> visited = new HashSet<Pair<Integer,Integer>>();
        
        int xx = 0, yy = 0, dir=0;
        
        visited.add(new Pair<Integer,Integer>(0,0));
        
        out: for(String step : steps)
        {
        	//System.out.print('"' + step + '"' + ' ' );
        	char turn = step.charAt(0);
        	if (turn != 'R' && turn != 'L') throw new Exception("r l ");
        	dir += turn == 'R' ? 1 : -1;
        	dir = (dir + 4) % 4;
        	
        	int dist = Integer.parseInt(step.substring(1));
        	
        	for (int jj = 0; jj < dist; jj++)
        	{
            	switch(dir)
            	{
            	case 0:
            		yy += 1;
            		break;
            	case 1:
            		xx += 1;
            		break;
            	case 2:
            		yy -= 1;
            		break;
            	case 3:
            		xx -= 1;
            		break;
            	}
            	
            	Pair<Integer, Integer> pos = new Pair<Integer, Integer>(xx,yy);
            	
            	if (visited.contains(pos))
            	{
            		System.out.println("Second time to " + pos);
            		break out;
            	}
            	else
            	{
            		visited.add(pos);
            	}
        		
        	}
        	
        	System.out.println("After " + Character.toString(turn) + dist 
        			+ ", dir = " + dir + ", pos = (" + xx + ", " + yy + ")" );
        }
        
        System.out.println("pos = (" + xx + ", " + yy + "), dist = " + (Math.abs(xx) + Math.abs(yy)));
        
        
	}
}
