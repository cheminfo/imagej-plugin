package org.cheminfo.scripting.image.tests;

import ij.process.ImageStatistics;

import org.cheminfo.scripting.image.EIJ;
import org.cheminfo.scripting.image.IJ;

public class ImageHash {

	public static void main(String[] args){
		// http://www.hackerfactor.com/blog/index.php?/archives/432-Looks-Like-It.html
		
		EIJ image = new EIJ("", "","tests/images/S.png", new IJ());
		
		// Step 1 : convert to greyscale and resize to 64 pixels
		image.grey();
		image.resize("8x8", null);
		
		//Step 2 : Compute mean value of pixels
		ImageStatistics stat = image.getStatistics();
		double mean = stat.mean;
		System.out.println(mean);
		
		//Step 3 : Create the 64 bits array
		byte[] pixels = (byte[])image.getProcessor().getPixels();		
		boolean[] bits = new boolean[pixels.length];
		for(int i=0; i<pixels.length; i++){
			bits[i]=((pixels[i]>=0 && pixels[i]<mean) || (pixels[i]<0 && (255-pixels[i])<mean));
		}
		
		//Step 4 : Construct the hash
		long n = 0, l = bits.length;
		for (int i = 0; i < l; ++i) {
		    n = (n << 1) + (bits[i] ? 1 : 0);
		}
		
		n=0L;
		long long2=-1L;
		long long3=long2 ^ n;
		System.out.println(long3);
		byte result=0;
		for (int i=63; i>=0; i--) {
			if ((long3 & 1) == 1) result++;
			long3=long3>>1;
		}
		
		System.out.println(result);
	}
}
