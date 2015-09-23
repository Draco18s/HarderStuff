package com.draco18s.hardlib.api.interfaces;

public interface IMechanicalPowerUser {
	//public void setPowerStatus(boolean hasPower);
	public void setPowerLevel(float powerInput);
	public boolean hasPower();
	public float powerScale(int p);
	public int minimumTorque();
	public String getActionString();
}
