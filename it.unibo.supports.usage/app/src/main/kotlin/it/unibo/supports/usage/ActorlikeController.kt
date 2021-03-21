package it.unibo.supports.usage


import it.unibo.interaction.IssObserver
import it.unibo.supports.WebSocketKotlinSupport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.json.JSONObject


class ActorlikeController(val scope: CoroutineScope,
                          val support: WebSocketKotlinSupport) : IssObserver  {

    val robotBehaviorLogic = RobotBoundaryWalkLogic( scope, support )
    val inputChannel       = Channel<String>()

    init{
        println("ActorlikeController | STARTS  ")
        scope.launch{
            robotBehaviorLogic.doBoundaryInit()
            msgLoop()
        }
    }


    override fun handleInfo(info: String) {
        scope.launch{ inputChannel.send( info ) }
    }

    override fun handleInfo(info: JSONObject) {
        handleInfo( info.toString() )
    }


    suspend fun msgLoop(){
            var endOfJob = false
            while ( ! endOfJob ) {
                val v = inputChannel.receive()
                val vJson  = JSONObject( v.toString() )
                println("ActorlikeController | receives $v ")  //in ${curThread()}
                if( vJson.has("endmove")  ) endOfJob = handleEndMove( vJson )
            }

    }

    suspend fun handleEndMove( endmove : JSONObject) : Boolean{
        val answer = endmove["endmove"] as String
        val move   = endmove["move"] as String
        when( answer ){
            "true" -> robotBehaviorLogic.boundaryStep(move, false)
            "false"-> robotBehaviorLogic.boundaryStep(move, true)
            "halted" -> return true
            "notallowed" ->  println("ActorlikeController | handleEndMove to do notallowed" )
            else -> println("ActorlikeController | handleEndMove IMPOSSIBLE answer=$answer" )
        }
        return false
    }

}