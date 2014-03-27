package org.cheminfo.scripting.image;

import org.cheminfo.function.Function;
import org.cheminfo.function.scripting.SecureFileManager;
import org.json.JSONObject;

public class IJ extends Function {
	/**
	 * This function load an imageJ. Supported formats: JPEG, JPG,BMP, TIFF, PNG
	 * 
	 * @param basedir
	 * @param key
	 * @param filename
	 * @return and extended ImageJ object
	 */
	public EIJ load(String basedir, String basedirkey, String filename) {
		// If it is a URL we wont check security
		if (filename.trim().matches("^https?://.*$")) {
			return new EIJ(basedir, basedirkey, filename, this);
		} else {
			String fullFilename = SecureFileManager.getValidatedFilename(
					basedir, basedirkey, filename);
			if (fullFilename == null)
				return null;
			return new EIJ(basedir, basedirkey, fullFilename, this);
		}
	}
	
	/**
	 * Returns the Hamming distance between the two hashes
	 */
	public byte hashDistance(long hash1, long hash2){
		long xor = hash1^hash2;
		byte result=0;
		for (int i=0; i<64; i++) {
			if ((xor & 1) == 1) result++;
			xor=xor>>1;
		}
		return result;
	}
 
	/**
	 * This function accepts a String parameter
	 * 
	 * @param args
	 * @return args+", Hello world!"
	 */
	public static String helloWorld(String args) {
		return args + ", Hello World!";
	}


	
	
	/**
	 * This function accepts a JSon parameter. It can be a java json or a
	 * javascript json
	 * 
	 * @param object
	 * @return
	 */
	public String processParams(Object object) {
		JSONObject params = checkParameter(object);
		String name = params.optString("name", "none");
		return "Your name is: " + name;
	}

}
