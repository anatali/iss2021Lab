package it.unibo.executors;

import mapRoomKotlin.RoomMap;
import mapRoomKotlin.mapUtil;
import static mapRoomKotlin.mapUtil.*;
import static mapRoomKotlin.mapUtil.getMapRep;

public class RobotMovesInfo {
        private boolean  doMap = false;
        private String journey = "";
        private StringBuilder sb;

        public RobotMovesInfo(boolean doMap){
            this.doMap = doMap;
        }
        public void showRobotMovesRepresentation(  ){
            if( doMap ) showMap();
            else System.out.println( "journey=" + journey );
        }

        public String cleanMovesRepresentation(  ){
            journey = "";
            if( doMap ) return getMapAndClean(); //getCleanMap();
            else {
                String answer = journey;
                journey       = "";
                return answer;
            }

        }

        public String getMovesRepresentation(  ){
            if( doMap ) return getMapRep();
            else return journey;
        }

        public String getStringReverseMovesRepresentation(  ){
            sb = new StringBuilder(journey);
            sb.reverse();
            return sb.toString();
        }

        public void updateMovesRep(String move ){
            if( doMap )  doMove( move );
            //else
            journey = journey + move;
        }

        public void setObstacle(){
            if(doMap) mapUtil.setObstacle();
        }

}
