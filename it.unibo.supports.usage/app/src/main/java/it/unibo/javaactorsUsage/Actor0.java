package it.unibo.javaactorsUsage;


import it.unibo.supports2021.ActorBasicJava;

public class Actor0 extends ActorBasicJava {

    public Actor0(String name ){
        super(name);
     }
    @Override
    protected void handleInput(String info) {
        System.out.println( name + " | " + info + " " + aboutThreads() );
     }
}
