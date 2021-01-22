//const axios    = require('axios')
//const net      = require('net');

const sep      = ";"

function forward( cmd, host="localhost" ){
    payload    =  "{ \"type\": \"" + cmd + "\", \"arg\": 800 }";
        //jsonObject = JSON.stringify(payload);
    msg        = sep+payload+sep;
    const clients = net.connect(8999,host, () => {
      // 'connect' listener
      console.log('connected to server to send:' + msg);
      clients.write(msg+'\r\n');
    })

    clients.on('error', () => {
      console.log('ERROR with host=' + host);
      if( host="localhost" ) forward( cmd, "wenv")
    });
    clients.on('data', (data) => {
      console.log("from wenv server: "+data.toString());
      //clients.end();
    });
    clients.on('end', () => {
      console.log('disconnected from server');
    });
}//forward



//SIMULA UNA FORM che invia comandi POST
function sendRequestData( params, method) {
    console.log("sendRequestData " + params);
    method = method || "post"; // il metodo POST usato di default
    //console.log(" sendRequestData  params=" + params + " method=" + method);
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", "http://localhost:3000/"+params);
    var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "move");
        hiddenField.setAttribute("value", params);
     	//console.log(" sendRequestData " + hiddenField.getAttribute("name") + " " + hiddenField.getAttribute("value"));
        form.appendChild(hiddenField);
    document.body.appendChild(form);
    console.log("body children num= "+document.body.children.length );
    form.submit();
    document.body.removeChild(form);
    console.log("body children num= "+document.body.children.length );
}

function sendTheMove(move){
	console.log("sendTheMove in utils " + move);
	forward( move );
    //stompClient.send("/app/move", {}, JSON.stringify({'name': move }));
}

//exports.forward = forward;