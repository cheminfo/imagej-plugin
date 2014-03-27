
var image = IJ.load("/tests/images/BW.jpg");
image.rgb();
image.invert();
image.save("/tests/BW-rgb.jpg");