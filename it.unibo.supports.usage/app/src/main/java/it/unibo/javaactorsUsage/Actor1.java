package it.unibo.javaactorsUsage;


import it.unibo.supports2021.ActorBasicJava;

public class Actor1 extends ActorBasicJava {
    private ActorBasicJava dest;
    
    public Actor1(String name, ActorBasicJava dest){
        super(name);
        this.dest = dest;
    }
    @Override
    protected void handleInput(String info) {
        System.out.println( myname + " | " + info + " " + aboutThreads() );
        forward("redirected_"+info, dest);
    }
}
