package org.cheminfo.scripting.image.filters;

import ij.process.ImageProcessor;

import java.awt.Rectangle;

public class TamuraContrastFilter {
	private ImageProcessor grayImage;
	int margin = 6; // in pixels

	public TamuraContrastFilter(ImageProcessor grayImage) {
		this.grayImage = grayImage;
	}

	public byte[] performExtraction() {
		int width = getGrayImage().getWidth();
		int height = getGrayImage().getHeight();
		double[] contrastArray = new double[width * height];
		byte[] pixels = new byte[width * height];

		int offset, i;
		double max = 0, min = 256;
		Rectangle r = new Rectangle();

		for (int y = 0; y < (height - 0); y++) {
			offset = y * width;
			for (int x = 0; x < (width - 0); x++) {
				i = offset + x;
				configRect(r, x, y, margin, width, height);
				contrastArray[i] += calculateContrast(r);
				if (contrastArray[i] > max)
					max = contrastArray[i];
				if (contrastArray[i] < min)
					min = contrastArray[i];
			}
		}
		// Normalize contrast values
		double longInt = max - min;
		for (i = 0; i < contrastArray.length; i++) {
			double num = ((contrastArray[i] - min) / longInt) * 255;
			pixels[i] = (byte) (num);
		}

		return pixels;
	}

	private double calculateContrast(Rectangle r) {
		double mean = 0.0;
		int value;

		// Compute mean
		for (int y = r.y; y < (r.y + r.height); y++) {
			for (int x = r.x; x < (r.x + r.width); x++) {
				value = getGrayImage().getPixel(x, y);
				mean += value;
			}
		}
		mean = mean / (r.width * r.height);
		// Calculate standard deviation
		double stddev = 0.0;
		for (int y = r.y; y < (r.y + r.height); y++) {
			for (int x = r.x; x < (r.x + r.width); x++) {
				value = getGrayImage().getPixel(x, y);
				// stddev += Math.pow(value - mean , 2);
				double tmp = value - mean;
				stddev += tmp * tmp;
			}
		}
		stddev = Math.sqrt(stddev / (r.width * r.height));
		// Calculate fourth momentum around the mean
		double fourthMean = 0.0;
		for (int y = r.y; y < (r.y + r.height); y++) {
			for (int x = r.x; x < (r.x + r.width); x++) {
				value = getGrayImage().getPixel(x, y);
				// fourthMean += Math.pow(value - mean , 4);
				double tmp = value - mean;
				tmp = tmp * tmp; // tmp^2
				fourthMean += tmp * tmp; // tmp^4
			}
		}
		fourthMean = fourthMean / (r.width * r.height);
		// Now, calculate the contrast
		double contrast = 0.0;
		// contrast = Math.pow(stddev,2) / ( Math.pow( fourthMean, 0.25 ) );
		contrast = (stddev * stddev) / (Math.pow(fourthMean, 0.25));
		return contrast;
	}

	private void configRect(Rectangle r, int x, int y, int margin, int width,
			int height) {
		int xc = x - margin;
		int yc = y - margin;
		int wc = 2 * margin + 1;
		int hc = 2 * margin + 1;

		if (xc < 0)
			xc = 0;
		if (x + margin + 1 > width)
			wc = margin + width - x;
		if (yc < 0)
			yc = 0;
		if (y + margin + 1 > height)
			hc = margin + height - y;

		r.setBounds(xc, yc, wc, hc);
	}

	public ImageProcessor getGrayImage() {
		return grayImage;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

}
