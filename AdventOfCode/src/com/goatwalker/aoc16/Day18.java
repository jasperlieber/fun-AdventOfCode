package com.goatwalker.aoc16;


public class Day18
{

    private static final char chTrap = '^';
    private static final char chSafe = '.';
    String input = //".^^.^.^^^^";
            "......^.^^.....^^^^^^^^^...^.^..^^.^^^..^.^..^.^^^.^^^^..^^.^.^.....^^^^^..^..^^^..^^.^.^..^^..^^^..";
//            + "day15.txt";
//            "..^^.";
    
    private int numRows = 400000;
    private int numSafe = 0;

    
    public static void main(String[] args) throws Exception
    {
        Day18 game = new Day18();
        game.doit();
    }

    private void doit() throws Exception
    {
        for (int jj = 0; jj < input.length(); jj++)
            if (input.charAt(jj) == chSafe) numSafe++;
        //System.out.println(input);
        for (int jj = 1; jj < numRows ; jj++)
        {
            input = nextRow(input);
            //System.out.println(input);
        }
        System.out.println(numSafe);
    }

    /*
     * Then, a new tile is a trap only in one of the following situations:
            
            Its left and center tiles are traps, but its right tile is not.
            Its center and right tiles are traps, but its left tile is not.
            Only its left tile is a trap.
            Only its right tile is a trap.

     */
    private String nextRow(String row)
    {
        String r2 = "";
        for (int jj = 0; jj < row.length(); jj++)
        {
            boolean tL = jj != 0 && row.charAt(jj-1) == chTrap;
            boolean tC = row.charAt(jj) == chTrap;
            boolean tR = jj != row.length()-1 && row.charAt(jj+1) == chTrap;
            
            int boolCnt = 0;
            if (tL && tC && !tR) boolCnt++;
            if (tC && tR && !tL) boolCnt++;
            if (tL && !tC && !tR) boolCnt++;
            if (tR && !tC && !tL) boolCnt++;
            
            r2 += boolCnt == 1 ? chTrap : chSafe;
            
            if (boolCnt != 1) numSafe++;
        }
        
        return r2;
        
    }


}