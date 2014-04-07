/**
 * @object IJ
 * Library that provides methods for image manipulation.
 * @constructor
 * Load a new image
 * @param	filename:string	The path of the image
 * @return	+IJ
 */
var IJ = function (newEIJ) {
	this.EIJ=newEIJ;
};

/**
 * @function load(filename)
 * This function loads and returns an ImageJ image.
 * @param	filename:string	The path of the image
 * @return	+IJ	The loaded image
 * 
 */
IJ.load=function(filename) {
	return new IJ(IJAPI.load(Global.basedir, Global.basedirkey, File.checkGlobal(filename)));
};

/**
 * @function loadBase64(data)
 * Load an image from its base64-encoded string
 * @param	data:string	Base64 representation of the image
 * @return	+IJ	The loaded image
 */
IJ.loadBase64=function(data) {
	if(data.startsWith("data:")) {
		data = data.replace(/^data:.*,/,"");
	}
	return new IJ(IJAPI.loadBase64(Global.basedir, Global.basedirkey, data));
}

/**
 * @function hashDistance(hash1, hash2)
 * Returns the Hamming distance between the two hashes
 * @param 	hash1:number	The first hash
 * @param 	hash2:number	The second hash
 * @return	number			An integer between 0 and 64
 */
IJ.hashDistance=function(hash1,hash2) {
	return IJAPI.hashDistance(hash1,hash2);
};

/**
 * @object IJ.prototype
 * Methods of the IJ object
 */
