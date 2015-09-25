package com.draco18s.hardlib.api.internal;

public class CropWeatherOffsets {
	public float tempFlat;
	public float rainFlat;
	public int tempTimeOffset;
	public int rainTimeOffset;
	
	/**
	 * @param tf Temperature offset.  Positive is "cold native" negative is "warm native."
	 * @param rf Rain offset.  Positive is "dry native" negative is "wet native."
	 * @param to Temp season offset in ticks.  Typically fractions of a year-length.
	 * @param ro Rain sesaon offset in ticks.  Typically fractions of a year-length.
	 */
	public CropWeatherOffsets(float tf, float rf, int to, int ro) {
		tempFlat = tf;
		rainFlat = rf;
		tempTimeOffset = to;
		rainTimeOffset = ro;
	}
	
	/**
	 * Constructor that creates a duplicate with the same values.
	 * @param orig - the original top copy
	 */
	public CropWeatherOffsets(CropWeatherOffsets orig) {
		tempFlat = orig.tempFlat;
		rainFlat = orig.rainFlat;
		tempTimeOffset = orig.tempTimeOffset;
		rainTimeOffset = orig.rainTimeOffset;
	}
	
	@Override
	public String toString() {
		return ((tempFlat >=0)?"+":"") + tempFlat + "," + ((rainFlat >=0)?"+":"") + rainFlat + "|" + tempTimeOffset + "," + rainTimeOffset;
	}
}
