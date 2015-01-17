package com.draco18s.hardlib;

public class HashUtils {
	public static final int SEED = 23;
	public static final int fODD_PRIME_NUMBER = 37;
	
	public static int hash(int aSeed , int aInt) {
		return firstTerm(aSeed) + aInt;
	}
	
	public static int hash(int aSeed , String aString) {
		return firstTerm(aSeed) + aString.hashCode();
	}

	public static int firstTerm(int aSeed){
		return fODD_PRIME_NUMBER * aSeed;
	}
}
