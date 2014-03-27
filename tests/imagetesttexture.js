clearLog();
var list = dir("/XTC/images/png", {
	filter : ".*tif"
});
var size = 1;
for ( var i = 0; i < size; i++) {
	var image = IJ.load(list[i]);
	image.texture();
	image.save(list[i] + "-1", "{quality:100}");
}
