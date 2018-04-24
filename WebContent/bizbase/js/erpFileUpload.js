/**
 * ERP 数据统一上传方法
 * @param {Object} managerName 
 * @param {Object} methodName
 * @param {Object} businessType
 */
function onUpload(managerName,methodName){
	var inputFile= $("#erpUploadFile").val();
	if(inputFile == ""){
		$$.showMessage('${system.info}','请选择一个文件');
		return;
	}
	var process = document.getElementById('processing');
		if(process)
		{
		  document.getElementById("processMsg").innerHTML = "正在进行信息检验...";
		  process.style.display="block";
		}
	$.ajaxFileUpload({
		url:getRootPath()+'/uploaderAction.action?businessType=' + managerName,
		secureuri:false,
		fileElementId:'erpUploadFile',
		dataType: 'json',
		success: function (data, status){
			if(!data.result){
				process.style.display="none";
				if(data.message=='File suffix is not allowed.'){
					$$.showMessage('${system.info}','${db.importFile.fileTypeError}');
					return;
				}							
				if(data.message=='File size is too large.'){
					$$.showMessage('${system.info}','${db.importFile.overSize}');
					return;
				}
				if(data.message=='Error that the file was writen in the disk.'){
					$$.showMessage('${system.info}','${db.importFile.writeError}');
					return;
				}
			}
			document.getElementById("processMsg").innerHTML = "文件上传成功,正在导入数据...";
			var jsonData = eval("("+data.data+")"); 
		  	var attId=jsonData.id;
		  	var filePath=jsonData.filePath;
		  	
            var data = new PMSRequestObject(managerName, methodName, [filePath]);
			$$.ajax($$.PMSDispatchActionUrl, "requestString=" + data.toJsonString(), function(data, textStatus, XMLHttpRequest){
				process.style.display="none";
				if(data.result){
					$$.showMessage('${system.info}','导入成功!');
				}else{
					$$.showMessage('${system.info}', data.message);
				}
             },function(data, textStatus, XMLHttpRequest){
			  	process.style.display="none";
				$$.showMessage('${system.info}', data.message);
			 });
		},
		error: function (data, status, e){
			process.style.display="none";
			$$.showMessage('${system.info}', data.message);
		}
	})
}