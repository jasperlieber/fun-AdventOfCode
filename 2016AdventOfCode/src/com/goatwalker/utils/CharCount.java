package com.goatwalker.utils;

//import com.goatwalker.aoc16.Day04Part1;
//import com.goatwalker.aoc16.Day04Part1.CharCount;

public class CharCount extends Object implements Comparable<CharCount> {
	
	@Override
	public String toString() {
		return "[char=" + character + ", cnt=" + cnt + "]";
	}

	public CharCount(char cc, int cnt) {
		super();
		this.character = cc;
		this.cnt = cnt;
	}

	public char character;
	private int cnt;

	@Override
	public int compareTo(CharCount o) {
		if (cnt > o.cnt)
			return -1;
		if (cnt < o.cnt)
			return 1;
		return Integer.signum(character - o.character);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + character;
		result = prime * result + cnt;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharCount other = (CharCount) obj;
		if (character != other.character)
			return false;
		if (cnt != other.cnt)
			return false;
		return true;
	}

}
