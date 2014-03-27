package org.cheminfo.scripting.image.filters;

import ij.process.ImageProcessor;

/**
 * @author jccaicedo
 */
public class TamuraCoarsenessFilter {
	private int maxWindowSize = 6;
	private ImageProcessor grayImage;

	public TamuraCoarsenessFilter(ImageProcessor grayImage) {
		this.grayImage = grayImage;
	}

	public byte[] performExtraction() {
		int width = getGrayImage().getWidth();
		int height = getGrayImage().getHeight();
		byte[] coarseness = new byte[width * height];
		int margin = 0; // in pixels

		int offset, i;

		for (int y = margin; y < (height - margin); y++) {
			offset = y * width;
			for (int x = margin; x < (width - margin); x++) {
				i = offset + x;
				coarseness[i] = (byte) selectHighestDiff(x, y);
			}
		}

		return coarseness;
	}

	private int average(int x, int y, int windowSize) {
		int limit = (int) Math.pow(2, windowSize);
		int twoKMinusOne = (int) Math.pow(2, windowSize - 1);
		int totalPixels = (int) Math.pow(2, 2 * windowSize);
		int sum = 0;
		int value;

		for (int j = 1; j < limit; j++) {
			for (int i = 1; i < limit; i++) {
				value = getGrayImage().getPixel(x - twoKMinusOne + i,
						y - twoKMinusOne + j);
				sum += value;
			}
		}
		return sum / totalPixels;
	}

	/**
	 * Calculate the difference between the non overlapping neighborhoods of a
	 * given pixel
	 * 
	 * @param x
	 *            X coordinate of pixel
	 * @param y
	 *            Y coordinate of pixel
	 * @param windowSize
	 *            Size of the neighborhood
	 * @param horizontal
	 *            The direction of analysis: Horizontal (true) or Vertical
	 *            (false)
	 * @return
	 */
	private int diffBetweenNeighborhoods(int x, int y, int windowSize,
			boolean horizontal) {
		int value = 0;
		int valueA, valueB;
		int twoKMinusOne = (int) Math.pow(2, windowSize - 1);
		int limit = (int) Math.pow(2, windowSize);

		if (horizontal
				&& !(x + limit > getGrayImage().getWidth() || x - limit < 0)) {
			valueA = average(x + twoKMinusOne, y, windowSize);
			valueB = average(x - twoKMinusOne, y, windowSize);
			value = Math.abs(valueA - valueB);
		}
		if (!horizontal
				&& !(y + limit > getGrayImage().getHeight() || y - limit < 0)) {
			valueA = average(x, y + twoKMinusOne, windowSize);
			valueB = average(x, y - twoKMinusOne, windowSize);
			value = Math.abs(valueA - valueB);
		}
		return value;
	}

	private int selectHighestDiff(int x, int y) {
		int value;
		int maxValue = 0;
		boolean horizontal = true;
		boolean vertical = false;
		int maxK = 0;

		for (int k = 1; k <= maxWindowSize; k++) {
			value = diffBetweenNeighborhoods(x, y, k, horizontal);
			if (value > maxValue) {
				maxValue = value;
				maxK = k;
			}

			value = diffBetweenNeighborhoods(x, y, k, vertical);
			if (value > maxValue) {
				maxValue = value;
				maxK = k;
			}
		}
		int normalizedValue = 0;
		normalizedValue = (maxK) * (255 / maxWindowSize);
		return normalizedValue;
	}

	/**
	 * @param windowSize
	 *            the windowSize to set
	 * @uml.property name="maxWindowSize"
	 */
	public void setMaxWindowSize(int windowSize) {
		this.maxWindowSize = windowSize;
	}

	/**
	 * @return the windowSize
	 * @uml.property name="maxWindowSize"
	 */
	public int getMaxWindowSize() {
		return maxWindowSize;
	}

	public ImageProcessor getGrayImage() {
		return grayImage;
	}
}