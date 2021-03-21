/*
===============================================================
RobotObserver.java
handles messages received on the cmdsocket-8091
===============================================================
*/
package it.unibo.observers;
import it.unibo.interaction.IssObserver;
import org.json.JSONObject;

public class RobotObserver implements IssObserver {
    @Override
    public void handleInfo(String infoJson) {
        System.out.println("RobotObserver    | String infoJson =" + infoJson);
        handleInfo( new JSONObject(infoJson) );
    }
    @Override
    public void handleInfo(JSONObject infoJson) {
        System.out.println("RobotObserver    | JSONObject infoJson =" + infoJson);
        if( infoJson.has("endmove") ) handleEndMove(infoJson);
    }

    protected void handleEndMove( JSONObject move ){
        String endOfMOve = (String) move.get("endmove");
        System.out.println("RobotObserver    | endOfMOve=" + endOfMOve);
     }
}
