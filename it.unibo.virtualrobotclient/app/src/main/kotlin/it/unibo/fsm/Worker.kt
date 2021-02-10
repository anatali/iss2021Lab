/**
 * Worker.kt
 * @author AN - DISI - Unibo
===============================================================
A fsm that works as the control for the virtualrobot (walker).
We map events raised from WEnv into dispatches sent to walker.
The walker goes ahead (step by step) until an obstacle (a wall) is found.
The result of the computation is the number of steps done.
We could set the step time so that (e.g. 400)
each step will cover a distance equal to the length of the robot.
===============================================================
 */
package it.unibo.fsm
import it.unibo.interaction.WEnvConnSupport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

lateinit var walker : worker
//lateinit var hh : WEnvConnSupport

class worker (  name: String, scope: CoroutineScope, val hh : WEnvConnSupport,
				discardMessages:Boolean=true ) : Fsm( name, scope, discardMessages ){
	override fun getInitialState() : String{
		return "init"
	}


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	override fun getBody() : (Fsm.() -> Unit){
		//val steprobot = stepper("stepper", scope, usemqtt=false, owner=myself )
		var nstep = 0
		return{
			state("init") {	
				action {
					println("worker | START $hh")
					hh.sendMessage("w")//move the robot
				}
			    transition( edgeName="t0",targetState="walk", cond=doswitch() )
			}

			state("walk"){
				action {
					nstep++
					println("worker | walk nstep=$nstep")
					delay(1000)
					hh.sendMessage("w")//move the robot
				}
				transition( edgeName="t0",targetState="walk",     cond=whenDispatch("stepdone") )
				transition( edgeName="t0",targetState="walkdone", cond=whenDispatch("stepfail") )
			}

 			state("walkdone"){
				action {
					println( "worker |  walkdone: currentMsg=$currentMsg nstep=$nstep" )
					terminate()
				}
			}
		}
	}

}//worker 


//TODO: trasnform messages sent from WEnv in stepdone, stepfail
//var  walker : worker? = null

suspend fun handleWEnvEvent( jsonMsg : String ) { //called by startReceiver in WEnvConnSupport
	println("handleWEnvEvent | receives: $jsonMsg ")
	val jsonObject    = JSONObject( jsonMsg )
	if( jsonObject.has("endmove") ) {
		val endmove = jsonObject.getBoolean("endmove")
		println("handleWEnvEvent | endmove:  ${endmove} ")
		if(endmove) Messages.forward("wenv","stepdone","ok", walker )
		else Messages.forward("wenv","stepfail","todo(Time)", walker )
	}
	//else if( jsonObject.has("collision") ) Messages.forward("wenv","stepfail","", walker )

}//handleWEnvEvent



@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
	val hh = WEnvConnSupport( this, "localhost:8091", "400" ) //blocking
	walker = worker("worker", this, hh )
	hh.startReceiver( ::handleWEnvEvent  )

	//Messages.forward("main","msg","w", walker )
	walker.waitTermination()
	delay(1000)  //to see some sonar message, if it is the case
	hh.stopReceiver()
	println("worker main ends")
}