/*
axiosClientToWenv
*/

const axios = require('axios')
const URL   = 'http://localhost:8090/api/move' ;

async function doJob(){
    var res = await postTo8090("turnRight")
    console.log("axiosClientToWenv | res: " + res )
    res = await postTo8090("moveForward")
    console.log("axiosClientToWenv | res: " + res )
}

doJob()

//client.send( ahead )	//wait 800
//setTimeout(() => {  client.send( back ); }, 500); 
//setTimeout(() => {  postTo8090("turnLeft") }, 800);
//setTimeout(() => {  postTo8090("moveForward") }, 1500);
//setTimeout(() => {  client.finish( ); },    2500);
//-------------------------------------------------------------------
/*
The async and await keywords enable asynchronous, promise-based behavior
to be written in a cleaner style, avoiding the need to explicitly configure promise chains.

Async functions can contain zero or more await expressions.
async function foo() { return 1 } is equivalent to:  function foo() { return Promise.resolve(1) }
*/
async function postTo8090(move)  {
await axios({
            url: URL,
            data: { robotmove: move },
            method: 'POST',
            timeout: 900,
            headers: { 'Content-Type': 'application/json' }
			//.post(URL, { robotmove: move })
    }).then(response => {
    //console.log("axiosClientToWenv postTo8090 | statusCode: " + response.status  )
    console.log("axiosClientToWenv postTo8090 | data:       " + response.data)
    //console.log("axiosClientToWenv postTo8090 | statusText: " + response.statusText)
    const answer = JSON.parse( response.data )
    console.log("collision= " + answer.collision + " for:" + answer.move )
    return answer.collision
  })
  .catch(error => {
    console.error(error)
  })
}