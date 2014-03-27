package org.cheminfo.scripting.image.filters;

import ij.process.ImageProcessor;

public class LocalBinaryPartitionFilter {
	private int width;
	private int height;
	private ImageProcessor grayImage;

	public LocalBinaryPartitionFilter(ImageProcessor grayImage) {
		this.grayImage = grayImage;
	}

	public byte[] performExtraction() {
		width = grayImage.getWidth();
		height = grayImage.getHeight();
		byte[] pixels = (byte[]) grayImage.getPixels();

		byte[] result = new byte[width * height];

		int offset, index;
		for (int y = 1; y < height - 1; y++) {
			offset = y * width;
			for (int x = 1; x < width - 1; x++) {
				index = offset + x;
				result[index] = analyzeTexture(pixels, x, y, index);
			}
		}

		return result;
	}

	/**
	 * It check the neighborhood of a pixel and build a binary string in which
	 * each position has 1 if its value is greater than the pixel and 0 if its
	 * value is lesser than the pixel
	 * 
	 * @param pixels
	 *            The original array of pixels (Gray scale)
	 * @param x
	 *            The x coordinate of the pixel to be analyzed
	 * @param y
	 *            The y coordinate of the pixel to be analyzed
	 * @param index
	 *            The index position of the pixel in the array of pixels
	 * @return
	 */

	public byte analyzeTexture(byte[] pixels, int x, int y, int index) {
		// Take into account boundary conditions...
		int[] neighbors = { x - 1 + width * (y - 1), x + width * (y - 1),
				x + 1 + width * (y - 1), x - 1 + width * (y),
				x + 1 + width * (y), x - 1 + width * (y + 1),
				x + width * (y + 1), x + 1 + width * (y + 1) };

		int localValue = pixels[index];
		String binary = "";
		int code = 0;
		int one = 1;
		int temp;

		for (int pos = 0; pos < neighbors.length; pos++) {
			temp = pixels[neighbors[pos]];
			if (temp > localValue) {
				binary = "1" + binary;
				code += one << pos;
			} else
				binary = "0" + binary;
		}
		return (byte) code;
	}

	/**
	 * Apply a local Binary Partition filter
	 */
	public static void localBinaryPartition(ImageProcessor processor) {
		LocalBinaryPartitionFilter localbinary = new LocalBinaryPartitionFilter(
				processor.convertToByte(true));
		byte[] bytes = localbinary.performExtraction();
		processor.setPixels(bytes);

	}
}
