package fr.nekotine.fnafy.commands;

public class IntegerAddOrSet {

	private final int value;
	private final boolean relative;
	
	public IntegerAddOrSet(int v, boolean rel) {
		value = v;
		relative = rel;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isRelative() {
		return relative;
	}
	
}
