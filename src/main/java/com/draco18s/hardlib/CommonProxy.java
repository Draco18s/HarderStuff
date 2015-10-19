package com.draco18s.hardlib;

import java.lang.reflect.Method;

public class CommonProxy {
	public Method resourceLocation;
	
	public void init() {
		
	}

	public void throwException() {
		throw new IllegalStateException("HardLib requires COG version 1.2.20 (released Aug 4, 2015) or later.  Unfortunately COG does not a version number in its @mod declaration for a Forge initialization warning.");
	}
}
