/* Generated by AN DISI Unibo */ 
package it.unibo.callerco2

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Callerco2 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("caller2 request cmd ")
						request("cmd", "cmd(callerco2)" ,"resourceandco" )  
					}
					 transition(edgeName="t09",targetState="handleReply",cond=whenReply("replytocmd"))
				}	 
				state("handleReply") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("       --- caller2 handleReply: emit tsunami ")
						emit("alarm", "alarm(tsunami)" ) 
					}
					 transition(edgeName="t010",targetState="handleAlarm",cond=whenEvent("alarm"))
				}	 
				state("handleAlarm") { //this:State
					action { //it:State
						println("       --- caller2 handleAlarm   ")
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition(edgeName="t011",targetState="handleReply",cond=whenReply("replytocmd"))
					transition(edgeName="t012",targetState="handleAlarm",cond=whenEvent("alarm"))
				}	 
			}
		}
}
