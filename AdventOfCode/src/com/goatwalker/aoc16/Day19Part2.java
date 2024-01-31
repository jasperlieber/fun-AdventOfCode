package com.goatwalker.aoc16;

import java.util.ArrayList;


public class Day19Part2
{
    private static final int numElves = 3014387;

    
    
    public static void main(String[] args) throws Exception
    {
        Day19Part2 game = new Day19Part2();
        game.doit();
    }

    private void doit() throws Exception
    {
        ArrayList<int[]> gCnts = new ArrayList<int[]>(numElves);
        
        for (int jj = 0; jj < numElves; jj++) 
        {
            int[] pair = {jj, 1};
            gCnts.add(pair);
        }
        
        int cur = 0;
        do
        {   
            int opp = (cur + gCnts.size() / 2) % gCnts.size();
            
            if (gCnts.size() % 10000 == 0) System.out.println(gCnts.size());
            
//            System.out.format("Elf %d (pos %d) will steal from elf %d (pos %d) -- list = %s\n",
//                    gCnts.get(cur)[0], cur,
//                    gCnts.get(opp)[0], opp,
//                    display(gCnts));
            
            gCnts.get(cur)[1] += gCnts.get(opp)[1];
            
            gCnts.remove(opp);
            
            cur++;
            
            if (cur >= gCnts.size()) cur = 0;

//            cur = cur < (gCnts.size()-1) ? ( cur + 1 ) : 0;
            
        } while (gCnts.size() > 1);
        
        System.out.format("Done - elf %d has %d gifts\n",
                gCnts.get(0)[0]+1, gCnts.get(0)[1]);
        
    }

    @SuppressWarnings("unused")
    private Object display(ArrayList<int[]> gCnts)
    {
        String ss = "elves:";
        for (int[] val : gCnts)
            ss += " (" + val[0] + "," + val[1] + ")";
        return ss;
    }
}