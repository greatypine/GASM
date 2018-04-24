function sendNotice(roomid, usercode, username, action, msg) {
	
	if(action == "enter" || action == "exit") {
		return;
	}
  	msg = encodeURI(encodeURI(msg));
  	
  	PL.sessionId = usercode + "_" + roomid;
    p_publish_out(roomid, 'action', action, 'nick', encodeURI(encodeURI(username)), 'msg', msg);  	
}