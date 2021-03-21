package it.unibo.supports.usage

import mapRoomKotlin.mapUtil.doMove
import mapRoomKotlin.mapUtil.getMapAndClean
import mapRoomKotlin.mapUtil.getMapRep
import mapRoomKotlin.mapUtil.showMap

class RobotMovesInfo(val doMap: Boolean = true) {

    private var journey = ""
    fun showRobotMovesRepresentation() {
        if (doMap) showMap() else println("journey=$journey")
    }

    val movesRepresentationAndClean: String
        get() = if (doMap) getMapAndClean() else {
            val answer = journey
            journey = ""
            answer
        }

    val movesRepresentation: String
        get() = if (doMap) getMapRep() else journey

    fun updateRobotMovesRepresentation(move: String) {
        if (doMap) doMove(move) else journey = journey + move
    }


}