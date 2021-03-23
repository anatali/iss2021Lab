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

public abstract class ActorObserverBasic extends Thread implements IssObserver {
    private boolean goon         = true;
    private Vector<String> queue = new Vector<String>();
    private BlockingQueue<String>  bqueue = new LinkedBlockingQueue<String>(10);
    protected int counter;

    public ActorObserverBasic(int counter ){
        System.out.println("ActorObserverBasic " + this.getName() + " | STARTS "  );
        this.setName("threadobs_" + counter);
        this.counter = counter;
         start();
    }

    public static String aboutThreads(){
        return ""+Thread.currentThread().getName() +" nthreads="+ Thread.activeCount() ;
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
            //System.out.println("ActorObserverBasic | waitInputAndElab " + info  );
            //try { System.in.read(); } catch (IOException e) { e.printStackTrace(); }
            if(  goon ) handleInput( info );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract void handleInput(String info);
    /*{
        System.out.println("ActorObserverBasic " + counter + " | --- " + info + " " + MainTestWsHttpJavaSupport.aboutThreads());
        delay( counter*500 );
    }*/

    public static void delay(int dt){
        try {
            Thread.sleep(dt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



//------------------------------------------------------------------
@Override
    public void handleInfo(String infoJson) {
         //System.out.println("ActorObserverBasic " + counter + " | --- " + infoJson );
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
