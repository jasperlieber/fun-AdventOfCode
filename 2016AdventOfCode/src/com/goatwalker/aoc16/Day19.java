package com.goatwalker.aoc16;


public class Day19
{
    private static final int numElves = 5;//3014387;

    
    
    public static void main(String[] args) throws Exception
    {
        Day19 game = new Day19();
        game.doit();
    }

    private void doit() throws Exception
    {
        long[] gCnts = new long[numElves];
        
        int elfCnt = numElves;
        
        for (int jj = 0; jj < numElves; jj++) 
            gCnts[jj] = 1;
        
        int cur = 0;
//        int opp = elfCnt / 2;
        do
        {  
//            int opp = cur + elfCnt 
            int next = cur;
            for (int jj = 0; jj < elfCnt / 2; jj++)
            {
                next = findNextNonZero(gCnts,next);
                if (next == cur) break;
            }

            if (next == cur) break;

            gCnts[cur] += gCnts[next];
            gCnts[next] = 0;
            elfCnt--;
            
//            System.out.format("Elf %d steals from elf %d, elfCnt now %d\n",
//                    cur+1,next+1,elfCnt);
            
            int cur2 = findNextNonZero(gCnts,cur);
            if (cur2 == cur) break;
            cur = cur2;
        } while (true);
        
        System.out.format("Done - elf %d has %d gifts\n",
                cur+1,gCnts[cur]);
        
        
    }

    private int findNextNonZero(long[] gCnts, int cur)
    {
        int next = cur;
        while (true) 
        {
            next = next(next);
            if (next == cur || gCnts[next] != 0) break;
        }
        return next;
    }

    private int next(int next)
    {
        return (next + 1) % numElves;
    }


}