IJ.prototype = {
		/**
		 * @function		save(path, options)
		 * Saves the given image in the format specified by the extension of the
		 * path. The allowed extensions are: tif, jpg, gif, txt, zip, raw, bmp, ftis, png and pgm
		 * In the options you can specify the quality of the resulting image.
		 * @param 		path:string		physical path in which to save the image
		 * @param		options:+Object	Object containing the options
		 * @option		quality 	Quality for jpeg image, possible values between 0 and 100 (Default 100)
		 * @return 		bool		If it succeeded saving or not
		 */
		save: function(path, options) {
			return this.EIJ.save(File.checkGlobal(path), options);
		},

		/**
		 * @function		saveTransparentPng(path, mask)
		 * Saves the given image as a transparent png
		 * Transparency is based on the mask and the threshold specified in it
		 * @param 		path:string		physical path in which to save the image
		 * @param		mask:+IJ 		Image containing the mask
		 */
		saveTransparentPng: function(path, mask) {
			this.EIJ.saveTransparentPng(File.checkGlobal(path), mask);
		},


		/**
		 * @function		resize(size, options)
		 * Scales the image to the specified width and height.
		 * @param		size:string 		New image size in the format widthxheight or percentage%)
		 * @param		options:+Object	Object containing the options
		 * @option		method		Resize method, possible values 0->None,1->Bilinear, 2->Bicubic (Default 1)
		 * @option 		average		Average pixels when downsizing, possible values y/n (Default n)
		 * @example		resize(200x100)		resize to exactly 200x100
		 * @example 	resize(x100)		proportional resize to 100 points height
		 * @example		resize(200x)		proportional resize to 200 points width
		 * @example		resize(50%)			proportional resize to 50%
		 * @return 		bool			 	If it succeeded resizing or not
		 */
		resize: function(size, options) {
			this.EIJ.resize(size, options);
			return this;
		},

		/**
		 * @function 		contrast(options)
		 * Applies a contrast filter to the image
		 * @param		options:+Object	Object containing the options
		 * @option		equalize	equalize the image, possible values y/n (Default y)
		 * @option		saturated	stretch image histogram with saturated pixels, possible values greater than 0 and less than 100 (Default 0)
		 * @return 		!this
		 */
		contrast: function(options) {
			this.EIJ.contrast(options);
			return this;
		},

		/**
		 * @function 		createMask(options)
		 * Generates a mask based on the options
		 * @param		options:+Object	Object containing the options
		 * @option		method			Name of the method to use to determine the threshold: Default, Huang, IJ_IsoData, Intermodes, IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen (Default: Default)
		 * @option		darkBackground	Boolean to specify that the background is dark (Default: true)
		 * @option		maskColor		Possible values: red, b/w, original (Default: red).
		 * @option		imageFilter		Allows to directly process RGB images by specifying on which derived image the mask will be created. Possible values: red, green, blue, hue, saturation, brightness, edge, texture, grey
		 * @return		+IJ				A new image containing the mask
		 * 
		 * @link	http://fiji.sc/wiki/index.php/Auto_Threshold	Threshold explanation
		 */
		createMask: function(options) {
			var options=options?options:{};
			var eij;
			if (options.imageFilter) {
				var imageFilter=options.imageFilter;
				var image;
				if (imageFilter=="red") {
					image=this.EIJ.splitRGB()[0];
				} else if (imageFilter=="green") {
					image=this.EIJ.splitRGB()[1];
				} else if (imageFilter=="blue") {
					image=this.EIJ.splitRGB()[2];
				} else if (imageFilter=="hue") {
					image=this.EIJ.splitHSB()[0];
				} else if (imageFilter=="saturation") {
					image=this.EIJ.splitHSB()[1];
				} else if (imageFilter=="brightness") {
					image=this.EIJ.splitHSB()[2];
				} else if (imageFilter=="edge") {
					image=this.EIJ.duplicate().edge();
				} else if (imageFilter=="grey") {
					image=this.EIJ.duplicate().grey();
				} else if (imageFilter=="texture") {
					image=this.EIJ.duplicate().texture();
				}
				if (image) {
					eij = image.createMask(options);
				}
			} else {
				eij = this.EIJ.createMask(options);
			}
			return new IJ(eij);
		},

		/**
		 * @function		analyze(mask, options)
		 * Returns an object containing all the information about an image
		 * Can also be called with options: analyze(Roi[] rois)
		 * @param	mask:+IJ		an EIJ picture that has a threshold and should be generated with "createMask"
		 * @param	options:+Object	Object containing the options
		 * @option	minLength		minimum length of the selected area
		 * @option	maxLength		maximal length of the selected area
		 * @option	minWidth		minimum width of the selected area
		 * @option	maxWidth		maximal width of the selected area
		 * @option	minHeight		minimum height of the selected area
		 * @option	maxHeight		maximal height of the selected area
		 * @option	minSurface		minimum surface of the selected area
		 * @option	maxSurface		maximal surface of the selected area
		 * @option	scale			scale the ROI with a defined factor
		 * @option	sortBy			how the results should be sorted. Possible values: "x", "y", "xy", "length", "surface"
		 * 
		 * @define	imageStats		{"x":"number","y":"number","height":"number","width":"number","surface":"number","xCenterOfMass":"number","yCenterOfMass":"number","xCentroid":"number","yCentroid":"number","histogram":"[number]","contour":"number","roundRectArcSize":"number"}
		 * @return 	[imageStats]	Result of the analysis as a JSON object
		 */
		analyze: function(mask, options) {
			var result;
			if(mask instanceof IJ) {
				result=this.EIJ.analyze(mask.EIJ, options);
			} else if(mask instanceof Array) {
				result = this.EIJ.analyze(mask, options);
			} else {
				result = this.EIJ.analyze(options);
			}
			return JSON.parse(result);
		},

		/**
		 * @function		split(mask, options)
		 * Returns an array of images based on a mask
		 * Can also be called with options: split(Roi[] rois)
		 * @param	mask:+IJ		an IJ picture that has a threshold and should be generated with "createMask"
		 * @param	options:+Object	Object containing the options
		 * @option	minLength		minimum length of the selected area
		 * @option	maxLength		maximal length of the selected area
		 * @option	minWidth		minimum width of the selected area
		 * @option	maxWidth		maximal width of the selected area
		 * @option	minHeight		minimum height of the selected area
		 * @option	maxHeight		maximal height of the selected area
		 * @option	minSurface		minimum surface of the selected area
		 * @option	maxSurface		maximal surface of the selected area
		 * @option	scale			scale the ROI with a defined factor
		 * @option	sortBy			how the results should be sorted. Possible values: "x", "y", "xy", "length", "surface"
		 * 
		 * @return 	[+IJ]			Array of IJ containing the images
		 */
		split: function(mask, options) {
			var javaSplit;
			if(mask instanceof IJ) {
				javaSplit=this.EIJ.split(mask.EIJ, options);
			} else if(mask instanceof Array) {
				javaSplit = this.EIJ.split(mask, options);
			} else {
				javaSplit = this.EIJ.split(options);
			}
			var l = javaSplit.length, split = new Array(l);
			for (var i=0; i<l; i++) {
				split[i]=new IJ(javaSplit[i]);
			}
			return split;
		},

		/**
		 * @function		paintMask(mask, options)
		 * Returns an image with painted mask
		 * @param	mask:+IJ		The mask to paint
		 * @param	options:+Object	Object containing the options
		 * @option	strokeColor		color of the stroke
		 * @option	strokeSize		width of the stroke
		 * 
		 * @return 	+IJ				New image with painted mask
		 */
		paintMask: function(mask, options) {
			return new IJ(this.EIJ.paintMask(mask.EIJ, options));
		},

		/**
		 * @function		paintRois(Roi[], options)
		 * Returns an image with painted Roi[]
		 * @param	rois:[?]		Array containing the regions of interest
		 * @param	options:+Object	Object containing the options
		 * @option	strokeColor		color of the stroke
		 * @option	strokeSize		width of the stroke
		 * 
		 * @return 	+IJ				New image with painted rois
		 */
		paintRois: function(rois, options) {
			return new IJ(this.EIJ.paintRois(rois, options));
		},

		/**
		 * @function		paintRoi(Roi, options)
		 * Returns an image with painted Roi
		 * @param	roi:?			The region of interest
		 * @param	options:+Object	Object containing the options
		 * @option	strokeColor		color of the stroke
		 * @option	strokeSize		width of the stroke
		 * 
		 * @return 	+IJ				New image with painted roi
		 */
		paintRoi: function(rois, options) {
			return new IJ(this.EIJ.paintRoi(rois, options));
		},

		/**
		 * @function		splitRGB()
		 * Returns an array of 3 images in 256 gray scale: red, green, blue
		 * 
		 * @return 		[+IJ]
		 */
		splitRGB: function() {
			var javaRGB=this.EIJ.splitRGB();
			// we obtain a "java array"
			var rgb=[];
			for (var i=0; i<javaRGB.length; i++) {
				rgb.push(new IJ(javaRGB[i]));
			}
			return rgb;
		},

		/**
		 * @function		splitHSB()
		 * Returns an array of 3 images in 256 gray scale: hue, saturation, brightness
		 * 
		 * @return 		[+IJ]
		 */
		splitHSB: function() {
			var javaHSB=this.EIJ.splitHSB();
			// we obtain a "java array"
			var hsb=[];
			for (var i=0; i<javaHSB.length; i++) {
				hsb.push(new IJ(javaHSB[i]));
			}
			return hsb;
		},

		/**
		 * @function 		histogram()
		 * Returns an histogram for this image. Returns a luminosity histogram for RGB images 
		 * @return		[number]
		 */
		histogram: function() {
			return this.EIJ.histogram();
		},

		/**
		 * @function		getWidth()
		 * Returns the width of the image
		 * @return 		number
		 */
		getWidth: function() {
			return this.EIJ.getWidth();
		},

		/**
		 * @function 		getHeight()
		 * Returns the height of the image
		 * @return 		number
		 */
		getHeight: function() {
			return this.EIJ.getHeight();
		},

		/**
		 * @function		getRois(options)
		 * Returns an array of region of interest based on a mask
		 * @param 	options:+Object	Object containing the options
		 * @option	minLength		minimum length of the selected area
		 * @option	maxLength		maximal length of the selected area
		 * @option	minWidth		minimum width of the selected area
		 * @option	maxWidth		maximal width of the selected area
		 * @option	minHeight		minimum height of the selected area
		 * @option	maxHeight		maximal height of the selected area
		 * @option	minSurface		minimum surface of the selected area
		 * @option	maxSurface		maximal surface of the selected area
		 * @option	scale			scale the ROI with a defined factor
		 * @option	sortBy			how the results should be sorted. Possible values: "x", "y", "xy", "length", "surface"
		 * @example	mask.getRois();
		 * @return 	[?]				Array containing the rois
		 */
		getRois: function(options) {
			return this.EIJ.getRois(options);
		},

		/**
		 * @function		duplicate()
		 * Returns a copy of the EIJ Image
		 * @return 		+IJ
		 */
		duplicate: function() {
			return new IJ(this.EIJ.duplicate());
		},

		/**
		 * @function		edge()
		 * Applies a edge filter to the image
		 * @return 		!this
		 */
		edge: function() {
			this.EIJ.edge();
			return this;
		},

		/**
		 * @function		color(nbColor)
		 * Applies a color filter to the image
		 * @param	nbColor:number	Number of colors, possible values between 2 and 256 (Default: 256)
		 * @return 	!this
		 */
		color: function(options) {
			if(typeof options =="number" )
				options = {nbColor:Math.floor(options)};
			this.EIJ.color(options);
			return this;
		},

		/**
		 * @function		grey(nbGrey)
		 * Applies a grey filter to the image
		 * @param	nbGrey:number	Number of greys, possible values between 2 and 256 (Default: 256)
		 * @return 	!this
		 */
		grey: function(options) {
			if(typeof options =="number" )
				options = {nbGrey:Math.floor(options)};
			this.EIJ.grey(options);
			return this;
		},

		/**
		 * @function		texture()
		 * Applies a texture filter to the image
		 * @return 		!this
		 */
		texture: function() {
			this.EIJ.texture();
			return this;
		},

		/**
		 * @function 		getColor()
		 * Returns the number of colors
		 * @return 		number
		 */
		getColor: function() {
			return this.EIJ.getColor();
		},

		/**
		 * @function		crop(x, y, width, height)
		 * Crops a image
		 * @param 		x:number		horizontal value from which to start cutting
		 * @param 		y:number		vertical value from which to start cutting
		 * @param 		width:number	width of the new image, if it is greater than the width of the original image minus the value of x, it calculates the width
		 * @param 		height:number	height of the new image, if it is greater than the height of the original image minus the value of y, it calculates the height
		 * @return 		!this
		 */
		crop: function(x, y, width, height) {
			this.EIJ.crop(x, y, width, height);
			return this;
		},

		/**
		 * @function rgb()
		 * Converts the image to RGB color space
		 * @return !this
		 */
		rgb: function() {
			this.EIJ.rgb();
			return this;
		},

		/**
		 * @function invert()
		 * Inverts the colors of the image
		 * @return !this
		 */
		invert: function() {
			this.EIJ.invert();
			return this;
		},

		/**
		 * @function getNChannels()
		 * Returns the number of channels in the image
		 * @return	number 1 for greyscale, 3 for RGB images
		 */
		getNChannels: function() {
			return this.EIJ.getNChannels();
		},

		/**
		 * @function getHash()
		 * Returns a 64 bits hash of the image
		 * @return	number	An integer representing the hash
		 */
		getHash: function() {
			return this.EIJ.getHash();
		},

		/**
		 * @function reduceColor(factor)
		 * Reduces the number of colors in the image by applying a mask on the pixel values
		 * @param 	factor:number	 An integer between 1 and 7 (1: 8 colors, 2: 64 colors, 3: 512 colors, 4: 4096 colors, 5: 32768 colors, 6: 262144 colors, 7: 2097152 colors)
		 * @return !this
		 */
		reduceColor: function(factor) {
			this.EIJ.reduceColor(factor);
			return this;
		},

		/**
		 * @function colorHistogram(factor)
		 * Returns a histogram where each bin represents a different color
		 * @param 	factor:number 	An integer between 1 and 5 (1: 8 colors, 2: 64 colors, 3: 512 colors, 4: 4096 colors, 5: 32768 colors)
		 * @return	[number]
		 */
		colorHistogram: function(factor) {
			return this.EIJ.colorHistogram(factor);
		}
};