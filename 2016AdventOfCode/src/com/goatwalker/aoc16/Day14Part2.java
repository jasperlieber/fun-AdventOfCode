package com.goatwalker.aoc16;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goatwalker.utils.Logger;

public class Day14Part2
{
//     String root = "cuanljph";
     String root = "yjdafjpo";
//    String root = "abc";
    static final Pattern p3 = Pattern.compile("(.)\\1\\1");

    public static void main(String[] args) throws Exception
    {
        Day14Part2 game = new Day14Part2();
        game.doit();
    }

    MessageDigest md5;

    int cursor;
    private MdfCache mdfCache = new MdfCache();

    // int cursor = 0;

    private void doit() throws Exception
    {
        Matcher matcher;

        md5 = MessageDigest.getInstance("MD5");

        int start = 0;
        int found = 0;
        for (cursor = start; found < 64; cursor++)
        {
            String mdf = mdfCache.lookup(cursor);

            if ((matcher = p3.matcher(mdf)).find())
            {
                String c3 = matcher.group(1);
//                Logger.Log.out("triple at " + cursor + ": " + matcher.group(0) + " in " + mdf);

                mdfCache.prepareForSearch(cursor + 1);
//                Logger.Log.out(mdfCache.toString());

                boolean foundC5 = lookForC5(c3, cursor + 1);
                //Logger.Log.out(mdfCache.toString());
                if (foundC5) found++;
            }
        }
        System.out.println("done");
    }

    private boolean lookForC5(String c3, int start) throws Exception
    {
        final Pattern p5 = Pattern.compile("(" + c3 + ")\\1\\1\\1\\1");

        for (int jj = start; jj < start + 1000; jj++)
        {
            String mdf = mdfCache.lookup(jj);

            if (p5.matcher(mdf).find())
            {
                Logger.Log.out("For " + c3 + c3 + c3 + " on line "
                        + (start - 1) + ", found 5x on line " + jj + " in "
                        + mdf);
                return true;
            }
        }
        return false;
    }

    public class MdfCache
    {
        HashMap<Integer,String> mdfHash = new HashMap<Integer,String>();
        
        public MdfCache()
        {
//            for (int jj = 0; jj < maxArraySize; jj++)
//                mdfList.add(null);
        }

//        int listMin = 0;
//        int listMax = 0;
//        
//        int loEnd = 0;
////        int hiEnd = 0;
//
//        private static final int maxArraySize = 1000;
//        ArrayList<String> mdfList = new ArrayList<String>(maxArraySize);

        public String lookup(int index) throws Exception
        {
            String hash = mdfHash.get(index);
            if (hash == null)
            {
                hash = getMdf(root + index);
                mdfHash.put(index, hash);
            }

            return hash;
        }
        
        public void prepareForSearch(int minIndexOfSearch)
        {
            
            if (mdfHash.size() > 10000)
            {
                Set<Integer> cullKeys = new HashSet<Integer>();
                for (int key : mdfHash.keySet())
                    if (key < minIndexOfSearch) 
                        cullKeys.add(key);

                for (int key : cullKeys)
                    mdfHash.remove(key);
            }
        }


        @Override
        public String toString()
        {
            String out = "MdfCache size = " + mdfHash.size();
            return out;
        }

    }

    private String getMdf(String tst) throws NoSuchAlgorithmException
    {
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        //final String start = tst;
        for (int jj = 0; jj <= 2016; jj++)
        {
            md5.update(StandardCharsets.UTF_8.encode(tst));
            tst = String.format("%032x", new BigInteger(1, md5.digest()));
        }
        
        //System.out.format("mdf2016(%s) = %s\n",start,tst);

        return tst;
    }

}