/* Generated by AN DISI Unibo */ 
package it.unibo.caller

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Caller ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("caller STARTS")
						request("cmd", "cmd(a)" ,"actorcoap" )  
						delay(500) 
						request("cmd", "cmd(b)" ,"actorcoap" )  
					}
					 transition(edgeName="t00",targetState="handleReply",cond=whenReply("cmdansw"))
				}	 
				state("handleReply") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition(edgeName="t01",targetState="handleReply",cond=whenReply("cmdansw"))
				}	 
			}
		}
}
