/*
===============================================================
RobotBoundaryWalkLogic.java
implements the business logic  

===============================================================
*/
package it.unibo.observers

import it.unibo.interaction.MsgRobotUtil
import it.unibo.supports.IssCommSupport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import mapRoomKotlin.mapUtil.getMapRep
import wenv.RobotMovesInfo

class RobotBoundaryWalkLogic( val scope: CoroutineScope, private val rs: IssCommSupport, 
                             val usearil: Boolean = false,  doMap: Boolean = true ) {
    private var stepNum          = 1
    private var boundaryWalkDone = false
    private val moveInterval     = 1000L
    private val robotInfo: RobotMovesInfo
 
    init {
        robotInfo = RobotMovesInfo(doMap)
        robotInfo.showRobotMovesRepresentation()
    }



    fun doBoundaryInit()  {
        println("RobotBoundaryWalkLogic | doBoundaryInit rs=$rs usearil=$usearil")
        rs.request(if (usearil) MsgRobotUtil.wMsg else MsgRobotUtil.forwardMsg)
        //The reply to the request is sent by WEnv after the wtime defined in issRobotConfig.txt  
        //delay(moveInterval ); //to reduce the robot move rate
        println(getMapRep())
    }

    fun updateMovesRep(move: String?) {
        robotInfo.updateRobotMovesRepresentation(move)
    }

     fun boundaryStep(move: String, obstacle: Boolean) {
        if (stepNum <= 4) {
            if (move == "turnLeft") {
                updateMovesRep("l")
                //showRobotMovesRepresentation();
                if (stepNum == 4) {
                    boundaryWalkDone = true
                    
                    return
                }
                stepNum++
                doBoundaryGoon()
                return
            }
            //the move is moveForward
            if (obstacle) {
                rs.request(if (usearil) MsgRobotUtil.lMsg else MsgRobotUtil.turnLeftMsg)
                //delay(1000) //to reduce the robot move rate
            }
            if (!obstacle) {
                updateMovesRep("w")
                doBoundaryGoon()
            }
            robotInfo.showRobotMovesRepresentation()
        } else { //stepNum > 4
            println("RobotBoundaryWalkLogic | boundary ENDS")
        }
    }

    fun doBoundaryGoon() {
        rs.request(if (usearil) MsgRobotUtil.wMsg else MsgRobotUtil.forwardMsg)
        //delay(moveInterval) //to reduce the robot move rate
    }

}