clearLog();
var list = dir("/XTC/images/original", {
});
var size = list.length;
for ( var i = 0; i < size; i++) {
	var image = IJ.load(list[i]);
	var split = image.split();
	var sizeS = split.length;
	for ( var a = sizeS - 2; a < sizeS; a++) {
		var auxImage = split[a];
		auxImage.save(list[i] + "-img" + i + "-obj" + a, "{quality:100}");
	}

}
