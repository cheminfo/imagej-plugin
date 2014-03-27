var img = IJ.load("tests/images/wheel.jpg");
var histogram = img.EIJ.colorHistogram(2);
jexport("histogram",histogram);
img.EIJ.reduceColor(2);
img.save("tests/images/wheelR.jpg");