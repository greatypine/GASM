function MultiTableHeads() {
	this.columnSize=0;
	this.columns=[];
	this.rowMax=0;
	this.rowIndex=0;
	this.result=[];
	this.src;
}
MultiTableHeads.prototype={
	addRow:function(){
					this.rowIndex++;
					if(this.rowIndex>this.rowMax){
						this.rowMax=this.rowIndex;
						if(this.result[this.rowMax-1]===undefined){
							this.result[this.rowMax-1]=[];
						}
					}
				},
	addColumn:function (o){
					this.columns[this.columnSize]=o.split(":")[0];
					this.columnSize++;
					this.result[this.rowIndex-1][this.columnSize-1]=o.split(":")[1];
				},
	subRow:function (){
					this.rowIndex--;
				},
	fillResult:function (){
						for(var i=0;i<this.rowMax;i++){
							inner:
							for(var j=this.columnSize-1;j>=0;j--){
								var colspan=1;
								while(this.result[i][j] === undefined){
									colspan++;
									j--;
									if(j<0)
										break inner;
								}
								if (this.result[i][j]==='')
									continue inner;
								if(colspan>1)
									this.result[i][j]+="@"+colspan;
								var rowspan=1;
								for(var k=i+1;k<this.rowMax && (this.result[k][j] === undefined ||this.result[k][j]==='');k++){
									this.result[k][j]='';
									rowspan++;
								}
								if (rowspan>1)
									this.result[i][j]+="#"+rowspan;
							}
		
						}
					},
	calculate:function (o){
						var type = typeof(o);
						var shiftRow = Object.prototype.toString.call(o) !== '[object Array]';
						if (shiftRow)
							this.addRow();
						if (type === "string"){
							this.addColumn(o);
						}else if(type === "object"){
							for(var x in o){
								if(shiftRow){
									this.result[this.rowIndex-1][this.columnSize]=x;
								}
								this.calculate(o[x]);
							}
						}
						if (shiftRow)
							this.subRow();
					},
	init:function (o){
			this.src = o;
			this.calculate(this.src);
			this.fillResult();
		},
	toString:function (){
					var s="";
					s+=this.columns.toString()+"\n"+this.columnSize+"\n"+this.rowMax;
					for(var i=0;i<this.rowMax;i++)
						s+="\n"+i+"="+this.result[i].toString();
					return s;
				}
};