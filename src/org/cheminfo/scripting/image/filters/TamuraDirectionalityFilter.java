package org.cheminfo.scripting.image.filters;

import ij.ImagePlus;
import ij.gui.NewImage;
import ij.process.Blitter;
import ij.process.ImageProcessor;

public class TamuraDirectionalityFilter {
	private ImageProcessor grayImage;

	public TamuraDirectionalityFilter(ImageProcessor grayImage) {
		this.grayImage = grayImage;
	}

	public byte[] performExtraction() {
		int width = getGrayImage().getWidth();
		int height = getGrayImage().getHeight();

		// Horizontal Results
		ImagePlus iPlusH = NewImage.createFloatImage("Horizontal", width,
				height, 1, NewImage.FILL_BLACK);
		ImageProcessor imgProcH = iPlusH.getProcessor();
		imgProcH.copyBits(getGrayImage(), 0, 0, Blitter.COPY);
		// Vertical Results
		ImagePlus iPlusV = NewImage.createFloatImage("Vertical", width, height,
				1, NewImage.FILL_BLACK);
		ImageProcessor imgProcV = iPlusV.getProcessor();
		imgProcV.copyBits(getGrayImage(), 0, 0, Blitter.COPY);
		// Masks
		/*
		 * int[] maskH = { -1,0,1, -2,0,2, -1,0,1 }; int[] maskV = { 1,2,1,
		 * 0,0,0, -1,-2,-1 };
		 */
		int[] maskH = { -1, 0, 1, -1, 0, 1, -1, 0, 1 };
		int[] maskV = { 1, 1, 1, 0, 0, 0, -1, -1, -1 };

		// Calculate magnitude
		imgProcV.convolve3x3(maskV);
		imgProcH.convolve3x3(maskH);
		/*
		 * iPlusH.show(); iPlusV.show();
		 */

		int offset, i;

		double[] values = new double[width * height];
		float[] deltaV = (float[]) imgProcV.getPixels();
		float[] deltaH = (float[]) imgProcH.getPixels();

		double valueH;
		double valueV;
		double angle = 0;

		for (int y = 1; y < height - 1; y++) {
			offset = y * width;
			for (int x = 1; x < width - 1; x++) {
				i = offset + x;
				valueH = (double) deltaH[i];
				valueV = (double) deltaV[i];

				if (valueH == 0 && valueV == 0)
					angle = 0;
				else if (valueH == 0)
					// angle = 0;
					angle = Math.PI;
				else {
					angle = Math.PI / 2 + Math.atan(valueV / valueH);
				}
				values[i] = angle;
			}
		}
		// Normalize values
		byte[] pixels = new byte[width * height];
		for (i = 0; i < values.length; i++) {
			double num = (values[i] / Math.PI) * 255;
			pixels[i] = (byte) (num);
		}

		return pixels;
	}

	public ImageProcessor getGrayImage() {
		return grayImage;
	}
}
