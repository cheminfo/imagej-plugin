package org.cheminfo.scripting.image.filters;

import ij.process.ImageProcessor;

public class InvariantFeatureHistogramFilter {
	private ImageProcessor grayImage;

	public InvariantFeatureHistogramFilter(ImageProcessor grayImage) {
		this.grayImage = grayImage;
	}

	public byte[] performExtraction() {
		int width = grayImage.getWidth();
		int height = grayImage.getHeight();
		int margin = 0;
		int offset, i;
		double[] invariant = new double[height * width];
		double max = 0, min = 255;

		for (int y = margin; y < (height - margin); y++) {
			offset = y * width;
			for (int x = margin; x < (width - margin); x++) {
				i = offset + x;
				invariant[i] = localSupport(x, y);
				if (invariant[i] > max)
					max = invariant[i];
				if (invariant[i] < min)
					min = invariant[i];
			}
		}

		// Normalize values
		byte[] pixels = new byte[height * width];
		double interval = max - min;
		for (i = 0; i < invariant.length; i++) {
			double num = ((invariant[i] - min) / interval) * 255;
			pixels[i] = (byte) (num);
		}

		return pixels;
	}

	public double localSupport(int x, int y) {
		double sum = 0.0;

		int round = 16;
		int total = 0;
		for (int r = 0; r < round; r++) {
			double phi = 2 * Math.PI * (double) r / (double) round;
			int a = 0, b = 0;
			int x1 = (int) (4 * Math.cos(phi) + x);
			int y1 = (int) (4 * Math.sin(phi) + y);
			int x2 = (int) (-8 * Math.sin(phi) + x);
			int y2 = (int) (8 * Math.cos(phi) + y);

			if (x1 >= 0 && x1 < grayImage.getWidth() && y1 >= 0
					&& y1 < grayImage.getHeight()) {
				a = grayImage.getPixel(x1, y1);
				total++;
			}

			if (x2 >= 0 && x2 < grayImage.getWidth() && y2 >= 0
					&& y2 < grayImage.getHeight()) {
				b = grayImage.getPixel(x2, y2);
				total++;
			}

			sum += Math.sqrt((double) a * (double) b);
		}
		double average = sum / (double) total;
		return average;
	}

	/**
	 * Apply a invariant feature filter
	 */
	public static void invariantFeatureHistogram(ImageProcessor processor) {
		InvariantFeatureHistogramFilter invariantTexture = new InvariantFeatureHistogramFilter(
				processor.convertToByte(true));
		byte[] bytes = invariantTexture.performExtraction();
		processor.setPixels(bytes);
	}
}
