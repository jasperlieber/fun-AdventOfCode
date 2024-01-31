package com.goatwalker.aoc16.utils;

public class InstructionDay08 {

	@Override
	public String toString() {
		return "[instruction=" + instruction + ", val1=" + val1
				+ ", val2=" + val2 + "]";
	}
	public enum InstructionEnum { RECT, ROTCOL, ROTROW };

	public InstructionEnum instruction;
	public int val1, val2;
}
