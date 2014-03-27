clearLog();


// var image = IJ.load("/tests/images/XTC1.png");

var image = IJ.load("/tests/images/BIO1.JPG");

var mask=image.createMask({imageFilter:"red"});
image.save("ORI.png");
mask.save("TEST.png");

