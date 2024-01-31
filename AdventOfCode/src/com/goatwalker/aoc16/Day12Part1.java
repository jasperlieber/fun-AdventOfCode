package com.goatwalker.aoc16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12Part1
{

    public static void main(String[] args) throws Exception
    {
        Day12Part1 game = new Day12Part1();
        game.doit();
    }

    HashMap<String, Long> registers = new HashMap<String, Long>();
    private int cursor;

    private void doit() throws Exception
    {
        init();
        String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
                 + "day12.txt";
//                + "day12 - Copy.txt";
        // + "day10 - Copy - Copy.txt";
        
        ArrayList<String> lines = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line = br.readLine();
            

            while (line != null)
            {
                lines.add(line);

                //System.out.println("read: " + line);

                line = br.readLine();
            }
        }
        
        cursor = 0;
        while (cursor < lines.size())
        {
            String line = lines.get(cursor);
            //Logger.Log.out(cursor + ": " + line);

            processLine(line);

            cursor++;
            
//            Logger.Log.out("R:" + " " + registers.get("a")
//                    + " " + registers.get("b")
//                    + " " + registers.get("c")
//                    + " " + registers.get("d")
//                    + " " + "cursor = " + cursor);
        }

        for (Map.Entry<String, Long> entry : registers.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + " -> " + value);
        }
    }

    private void init()
    {
        registers.put("a", 0L);
        registers.put("b", 0L);
        registers.put("c", 1L);
        registers.put("d", 0L);
    }

    /*
        cpy 41 a
        inc a
        inc a
        dec a
        jnz a 2
        dec a
     */
    private void processLine(String line) throws Exception
    {
        Matcher matcher;

        final Pattern pCpyVal = Pattern.compile("cpy (\\d+) (\\w)");
        final Pattern pCpyReg = Pattern.compile("cpy (\\w) (\\w)");
        final Pattern pInc = Pattern.compile("inc (\\w)");
        final Pattern pDec = Pattern.compile("dec (\\w)");
        final Pattern pJnzVal = Pattern.compile("jnz (\\d+) (\\d+)");
        final Pattern pJnzReg = Pattern.compile("jnz (\\w) (-?\\d+)");

        if ((matcher = pCpyVal.matcher(line)).find())
        {
            if (matcher.groupCount() != 2) 
                throw new Exception("issue with " + line);
            
            long val = Long.parseLong(matcher.group(1));
            String reg = matcher.group(2);
            
            registers.put(reg,val);
        }
        else if ((matcher = pCpyReg.matcher(line)).find())
        {
            if (matcher.groupCount() != 2) 
                throw new Exception("issue with " + line);
            
            String from = matcher.group(1);
            String reg = matcher.group(2);
            
            Long val = registers.get(from);
            if (val == null)
                throw new Exception("couldn't find register " + from);
            
            registers.put(reg,val);
        }
        else if ((matcher = pInc.matcher(line)).find())
        {
            if (matcher.groupCount() != 1) 
                throw new Exception("issue with " + line);

            String from = matcher.group(1);
            Long val = registers.get(from);
            if (val == null)
                throw new Exception("couldn't find register " + from);
            
            registers.put(from, val+1);
        }
        else if ((matcher = pDec.matcher(line)).find())
        {
            if (matcher.groupCount() != 1) 
                throw new Exception("issue with " + line);

            String from = matcher.group(1);
            Long val = registers.get(from);
            if (val == null)
                throw new Exception("couldn't find register " + from);
            
            registers.put(from, val-1);
        }
        /*
         * jnz x y jumps to an instruction y away (positive means forward; 
         * negative means backward), but only if x is not zero.
         */
        else if ((matcher = pJnzVal.matcher(line)).find())
        {
            if (matcher.groupCount() != 2) 
                throw new Exception("issue with " + line);
            long val = Long.parseLong(matcher.group(1));
            long away = Long.parseLong(matcher.group(2));
            if (val != 0) cursor += away - 1;
        }
        else if ((matcher = pJnzReg.matcher(line)).find())
        {
            if (matcher.groupCount() != 2) 
                throw new Exception("issue with " + line);
            String reg = matcher.group(1);
            long away = Long.parseLong(matcher.group(2));
            
            Long val = registers.get(reg);
            if (val == null)
                throw new Exception("couldn't find register " + reg);
            
            if (val != 0) cursor += away - 1;
        }
        else
            throw new Exception("unknown instruction: " + line);

    }

}
