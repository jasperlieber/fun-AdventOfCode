package com.goatwalker.aoc16;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.utils.Logger;

public class Day14Part1
{
//    String root = "cuanljph";
//    String root = "yjdafjpo";
    String root = "abc";
    static final Pattern p3 = Pattern.compile("(.)\\1\\1");

    public static void main(String[] args) throws Exception
    {
        Day14Part1 game = new Day14Part1();
        game.doit();
    }


    private void doit() throws NoSuchAlgorithmException
    {
        Matcher matcher;
        
		int start = 0;
		int found = 0; 
//		char[] password = {'-','-','-','-','-','-','-','-'};
		for (long jj = start; found < 64; jj++)
		{
            String mdf = getMdf(root+jj);
            
            if ((matcher = p3.matcher(mdf)).find())
            {
                String c3 = matcher.group(1);
                //Logger.Log.out(matcher.group(0) + " " + c3 + " " + mdf);

                boolean foundC5 = lookForC5(c3,jj+1);
		        if (foundC5) 
		            found++;
		    }
		}
		System.out.println("done");
	}

    private boolean lookForC5(String c3, long start) throws NoSuchAlgorithmException
    {
        final Pattern p5 = Pattern.compile("(" + c3 + ")\\1\\1\\1\\1");

        for (long jj = start; jj < start + 1000; jj++)
        {
            String mdf = getMdf(root+jj);
            
            if (p5.matcher(mdf).find())
            {
                Logger.Log.out("For " + c3 + c3 + c3 + " on line " + (start - 1)
                        + ", found 5x on line " + jj + " in " + mdf);
                return true;
            }
        }
        return false;
    }

    private static String getMdf(String tst) throws NoSuchAlgorithmException
    {
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(StandardCharsets.UTF_8.encode(tst));
        String res = String.format("%032x", new BigInteger(1, md5.digest()));

        return res;
    }
}