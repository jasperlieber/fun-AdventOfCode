package com.goatwalker.aoc16;

import java.util.LinkedList;


public class Day19Part2_2
{
    private static final int numElves = 3014387;

    
    
    public static void main(String[] args) throws Exception
    {
        Day19Part2_2 game = new Day19Part2_2();
        game.doit();
    }

    /*
     * Divide the elves into left and right ‘halves’, the right half being
     * bigger if there's an odd number.
     * 
     * Current recipient is the elf at the start of the left half, so the giver
     * to be eliminated from the circle is at the start of the right half.
     * 
     * If the halves are now the same size, that means the right half used to be
     * bigger by one; shift an elf off the start of the right half and push it
     * on the end of the left half, to keep the halves balanced.
     * 
     * That recipient is shifted off the left half and pushed on to the end of
     * the right, so the next elf to the left becomes the recipient for the next
     * time through.
     */

    private void doit() throws Exception
    {
        LinkedList<Integer> ll = new LinkedList<Integer>();
        LinkedList<Integer> rr = new LinkedList<Integer>();
        
        for (int jj = 1; jj <= numElves/2; jj++) ll.add(jj);
        for (int jj = numElves/2 + 1; jj <= numElves; jj++) rr.add(jj);
        
        while (ll.size() > 0)
        {
//            System.out.println("ll =" + display(ll));
//            System.out.println("rr =" + display(rr));

            rr.removeFirst();
            
            if (ll.size() == rr.size())
            {
                int shift = rr.removeFirst();
                ll.addLast(shift);
            }
            
            int shift = ll.removeFirst();
            rr.add(shift);
            
        }
        
        System.out.println("done: " + display(rr));
    }

    private String display(LinkedList<Integer> rr)
    {
        String ss = "";
        for (int jj : rr)
            ss += " " + jj;
        return ss;
    }
}