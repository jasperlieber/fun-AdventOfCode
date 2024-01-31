package com.goatwalker.euler;

import java.math.BigInteger;

public class p57 {

	public static void main(String[] args) 
	{
		BigInteger bbb = new BigInteger("1234567899999999999999999999999999999999");
		bbb = bbb.pow(4);
		
		System.out.println(myLog10(bbb));
		
		testMyLog(bbb);
		testStrlen(bbb);
		
		System.exit(0);
		
		//double aa = 1, bb = 1;
		BigInteger aa = BigInteger.valueOf(1), bb = BigInteger.valueOf(1);
		
		int cnt2=0;
		int cnt=0;
		for (int jj=0; jj < 1001; jj++)
		{
			BigInteger b2 = aa.add(bb);
			BigInteger a2 = b2.add(bb);
			
			int myLog10b2 = myLog10(b2);
			int myLog10a2 = myLog10(a2);
			boolean t1 = myLog10a2
					> myLog10b2;
			if (t1)
			{
				cnt++;
			}
			int numDigitsA2 = numDigits(a2);
			int numDigitsB2 = numDigits(b2);
			boolean t2 = numDigitsA2 > numDigitsB2;
			if (t2) cnt2++;
			
			if (t1 ^ t2)
			{
				System.out.println("diff");
			}

			aa = a2;
			bb = b2;
		}
		System.out.println(cnt);
		System.out.println(cnt2);
		
	}

	private static void testMyLog(BigInteger bb)
    {
	    long start = 0;
		int runs = 100000; // enough to run for 2-10 seconds.
		for(int i=-100000;i<runs;i++) {
		    if(i == 0) start = System.nanoTime();
		    myLog10(bb);
		}
		long time = System.nanoTime() - start;
		System.out.printf("Each myLog10 took an average of %,d ns%n", time/runs);
    }

	private static void testStrlen(BigInteger bb)
    {
	    long start = 0;
		int runs = 100000; // enough to run for 2-10 seconds.
		for(int i=-100000;i<runs;i++) {
		    if(i == 0) start = System.nanoTime();
		    numDigits(bb);
		}
		long time = System.nanoTime() - start;
		System.out.printf("Each numDigits took an average of %,d ns%n", time/runs);
    }

	private static int myLog10(BigInteger val)
    {
		//System.out.print("myLog10("+val.toString()+") = ");
		int log10 = 0;
		for (BigInteger v2 = val;//new BigInteger(val); 
			 v2.compareTo(BigInteger.TEN) >= 0;
			 v2 = v2.divide(BigInteger.TEN))
			log10++;
		//System.out.println(log10);
	    return log10+1;
    }

	private static int numDigits(BigInteger b2)
    {
		int nn = b2.toString().length();
	    return nn;
    }

}
