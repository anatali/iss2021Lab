//const axios    = require('axios')
//const net      = require('net');

const sep      = ";"


//SIMULA UNA FORM che invia comandi POST
function sendRequestData( params, method) {
    console.log("sendRequestData in jscode/utils " + params);
    method = method || "post"; // il metodo POST usato di default
    //console.log(" sendRequestData  params=" + params + " method=" + method);
    var form = document.createElement("form");
    form.setAttribute("method", method);
    myip = location.host;
    form.setAttribute("action", "http://"+myip+"/"+params);
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

