package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15Part1
{

    String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
//            + "day15.txt";
            + "day15_part2.txt";
//            + "day15 - Copy.txt";
    
    public static void main(String[] args) throws Exception
    {
        Day15Part1 game = new Day15Part1();
        game.doit();
    }

    HashMap<String, Long> registers = new HashMap<String, Long>();

    private void doit() throws Exception
    {
        init();
        
        ArrayList<Long[]> vals = new ArrayList<Long[]>();// = new ArrayList<String>();

//        long maxPosCnt = 0;//, maxDiscNum = 0, 
//        long maxDiscOffset = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line = br.readLine();

            while (line != null)
            {

                Long[] rr = processLine(line);
                // System.out.println("read " + line);
                vals.add(rr);

//                if (rr[1] > maxPosCnt)
//                {
//                    maxPosCnt = rr[1];
//                    maxDiscOffset = rr[3] - rr[2];
//                }

                line = br.readLine();
            }
        }
        boolean found = false;
        long tt;
        for (tt = 0; !found; tt++)
        {
            found = true;
            for (Long[] rr : vals)
            {
                // Disc #2 has 2 positions; at time=0, it is at position 1.
                // Disc #d has n positions; at time=z, it is at position p.
                // p(t) = (t-d+p-z)%n
                long pos = (tt+rr[0]+rr[3]-rr[2]) % rr[1];
//                System.out.format("slots=%d disk=%d ztime=%d offset=%d \n", rr[1],
//                        rr[0], rr[2], rr[3]);
//                System.out.println("Disk #" + rr[0] + " at time " + tt + " in pos " + pos);

                found &= pos == 0;
            }
            if (found) break;
        }
        System.out.println("thru time = " + tt);
            
           
    }

    private void init()
    {
    }

    // Disc #1 has 5 positions; at time=0, it is at position 4.
    // Disc #2 has 2 positions; at time=0, it is at position 1.
    // Disc #d has n positions; at time=z, it is at position p.
    // p(t) = (t-d+p-z)%n
    // p(t) == 0 --> (t+d+p-z)%n == 0 --> n == (t+d+p-z)*m
    // 

    private Long[] processLine(String line) throws Exception
    {
        Matcher matcher;

        final Pattern pat = Pattern.compile(
            "Disc #(\\d+) has (\\d+) positions; at time=(\\d+), it is at position (\\d+).");

        if ((matcher = pat.matcher(line)).find())
        {
            if (matcher.groupCount() != 4) 
                throw new Exception("issue with " + line);

            long nDisc = Long.parseLong(matcher.group(1));
            long nPosCnt = Long.parseLong(matcher.group(2));
            long nTime = Long.parseLong(matcher.group(3));
            long nPos = Long.parseLong(matcher.group(4));
            
            // p(t) = (t + n4 + n1 - n3) % n2
            // p(t) == 0 --> (t+n4+n1-n3)*x = n2*y
            
            return new Long[] {nDisc,nPosCnt,nTime,nPos};
            
        }
        else
            throw new Exception("unknown instruction: " + line);

    }

}
