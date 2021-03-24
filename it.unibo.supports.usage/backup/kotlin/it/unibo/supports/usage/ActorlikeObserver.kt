package it.unibo.supports.usage

import it.unibo.interaction.IssObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class ActorlikeObserver(val scope: CoroutineScope, val counter : Int ) : IssObserver {

    val inputChannel = Channel<String>()

    init{
        scope.launch{
            msgLoop()
        }
    }

    override fun handleInfo(info: String) {
        //println("NaiveObserver $counter | $info")
        //while( true ){ } //blocks the system
        scope.launch{ inputChannel.send( info ) }
    }

    override fun handleInfo(info: JSONObject) {
        handleInfo( info.toString() )
    }
    //---------------------------------------------

    suspend fun msgLoop(){
        var endOfJob = false
        while ( ! endOfJob ) {
            val v = inputChannel.receive()
            val vJson  = JSONObject( v.toString() )
            if( ! vJson.has("endmove")) {
                println("ActorObserver $counter | receives $v ")  //in ${curThread()}
                if( counter != 1 ) delay(counter*500L) //loop BLOCK the observers!
            }else{
                val move   = vJson["move"] as String
                println("ActorObserver $counter | move= $move ")
                endOfJob = ( move == "alarm" )
            }
         }
    }
}