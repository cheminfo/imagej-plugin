clearLog();

var folder="/tests/images/temp/";


var image = IJ.load("/tests/images/BIO1.JPG");
/*
 image.save(folder+"ORI.png");

var rgb = image.splitRGB();
for (var i=0; i<rgb.length; i++) {
	rgb[i].save(folder+"RGB-"+i+".png");
}
*/
var hsb = image.splitHSB();
/*
for (var i=0; i<hsb.length; i++) {
	hsb[i].save(folder+"HSB-"+i+".png");
}
*/

var mask=hsb[2].createMask({method:"Li"});
mask.save(folder+"MASK.png");
/*
var painted=image.paintMask(mask);
painted.save(folder+"PAINTED.png");

var split=image.split(mask, {minLength:100});
for (var i=0; i<split.length; i++) {
	split[i].save(folder+"SPLIT-"+i+".png");
}
*/

var rois=mask.getRois({minLength:100, scale: 0.8});
var stats=hsb[1].analyze(rois);

var painted=image.paintRois(rois);
painted.save(folder+"PAINTED.png");

// var stats=JSON.parse(image.analyzeImage(mask, {minLength:100, scale: 0.8}));
jexport("stats",stats);




/*

ImagePlus paintedImage=paintMask(image, mask, Color.BLUE, 3);
save(paintedImage, "./tests/PAINTED","png");

ImagePlus[] spots=split(image, mask, 100, 100000, 0);
saveAll(spots, "./tests/SPOT-","png");

// we will save the red components of all the spots
for (int i=0; i<spots.length; i++) {
	save(splitRGB(spots[i])[0],"./tests/RED-"+i,"png");
}

*/