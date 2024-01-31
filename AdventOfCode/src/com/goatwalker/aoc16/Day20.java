package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.utils.Pair;

public class Day20
{

    String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
            + "day20.txt";
//            + "day15_part2.txt";
//            + "day20 - Copy.txt";
    
    public static void main(String[] args) throws Exception
    {
        Day20 game = new Day20();
        game.doit();
    }


    private void doit() throws Exception
    {
        init();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line = br.readLine();

            while (line != null)
            {
                processLine(line);
                // System.out.println("read " + line);

                line = br.readLine();
            }
        }
        
        long nn = 0;
        long curMax = 0;
        
        while(true)
        {
            MyPair next = culls.pollFirst();
            
            System.out.format("testing %d against %s with curMax = %d\n", 
                    nn, next, curMax);
            
            if (nn < next.getFirst()) break;
            
            long second = next.getSecond()+1;
            if (second > curMax) curMax = second;
            nn = curMax;
        }
        System.out.println(nn);  // 15227950 incorrect
    }
    
    Pattern pat;
    
    TreeSet<MyPair> culls = new TreeSet<MyPair>();

    private void init()
    {
        pat = Pattern.compile(
                "(\\d+)-(\\d+)");
    }


    private void processLine(String line) throws Exception
    {
        Matcher matcher;
        if ((matcher = pat.matcher(line)).find())
        {
            if (matcher.groupCount() != 2) 
                throw new Exception("issue with " + line);

            long lo = Long.parseLong(matcher.group(1));
            long hi = Long.parseLong(matcher.group(2));
            
            System.out.format("%d - %d\n", lo, hi);
            
            culls.add(new MyPair(lo,hi));
            
        }
        else
            throw new Exception("unknown pattern: " + line);

    }

    public class MyPair extends Pair<Long, Long> implements Comparable<MyPair>
    {
        public MyPair(Long first, Long second)
        {
            super(first, second);
        }

        @Override
        public int compareTo(MyPair o)
        {
            return (int)Math.signum(getFirst() - o.getFirst());
        }

    }


}
