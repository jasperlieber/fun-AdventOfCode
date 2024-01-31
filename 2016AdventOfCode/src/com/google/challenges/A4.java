package com.google.challenges;


public class A4
{
    public static void main(String[] args) throws Exception 
    {
        int[] qs = {22,23};
        int[] ps = answer(5,qs);
        

        for (int ii = 0; ii < qs.length; ii++)
            System.out.println(qs[ii] + " parent is " + ps[ii]);
    }
    
    
    public static int[] answer(int h, int qs[])
    {
        int numqs = qs.length;
        int[] ps = new int[numqs];
        for (int ii = 0; ii < numqs; ii++)
        {
            int q = qs[ii];
            int cur = powerOfTwo(h) - 1;  // initially the root is highest value

            ps[ii] = -1;
            if (q >= cur) continue;
            
            int ht = h;
            
            while (true)
            {
                ht--;
                int left = cur - powerOfTwo(ht);
                int right = cur - 1;
                
                if (q == left || q == right)
                {
                    ps[ii] = cur;
                    break;
                }
                
                cur = q < left ? left : right;
    
            }
        }
        return ps;
    }

    // return 2^pow
    private static int powerOfTwo(int pow)
    {
        return 1<<pow;
    }
}