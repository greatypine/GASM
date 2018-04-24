//处理数据权限编辑时关于字符串的处理
function getAllCheckedNodeCode(checkedAll, unchecked, db){
	var arrChecked = checkedAll.split(",");
	var arrUnchecked = unchecked.split(",");
	var arrDb = db.split(",");
	var allCheckedNodeCode = "";
	if(arrUnchecked != "") {
		for(i=0; i < arrDb.length; i++) {
			var isDelete = false;
			for(j=0; j < arrUnchecked.length; j++) {
				if(arrDb[i] == arrUnchecked[j]) {
					isDelete = true;
					break;
				}
			}
			if(!isDelete) {
				allCheckedNodeCode = allCheckedNodeCode + arrDb[i] + ",";
			}
		}
		allCheckedNodeCode = allCheckedNodeCode.substring(0,allCheckedNodeCode.length - 1);
	} else {
		allCheckedNodeCode = db;
	}
	arrDb = allCheckedNodeCode.split(",");
	if(arrChecked != "") {
		allCheckedNodeCode = allCheckedNodeCode + ",";
		for(i=0; i < arrChecked.length; i++) {
			var isAdd = true;
			for(j=0; j < arrDb.length; j++) {
				if(arrDb[j] == arrChecked[i]) {
					isAdd = false;
					break;
				}
			}
			if(isAdd) {
				allCheckedNodeCode = allCheckedNodeCode + arrChecked[i] + ",";
			}
		}
		allCheckedNodeCode = allCheckedNodeCode.substring(0,allCheckedNodeCode.length - 1);
	} 
	if(allCheckedNodeCode.substring(0,1) == ",") {
		allCheckedNodeCode = allCheckedNodeCode.substring(1,allCheckedNodeCode.length);
	}
	return allCheckedNodeCode;
}