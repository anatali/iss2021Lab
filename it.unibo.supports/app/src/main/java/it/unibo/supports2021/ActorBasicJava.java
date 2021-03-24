package it.unibo.supports2021;

import it.unibo.interaction.IJavaActor;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class ActorBasicJava extends Thread implements IJavaActor {
    private boolean goon = true;
    private BlockingQueue<String> bqueue = new LinkedBlockingQueue<String>(10);
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