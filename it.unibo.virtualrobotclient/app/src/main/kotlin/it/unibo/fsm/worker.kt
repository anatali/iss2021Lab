/**
 * worker.kt
 * @author AN - DISI - Unibo
===============================================================

===============================================================
 */
package it.unibo.fsm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class worker (  name: String, scope: CoroutineScope,
				discardMessages:Boolean=true ) : Fsm( name, scope, discardMessages ){
	override fun getInitialState() : String{
		return "init"
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	override fun getBody() : (Fsm.() -> Unit){
		//val steprobot = stepper("stepper", scope, usemqtt=false, owner=myself )
		return{
			state("init") {	
				action { println("worker | START")    }
			    transition( edgeName="t0",targetState="dowork", cond=doswitch() )
			}
			
			state("dowork"){
				action {
					action { println("worker | dowork")    }
					delay(1000)
					//forward(  robot, "w", worker )
				}
				transition( edgeName="t0",targetState="dowork",    cond=whenDispatch("stepdone") )
				transition( edgeName="t0",targetState="obstacle",  cond=whenDispatch("stepfail") )
			}
			
			state("obstacle"){
				action {
					println( "worker step failed: $currentMsg" )
					//forward("end", "normal", steprobot )
					terminate()
				}
			}
		}
	}

}//worker 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
	val  robot = worker("worker", this )
	Messages.forward("main","msg","w", robot  )
	robot.waitTermination()
	println("worker main ends")
}