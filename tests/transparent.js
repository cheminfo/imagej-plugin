clearLog();

var folder="/tests/images/temp2/";


var image = IJ.load("/tests/images/temp2/ori.png");
var hsb = image.splitHSB();
var mask=hsb[2].createMask({method:"Default"});
mask.save(folder+"MASK.png");
image.saveTransparentPng(folder+"Transparent.png", mask);


var rois=mask.getRois({minLength:100, sortBy:"xy", scale: 1});
var split = image.split(rois);
var masks = mask.split(rois);


for (var i=0; i<split.length; i++) {
  split[i].saveTransparentPng(folder+"split-"+i+".png", masks[i]);
  masks[i].save(folder+"mask-"+i+".png");
}

var small=IJ.load(folder+"split-0.png");
small.grey();
small.save(folder+"split-0-grey.jpg")
var histogram=small.histogram();
jexport("histogram",histogram);
