package com.draco18s.wildlife.util;

import com.draco18s.hardlib.HashUtils;

public class Tree {
	public int x;
	public int y;
	public int z;
	public int approximateAge;
	
	public Tree(int xx, int yy, int zz, int startAge) {
		x = xx;
		y = yy;
		z = zz;
		approximateAge = startAge;
	}
	
	@Override
	public int hashCode() {
		int h = HashUtils.firstTerm(HashUtils.SEED);
		h = HashUtils.hash(h, x);
		h = HashUtils.hash(h, y);
		h = HashUtils.hash(h, z);
		return h;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Tree) {
			Tree ot = (Tree)o;
			return this.x == ot.x && this.y == ot.y && this.z == ot.z;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Tree["+x+","+y+","+z+"] " + approximateAge;
	}
}
