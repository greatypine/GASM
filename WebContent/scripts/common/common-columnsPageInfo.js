function ColumnsPageInfo(){
	this.currentPage = 1;
}
ColumnsPageInfo.prototype = {
	createPageInfo:function(queryId,pageinfo,sortinfo){
		var pageTable = $("<table align='right' class='pagingtable' width='100%'></table>");
		var tr = $("<tr></tr>");
		var recordsCountPageTd = $("<td style='padding-left: 1px; padding-right: 1px; vertical-align: middle;float:right'>翻页条数：</td>");
		var nPreviousTd = $("<td style='padding-left: 1px; padding-right: 1px; vertical-align: middle;float:right'></td>");
		var nNextTd = $("<td  style='padding-left: 1px; padding-right: 1px; vertical-align: middle;float:right'></td>");
		$(nPreviousTd).html("<img src='" + getRootPath() + "/scripts/common/images/columnprevvious.gif' />&nbsp;&nbsp;");
		$(nNextTd).html("&nbsp;&nbsp;<img src='" + getRootPath() + "/scripts/common/images/columnnext.gif' />");
		var select = $("<select id='recordsCountPageId' style='width:40px'></select>");
		var option1 = $("<option value='0'>3</option>");
		if(pageinfo.recordsCountPage == 0){option1.attr("selected",true)}
		var option2 = $("<option value='1'>2</option>");
		if(pageinfo.recordsCountPage == 1){option2.attr("selected",true)}
		var option3 = $("<option value='2'>1</option>");
		if(pageinfo.recordsCountPage == 2){option3.attr("selected",true)}
		
		select.append(option1);
		select.append(option2);
		select.append(option3);
		recordsCountPageTd.append(select);
		recordsCountPageTd.append("&nbsp;&nbsp;&nbsp;&nbsp;");
		
		tr.append(nNextTd);
		tr.append(nPreviousTd);
		tr.append(recordsCountPageTd);
		pageTable.append(tr);
		
		$(select).bind("change",{queryId:queryId,pageinfo:pageinfo,sortinfo:sortinfo}, function(event) {
			var pageinfo = event.data.pageinfo;
			pageinfo.recordsCountPage = document.getElementById("recordsCountPageId").value;
			pageinfo.currentPage = 1;
			$$.executeMetadataSearch(event.data.queryId,pageinfo,event.data.sortinfo);
            $$.executeSearch(event.data.queryId);
		} );
		
		$(nNextTd).bind("click",{queryId:queryId,pageinfo:pageinfo,sortinfo:sortinfo}, function(event) {
			var pageinfo = event.data.pageinfo;
			if(pageinfo.endRowPosition<pageinfo.totalRecords-1){
				pageinfo.recordsCountPage = document.getElementById("recordsCountPageId").value;
				pageinfo.currentPage = pageinfo.currentPage + 1;
				$$.executeMetadataSearch(event.data.queryId,pageinfo,event.data.sortinfo);
                $$.executeSearch(event.data.queryId);
			}
			
		} );
		
		$(nPreviousTd).bind("click",{queryId:queryId,pageinfo:pageinfo,sortinfo:sortinfo}, function(event) {
			var pageinfo = event.data.pageinfo;
			if(pageinfo.currentPage > 1){
				pageinfo.recordsCountPage = document.getElementById("recordsCountPageId").value;
				pageinfo.currentPage = pageinfo.currentPage - 1;
				$$.executeMetadataSearch(event.data.queryId,pageinfo,event.data.sortinfo);
	            $$.executeSearch(event.data.queryId);
			}
			
		} );
		
		$("#"+queryId+"_columnspageinfo").html(pageTable);
	}
	
}
