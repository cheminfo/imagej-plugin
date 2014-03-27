package org.cheminfo.scripting.image.extraction;

import ij.ImagePlus;
import ij.plugin.filter.RankFilters;
import ij.process.ImageProcessor;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.cheminfo.scripting.image.EIJ;

public class PillExtraction {

	int nc;
	int nr;
	ImageProcessor ipm[][];
	int mObjects[][];
	float[][] pixels;

	private List<ImageObject> extractObjects2(ImagePlus iip) {
		nc = iip.getWidth();
		nr = iip.getHeight();

		ipm = new ImageProcessor[nc][nr];
		mObjects = new int[nc][nr];
		List<ImageObject> objects = new Vector<ImageObject>();
		Vector<Coordinates> toAnalyze = new Vector<Coordinates>();

		int nObject = 0;

		// loading patches of the generated image (with black background)
		for (int i = 0, y = 0; i < iip.getHeight(); i++, y++) {
			for (int j = 0, x = 0; j < iip.getWidth(); j++, x++) {
				if (pixels[j][i] == 255) { // it is not black
					mObjects[x][y] = -1;
				} else {
					mObjects[x][y] = 0;
				}
			}
		}

		// finding objects
		for (int n = 1; n < nr - 1; n++) {
			for (int m = 1; m < nc - 1; m++) {
				if (mObjects[m][n] == -1) {
					nObject++;
					int x = m;
					int y = n;
					toAnalyze.add(new Coordinates(x, y));
					mObjects[x][y] = nObject;

					while (toAnalyze.size() > 0) {
						int j = toAnalyze.get(0).getX();
						int i = toAnalyze.get(0).getY();

						if (mObjects[j - 1][i] == -1) {
							x = j - 1;
							y = i;
							toAnalyze.add(new Coordinates(x, y));
							mObjects[x][y] = nObject;
						}
						if (mObjects[j + 1][i] == -1) {
							x = j + 1;
							y = i;
							toAnalyze.add(new Coordinates(x, y));
							mObjects[x][y] = nObject;
						}
						if (mObjects[j - 1][i - 1] == -1) {
							x = j - 1;
							y = i - 1;
							toAnalyze.add(new Coordinates(x, y));
							mObjects[x][y] = nObject;
						}
						if (mObjects[j][i - 1] == -1) {
							x = j;
							y = i - 1;
							toAnalyze.add(new Coordinates(x, y));
							mObjects[x][y] = nObject;
						}
						if (mObjects[j + 1][i - 1] == -1) {
							x = j + 1;
							y = i - 1;
							toAnalyze.add(new Coordinates(x, y));
							mObjects[x][y] = nObject;
						}
						if (mObjects[j - 1][i + 1] == -1) {
							x = j - 1;
							y = i + 1;
							toAnalyze.add(new Coordinates(x, y));
							mObjects[x][y] = nObject;
						}
						if (mObjects[j][i + 1] == -1) {
							x = j;
							y = i + 1;
							toAnalyze.add(new Coordinates(x, y));
							mObjects[x][y] = nObject;
						}
						if (mObjects[j + 1][i + 1] == -1) {
							x = j + 1;
							y = i + 1;
							toAnalyze.add(new Coordinates(x, y));
							mObjects[x][y] = nObject;
						}

						toAnalyze.remove(0);
					}
					// printObjects();
				}
			}
		}

		for (int i = 1; i <= nObject; i++) {
			ImagePlus o = cropObject2(iip, i);
			objects.add(new ImageObject(o));
		}

		Collections.sort(objects);
		return objects;

	}

	public ImagePlus removeImpurities(ImagePlus imp) {
		ImagePlus binary = imp.duplicate();
		ImagePlus out = imp.duplicate();
		ImageProcessor ip = binary.getProcessor();
		ImageProcessor ipo = out.getProcessor();
		ip.autoThreshold();

		RankFilters rf = new RankFilters();
		rf.rank(ip, 50.0, RankFilters.OUTLIERS);

		pixels = ip.getFloatArray();

		int width = imp.getWidth();
		int height = imp.getHeight();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (pixels[j][i] == 0) {
					ipo.putPixelValue(j, i, 0);
				}
			}
		}
		out.setTitle(imp.getTitle());

		return out;
	}

	private ImagePlus cropObject2(ImagePlus iip, int nObject) {
		ImageProcessor ip = iip.getProcessor();
		int xs = 0, ys = 0;
		nc = iip.getWidth();
		nr = iip.getHeight();
		int xe = nc;
		int ye = nr;

		boolean ends = false;

		// left
		for (int j = 0; j < nc && !ends; j++) {
			for (int i = 0; i < nr && !ends; i++) {
				if (mObjects[j][i] == nObject) {
					xs = j;
					ends = true;
				}
			}
		}

		ends = false;

		// right
		for (int j = nc - 1; j > 0 && !ends; j--) {
			for (int i = 0; i < nr && !ends; i++) {
				if (mObjects[j][i] == nObject) {
					xe = j;
					ends = true;
				}
			}
		}

		ends = false;

		// top
		for (int i = 0; i < nc && !ends; i++) {
			for (int j = 0; j < nc && !ends; j++) {
				if (mObjects[j][i] == nObject) {
					ys = i;
					ends = true;
				}
			}
		}

		ends = false;

		// down
		for (int i = nr - 1; i > 0 && !ends; i--) {
			for (int j = 0; j < nc && !ends; j++) {
				if (mObjects[j][i] == nObject) {
					ye = i;
					ends = true;
				}
			}
		}

		int width = (xe - xs + 1);
		int height = (ye - ys + 1);

		ip.setRoi(xs, ys, width, height);

		ImageProcessor oip = ip.crop();
		String title = iip.getTitle() + " (object)";
		ImagePlus o = new ImagePlus(title, oip);

		return o;
	}

	public EIJ[] extract2(EIJ imp) {
		ImagePlus oimp = removeImpurities(imp);
		List<ImageObject> objects = extractObjects2(oimp);
		EIJ[] images = new EIJ[objects.size()];
		for (int i = 0; i < objects.size(); i++) {
			EIJ img = imp.duplicate();
			img.setImage(objects.get(i).getObject());
			images[i] = img;
		}
		return images;
	}

}
