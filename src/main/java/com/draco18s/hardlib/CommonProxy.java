package com.draco18s.hardlib;

public class CommonProxy {

	public void throwException() {
		throw new IllegalStateException("HardLib requires COG version 1.2.20 (released Aug 4, 2015) or later.  Unfortunately COG does not a version number in its @mod declaration for a Forge initialization warning.");
	}
}
