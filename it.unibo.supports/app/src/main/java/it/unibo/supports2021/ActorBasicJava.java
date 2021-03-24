package it.unibo.supports2021;

import it.unibo.interaction.IJavaActor;
import it.unibo.interaction.IssObservable;
import org.jetbrains.annotations.NotNull;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class ActorBasicJava extends Thread implements IJavaActor, IssObservable {
    private boolean goon = true;
    private Vector<IJavaActor> actorobservers      = new Vector<IJavaActor>();
    private BlockingQueue<String> bqueue           = new LinkedBlockingQueue<String>(10);
    protected String name ;
    public ActorBasicJava(String name){
        this.name = name ;
        this.setName( "thread_"+name );   //set the name of the thread
        start();
    }
    //------------------------------------------------

    protected void forward(@NotNull String msg, @NotNull ActorBasicJava dest) {
        dest.send(msg);
    }

    @Override
    public synchronized void send( String msg ){
        try {
            bqueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------
    @Override
    public void run() {
        while (goon){
            waitInputAndElab();
        }
        //System.out.println( name + " | waitInputAndElab END "  );
    }

    public void terminate(){
        //System.out.println(name +  " | terminate "   );
        goon = false;
        send("bye");
    }

    protected void waitInputAndElab() {
        try {
            //System.out.println(name +  " | waitInputAndElab ... "  );
            String info = bqueue.take();
             if (goon) handleInput(info);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected abstract void handleInput(String info);

//---------------------------------------------------------------------------
    @Override
    public void registerActor(@NotNull IJavaActor obs) { actorobservers.add(obs);  }

    @Override
    public void removeActor(@NotNull IJavaActor obs) { actorobservers.remove(obs);   }

    protected void updateObservers(String info ){
        actorobservers.forEach( v -> v.send(info));
    }


//-----------------------------------------------------
    public static String aboutThreads(){
        return "curThread="+Thread.currentThread().getName() +" nthreads="+ Thread.activeCount() ;
    }
    public static void delay(int dt){
        try {
            Thread.sleep(dt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}