clearLog();
var list = dir("/XTC/images/png",{filter:".*png"});
var size=50;
var histograms = new Array(size);
for(var i=0;i<size;i++){
 	var image = IJ.load(list[i]);
  
  	histograms[i]=image.histogram();
}

var comparator = Distance.getComparator('AOSimilarity',{});
//Creating the similarity matrix
var similarity = new Array(size);
for (i=0;i<size;i++) {
	similarity[i]=new Array(size);
}
//Calculating similarities...
for (i=0;i<size;i=i+1) {
	for (j=i;j<size;j=j+1) {
		similarity[i][j]=comparator.quickSimilarity(histograms[i],histograms[j]);
		similarity[j][i]=similarity[i][j];
	}
}

var labels = new Array(size);
var batch="";
for (i=0;i<size;i=i+1) {
	batch=list[i].substring(16,list[i].indexOf("-"));
  	var label = {colorBatch:hex_md5(batch).substring(0,6),image:'http://isicsrv5.epfl.ch/'+list[i],label:batch};
  	label.contrasted='http://isicsrv5.epfl.ch'+list[i].replace("png","cpng");
  	labels[i]=label;
}

//Create the dendrogram	
var dendrogram = Distance.clustering(similarity,labels,{});
//Return to the client the JSON of the dendrogram
jexport('dendrogram',dendrogram,'dendrogram'); 
jexport('sim',similarity,'matrix');

