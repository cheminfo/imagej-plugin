clearLog();
var list = dir("/XTC/images/png", {
	filter : ".*png"
});
var size = 2;
var histograms = new Array(size);
var aSize = new Array(size);
for ( var i = 0; i < size; i++) {
	var image = IJ.load(list[i]);
	aSize[i]=new Array(3);
	aSize[i][0] = list[i];
	aSize[i][2] = image.getHeight();
	aSize[i][1] = image.getWidth();
	/*
	 * image.contrast("{}"); image.edge(); image.save(list[i] + "-1",
	 * "{quality:100}");
	 */
}
jexport('size', aSize, 'matrix');
