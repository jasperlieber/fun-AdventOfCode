package com.goatwalker.aoc16;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Day05Part2 {
	
	public static void main(String[] args) throws Exception {
		
		String root = "abbhdwsy";
//		String root = "abc";
		
		int start = 0;//3231928; // 0;
		int found = 0; 
		char[] password = {'-','-','-','-','-','-','-','-'};
		for (long jj = start; found < 8; jj++)
		{
			String tst = root + jj;
			String md5 = getMdf(tst);
			if (md5.substring(0, 5).equals("00000"))
			{
				int position = Integer.parseInt(md5.substring(5,6),16);
				if (position < 8 && password[position] == '-')
				{
					found++;
					password[position] = md5.charAt(6);
					System.out.println(vcat(password));
				}
			}
		}
		
		System.out.println(vcat(password));
		
//		String yourString = "abc3231929";
//		String yourString = "abc5017308";
		
		
		
		
	}

	private static String vcat(char[] password) {
		// TODO Auto-generated method stub
		String ss = "";
		for (char s : password)
			ss += s;
		return ss;
	}

	private static String getMdf(String tst) throws NoSuchAlgorithmException {
		final MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(tst));
		String res = String.format("%032x", new BigInteger(1, md5.digest()));
		
		return res;
	}
}