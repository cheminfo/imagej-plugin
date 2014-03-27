/**
 * 
 */
package org.cheminfo.scripting.image.extraction;

import ij.ImagePlus;

/**
 * @author Jorge Camargo
 *
 */
public class ImageObject implements Comparable {
	ImagePlus object;
	int size;
	
	public ImageObject(ImagePlus object) {
		super();
		this.object = object;
		this.size = object.getWidth() * object.getHeight();
	}

	public int getSize() {
		return size;
	}
	
	public ImagePlus getObject() {
		return object;
	}

	public int compareTo(Object in) {
		ImageObject io = (ImageObject)in;
		ImagePlus ip = io.getObject();
		int inSize = ip.getWidth() * ip.getHeight();
		
		if(size < inSize){
			return -1;
		}
		else if (size == inSize){
			return 0;
		}
		else{
			return 1;
		}
	}
	
	
	
}
