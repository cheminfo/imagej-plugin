package org.cheminfo.scripting.image.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.cheminfo.scripting.image.EIJ;
import org.cheminfo.scripting.image.IJ;

public class TestBit {


	public static void main(String[] args) {


		int nb=5;
		
		int mask=255>>(8-nb)<<(8-nb);
		int colorMask=mask<<16 | mask<<8 | mask | 0xFF000000;
		
		int pixel = 0xffffffff;
		
		int slot=((pixel & (mask << 16)) >> (24-3*nb)) |
				((pixel & (mask << 8)) >> (16-2*nb)) |
				((pixel & mask) >> (8-nb));

		System.out.println(Integer.toBinaryString(mask));
		System.out.println(Integer.toBinaryString(colorMask));
		System.out.println(Integer.toBinaryString(slot));
		System.out.println(slot);
		
		
	}

}
