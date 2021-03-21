/*
===============================================================
RobotObserver.java
A class that defines objects tha handles messages received
on the cmdsocket-8091 by putting info on the input queue
of a ActorSimpleShow Thread

It is a 'graceful transition' to the usage of a Kotlin channel
performed by the WebSocketKotlinSupport
===============================================================
*/
package it.unibo.observers;
import it.unibo.interaction.IssObserver;
import org.json.JSONObject;

public class ActorRobotObserver implements IssObserver {
    private ActorSimpleShow handler;

    public ActorRobotObserver(){
        //Activate the handler
        handler = new ActorSimpleShow();
        handler.start();
    }

    @Override
    public void handleInfo(String infoJson) {
        handler.put( infoJson );
    }
    @Override
    public void handleInfo(JSONObject infoJson) {
        handleInfo( infoJson.toString() );
    }

    public void close(){
        handler.terminate();
    }

}
