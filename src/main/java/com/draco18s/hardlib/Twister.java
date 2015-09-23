package com.draco18s.hardlib;

public class Twister {
	 private static int[] MT = new int[624];
	 private static int index = 0;
	 
	 public static void initialize_generator(int seed) {
	     index = 0;
	     MT[0] = seed;
	     for(int i = 1; i <= 623; i++) { // loop over each element
	         MT[i] = (1812433253 * (MT[i-1] ^ ((MT[i-1]) >> 30)) + i) & 0xFFFFFFFF;
	     }
	 }
	 
	 public static int extract_number() {
	     if (index == 0) {
	         generate_numbers();
	     }
	 
	     long y = MT[index];
	     y = y ^ (y>>11);
	     y = y ^ ((y>>7) & (2636928640l)); // 0x9d2c5680
	     y = y ^ ((y>>15) & (4022730752l)); // 0xefc60000
	     y = y ^ (y>>18);

	     index = (index + 1) % 624;
	     return (int)y;
	 }
	 
	 static void generate_numbers() {
	     for(int i = 0; i <= 623; i++) {
	         int y = (MT[i] & 0x80000000)                       // bit 31 (32nd bit) of MT[i]
	                        + (MT[(i+1) % 624] & 0x7fffffff);   // bits 0-30 (first 31 bits) of MT[...]
	         MT[i] = MT[(i + 397) % 624] ^ (y>>1);
	         if ((y % 2) != 0) { // y is odd
	             MT[i] = (int) (MT[i] ^ (2567483615l)); // 0x9908b0df
	         }
	     }
	 }
}
