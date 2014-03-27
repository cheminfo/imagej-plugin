clearLog();
var list = dir("/XTC/images/png", {
	filter : ".*png"
});
var size = 4;
for ( var i = 0; i < size; i++) {
	var image = IJ.load(list[i]);
	switch (i % 4) {
	case 0:
		image.resize("200x200", "{}");
		break;
	case 1:
		image.resize("150x", "{}");
		break;
	case 2:
		image.resize("x180", "{}");
		break;
	case 3:
		image.resize("40%", "{}");
		break;
	}
	image.save(list[i] + "-1", "{quality:100}");
}
