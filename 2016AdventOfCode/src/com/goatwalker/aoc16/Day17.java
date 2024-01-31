package com.goatwalker.aoc16;


public class Day17
{

//    String filename = "C:\\Users\\jasper\\Google Drive\\Fun\\2016AdventOfCode\\EclipseWorkspace\\"
////            + "day15.txt";
//            + "day15_part2.txt";
////            + "day15 - Copy.txt";
    
    public static void main(String[] args) throws Exception
    {
        Day17 game = new Day17();
        game.doit();
    }

    private void doit() throws Exception
    {
        StringBuffer input = new StringBuffer("01110110101001000");
//        StringBuffer input = new StringBuffer("111");
        int fillLength = 35651584;
        
        do
        {
            StringBuffer bb = new StringBuffer(input);
            input.append("0").append(toggle(bb.reverse()));
//            System.out.println(input);
        } while (input.length() < fillLength);
        
//        System.out.println(input);
        
        input.delete(fillLength,input.length());
            
//        System.out.println(input);
        
        while (input.length() % 2 == 0)
        {
            StringBuffer i2 = new StringBuffer();
            for(int jj = 0; jj < input.length(); jj += 2)
            {
                char c1 = input.charAt(jj);
                char c2 = input.charAt(jj+1);
                i2.append(c1==c2 ? '1' : '0');
            }
            input = i2;
//            System.out.println(input);
        }
        System.out.println(input);

    }

    StringBuffer toggle(StringBuffer sb)
    {
        for (int jj = 0; jj < sb.length(); jj++)
            sb.setCharAt(jj, 
                    sb.charAt(jj) == '0' ? '1' : '0');
        return sb;
    }


}