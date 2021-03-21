package it.unibo.supports.usage

import it.unibo.interaction.IssObserver
import org.json.JSONObject

class NaiveObserver( val counter : Int ) : IssObserver {
    override fun handleInfo(info: String) {
        println("NaiveObserver $counter | $info")
        //while( true ){ } //blocks the system
    }

    override fun handleInfo(info: JSONObject) {
        handleInfo( info.toString() )
    }
}