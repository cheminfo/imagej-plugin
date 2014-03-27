clearLog();
var list = dir("/XTC/images/png", {
	filter : ".*tif"
});
var size = 1;
for ( var i = 0; i < size; i++) {
	var image = IJ.load(list[i]);
	image.crop(207, 587, 279, 204);
	image.save(list[i] + "-1", "{quality:100}");
}
