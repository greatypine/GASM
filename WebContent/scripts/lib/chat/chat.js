  var iframeid=window.frameElement.id;
  var currentDoc = $(window.parent.document).find("#" + iframeid);
  	
  var roomid= currentDoc.attr('roomid');
  var usercode = currentDoc.attr('usercode');
  var nick = currentDoc.attr('username');
  nick = encodeURI(encodeURI(nick));
  var roomname = currentDoc.attr('roomname'); 
   
  var backgroundColor= currentDoc.attr('backgroundColor');
  if(!backgroundColor) backgroundColor="#003366";
  var chatContentsBackgroundColor = currentDoc.attr('chatContentsBackgroundColor');
  if(!chatContentsBackgroundColor) chatContentsBackgroundColor="#aaaaaa";  
  var chatContentsWidth = currentDoc.attr('chatContentsWidth');
  if(!chatContentsWidth) chatContentsWidth="375px";
  var chatContentsHeight = currentDoc.attr('chatContentsHeight');    
  if(!chatContentsHeight) chatContentsHeight="310px";
  var fontSize = currentDoc.attr('fontSize');    
  if(!fontSize) fontSize="18px";
  var msgWidth = currentDoc.attr('msgWidth');  
  if(!msgWidth) msgWidth="300px";
  
  var chatDoc;
  var chatFrame;
  //var roomid=getPageParameter('roomid');
  //var usercode = getPageParameter('usercode');
  //var nick = getPageParameter('username');
  //nick = encodeURI(encodeURI(nick));
  //var roomname = getPageParameter('roomname');

  var callParent;

  //window.onload = chat_init;

  function chat_init(callback){
  	if(callback) {
  		callParent = callback;
  	}

    if (window.frames && window.frames["chatContents"]) //IE 5 (Win/Mac), Konqueror, Safari
      chatFrame = window.frames["chatContents"];
    else if (document.getElementById("chatContents").contentWindow) //IE 5.5+, Mozilla 0.9+, Opera
      chatFrame = document.getElementById("chatContents").contentWindow;
    else //Moz < 0.9 (Netscape 6.0)
      chatFrame = document.getElementById("chatContents");

    if(chatFrame.document) //Moz 0.9+, Konq, Safari, IE, Opera
      chatDoc = chatFrame.document;
    else //Moz < 0.9 (Netscape 6.0)
      chatDoc = chatFrame.contentDocument;

	$("#chatContents").css("height", chatContentsHeight);
	$("#chatContents").css("width", chatContentsWidth);
	$("#msg").css("width", msgWidth);	
	chatDoc.body.style.fontSize = fontSize;
	chatDoc.body.style.backgroundColor = chatContentsBackgroundColor;
	this.document.body.style.backgroundColor = backgroundColor;
	
    enterChat();
    document.forms[0].msg.focus();
   }

	// Event Callback for join
	function onJoinAck(event) {
 		appendMessage('Listening to chat');
	}

   // Event Callback: display all events
   function onData(event) {
  		p_debug(false, "pushlet-app", 'event received event=' + event.getEvent() );
		var action = event.get('action');
		var content = 'none action=' + action;
		var msg = event.get('msg');
		
		var arrObj = event.get("p_id").split("_");
		var obj = {
			usercode:arrObj[0],
			roomid:arrObj[1],
			action:action,
		};	
		var isAppend = "true";	
		
     if (action == 'send') {
			content = '<b>' + event.get('nick') + '</b>: <i>' + msg + '</i>';
	} else if (action == 'enter') {
			content = '<b><i>*** ' + event.get('nick') + ' 进入'  + roomname + '  ***</i></b>';

			if(callParent){
				setTimeout(function(){
					callParent(obj);
				}, 500);
			}			
	} else if (action == 'exit') {
			content = '<b><i>*** ' + event.get('nick') + ' 离开'  + roomname + '  ***</i></b>';

			if(callParent){
				setTimeout(function(){
					callParent(obj);
				}, 500);
			}
    } else {
    		if(msg == "undefined" || msg == null || msg == "") {
    			isAppend = "false";
			} else {
  				content = '<b>' + event.get('nick') + '</b>: <i>' + event.get('msg') + '</i>';	
			}
			
			if(callParent){
				setTimeout(function(){p_publish
					callParent(obj);
				}, 500);
			}
    }
    
    content = decodeURI(content);
    if(isAppend == "true") {
    	appendMessage(content);
	}

}

// Event Callback: display all events
function onNack(event) {
   alert('negative response from server: ' + event.getEvent() + ' reason: ' + event.get('p_reason'));
}

  function appendMessage  (content){
    var newDiv = chatDoc.createElement("DIV");
    newDiv.innerHTML = content;
    chatDoc.getElementById("contents").appendChild(newDiv);
    chatFrame.scrollTo(0, chatDoc.getElementById("contents").offsetHeight);
  }

  function enterChat(){
  
	p_join_listen(roomid, usercode);
	p_publish(roomid, 'action', 'enter', 'nick', nick);
   }

  function sendMsg(){
  	var msg = document.getElementById("msg").value;
  	msg = encodeURI(encodeURI(msg));
    p_publish(roomid, 'action', 'send', 'nick', nick, 'msg', msg);
    resetForm();
   }

  function leaveChat(){
    // Send exit to chatters
    p_publish(roomid, 'action', 'exit', 'nick', nick);

	// Stop pushlet session
	p_leave();

	// Give some time to send the leave request to server
	//setTimeout('gotoEnter()', 500);
 }

 function gotoEnter() {
    window.location.href='enter.html';
 }

 function resetForm(){
    document.forms[0].msg.value = "";
    document.forms[0].msg.focus();
 }//-->
