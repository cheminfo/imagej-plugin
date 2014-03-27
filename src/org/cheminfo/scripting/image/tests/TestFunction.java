package org.cheminfo.scripting.image.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.cheminfo.scripting.image.EIJ;
import org.cheminfo.scripting.image.IJ;

public class TestFunction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// You can run this method for testing the behavior of you functions
		// inside java.
		// You don't need to build the plugin(.jar) for testing it.
		// dsd
		// It has to print "Castillo, Hello World!"
		System.out.println(IJ.helloWorld("Castillo"));
		EIJ image = new EIJ("", "",
				"/usr/local/script/data/lpatiny/data/plant8.jpg", new IJ());
		int[] hist = image.histogram();
		for (int i = 0; i < hist.length; i++)
			System.out.print(hist[i] + " ");
	}

	static public String getContents(File aFile) {
		// ...checks on aFile are elided
		StringBuilder contents = new StringBuilder();

		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null; // not declared within while loop
				/*
				 * readLine is a bit quirky : it returns the content of a line
				 * MINUS the newline. it returns null only for the END of the
				 * stream. it returns an empty String if two newlines appear in
				 * a row.
				 */
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return contents.toString();
	}

}
