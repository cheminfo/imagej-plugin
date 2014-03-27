clearLog();

jexport("ab",dir("/XTC/images/png"));

var list = dir("/XTC/images/png", {
	filter : ".*png"
});
var size = list.length;
size=3;
var colors = [];
for ( var i = 0; i < size; i++) {
	var image = IJ.load(list[i]);
	
	image.color({ab:"12"});
	
	colors.push({
		name: list[i],
		width: image.getWidth(), 
		height: image.getHeight(), 
		colors: image.getColor()
		}
	);
}
jexport('colors', colors, 'matrix');
