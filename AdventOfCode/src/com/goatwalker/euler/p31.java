package com.goatwalker.euler;

public class p31 {

	public static void main(String[] args) {
		
		int[] dd={200,100,50,20,10,5,2,1};
		int[] dmax = new int[8];
		
		String ff = "";
		
		for (int jj = 0; jj < dd.length; jj++)
		{
			dmax[jj] = 200/dd[jj];
			ff += "%3d*" + dd[jj] + " + ";
		}
		ff += " = %d\n";
		
		int cnt = 0;
		int ints = 0;
		
		for (int d0 = 0; d0 <= dmax[0]; d0++)
			for (int d1 = 0; d1 <= dmax[1]; d1++)
				for (int d2 = 0; d2 <= dmax[2]; d2++)
					for (int d3 = 0; d3 <= dmax[3]; d3++)
						for (int d4 = 0; d4 <= dmax[4]; d4++)
							for (int d5 = 0; d5 <= dmax[5]; d5++)
								for (int d6 = 0; d6 <= dmax[6]; d6++)
									for (int d7 = 0; d7 <= dmax[7]; d7++)
									{
										int ss= d0 * dd[0] +
											    d1 * dd[1] +
											    d2 * dd[2] +
											    d3 * dd[3] +
											    d4 * dd[4] +
											    d5 * dd[5] +
											    d6 * dd[6] +
											    d7 * dd[7];

										System.out.format(ff,d0,d1,d2,d3,d4,d5,d6,d7,ss);
										if (ss == 200) cnt++;
										ints++;
									}
		
		System.out.println(cnt );
		System.out.println(ints );
	}

//	private static int FF(int[] dd) {
//		if (dd.length == 1)
//			return 1;
//		int top = 200/dd[0];
//		
//		int[] dd2 = new int[dd.length-1];
//		for (int jj=1; jj < dd.length; jj++)
//			dd2[jj-1] = dd[jj];
//			
//		for (int jj = top; jj >= 0; jj--)
//		{
//			
//		}
//	}

}
