package fr.nekotine.fnafy.commands;

public class DoubleAddOrSet {
	
	private final double value;
	private final boolean relative;
	
	public DoubleAddOrSet(double v, boolean rel) {
		value = v;
		relative = rel;
	}
	
	public boolean isRelative() {
		return relative;
	}
	
	public double getValue() {
		return value;
	}
	
	public double getRelativeValue(double baseValue) {
		return baseValue+value;
	}
}