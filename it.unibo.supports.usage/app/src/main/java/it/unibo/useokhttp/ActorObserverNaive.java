/**
 * ActorNaive.java
 ===============================================================
 ===============================================================

 */
package it.unibo.useokhttp;
import it.unibo.interaction.IssObserver;
import org.json.JSONObject;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ActorObserverNaive extends Thread implements IssObserver {
    private String info          = null;
    private boolean goon         = true;
    private Vector<String> queue = new Vector<String>();
    private BlockingQueue<String>  bqueue = new LinkedBlockingQueue<String>(10);
    private int counter;

    public ActorObserverNaive(int counter){
        this.setName("threadobsnaive");
        this.counter = counter;
        start();
    }
    @Override
    public void run(){
        while( goon )  waitInputAndElab();
    }

    public void terminate(){
        goon = false;
        put("bye");
    }

    public  void put( String info ){
        try {
            bqueue.put(info);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected  void waitInputAndElab(){
        try {
            String info = bqueue.take();
            if(  goon ) handleInput( info );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void handleInput(String info){
        System.out.println("ActorNaive " + counter + " | --- " + info + " " + TestOkhttp.aboutThreads());
        delay( counter*500 );
    }

    protected void delay(int dt){
        try {
            Thread.sleep(dt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



//------------------------------------------------------------------
@Override
    public void handleInfo(String infoJson) {
         put( infoJson );
    }
    @Override
    public void handleInfo(JSONObject infoJson) {
        handleInfo( infoJson.toString() );
    }

    public void close(){
         terminate();
    }

}
