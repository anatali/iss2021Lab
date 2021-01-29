const axios = require('axios')


//client.send( ahead )	//wait 800
//setTimeout(() => {  client.send( back ); }, 500); 
setTimeout(() => {  postTo8090("turnLeft") }, 800); 
//setTimeout(() => {  client.finish( ); },    2500); 
//-------------------------------------------------------------------
async function postTo8090(move){
const URL = 'http://localhost:8090/api/move' ;

await axios({
            url: URL,
            data: { robotmove: move },
            method: 'POST',
            timeout: 900,
            headers: { 'Content-Type': 'application/json' }
			//.post(URL, { robotmove: move })
    }).then(response => {
    console.log("serverutils postTo8090 | statusCode: " + response.status  )
    console.log("serverutils postTo8090 | data:       " + response.data)
    console.log("serverutils postTo8090 | statusText: " + response.statusText);
  })
  .catch(error => {
    console.error(error)
  })
}