clearLog();
var list = dir("/XTC/images/png", {
	filter : ".*tif"
});
var size = 1;
for ( var i = 0; i < size; i++) {
	var image = IJ.load(list[i]);
	//var image2 = image;
	var image2 = image.copy();
	//var image2 = IJ.load(list[i]);
	image.edge();
	image2.contrast("{}");
	image.save(list[i] + "-1", "{quality:100}");
	image2.save(list[i] + "-2", "{quality:100}");
}
