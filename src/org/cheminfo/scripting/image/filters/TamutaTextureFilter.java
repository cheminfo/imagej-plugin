package org.cheminfo.scripting.image.filters;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.NewImage;

import org.cheminfo.scripting.image.EIJ;

public class TamutaTextureFilter {
	/**
	 * Apply a tamura filter
	 */
	public static void tamura(EIJ eij) {
		/* Tamura Coarseness */

		TamuraCoarsenessFilter tamuraCoarseness = new TamuraCoarsenessFilter(
				eij.getProcessor().convertToByte(true));
		byte[] coarseness = tamuraCoarseness.performExtraction();

		ImagePlus iCoarseness = NewImage.createByteImage("Coarseness", eij.getWidth(),
				eij.getHeight(), 1, NewImage.FILL_BLACK);
		iCoarseness.getProcessor().setPixels(coarseness);

		/* Tamura Contrast */

		TamuraContrastFilter tamuraContrast = new TamuraContrastFilter(eij
				.getProcessor().convertToByte(true));
		byte[] contrast = tamuraContrast.performExtraction();

		ImagePlus iContrast = NewImage.createByteImage("Contrast", eij.getWidth(),
				eij.getHeight(), 1, NewImage.FILL_BLACK);
		iContrast.getProcessor().setPixels(contrast);

		TamuraDirectionalityFilter tamuraDirectionality = new TamuraDirectionalityFilter(
				eij.getProcessor().convertToByte(true));
		byte[] directionality = tamuraDirectionality.performExtraction();

		ImagePlus iDirectionality = NewImage.createByteImage("Directionality",
				eij.getWidth(), eij.getHeight(), 1, NewImage.FILL_BLACK);
		iDirectionality.getProcessor().setPixels(directionality);

		ImageStack stack1 = new ImageStack(eij.getWidth(), eij.getHeight());
		stack1.addSlice("directionality", iDirectionality.getProcessor());

		ImageStack stack2 = new ImageStack(eij.getWidth(), eij.getHeight());
		stack2.addSlice("coarseness", iCoarseness.getProcessor());

		ImageStack stack3 = new ImageStack(eij.getWidth(), eij.getHeight());
		stack3.addSlice("contrast", iContrast.getProcessor());

		ij.plugin.RGBStackMerge merge = new ij.plugin.RGBStackMerge();
		ImageStack result = merge.mergeStacks(eij.getWidth(), eij.getHeight(), 1, stack1, stack2,
				stack3, true);
		int[] pixels = (int[]) result.getPixels(1);
		eij.getProcessor().setPixels(pixels);
		eij.grey();
	}
}
