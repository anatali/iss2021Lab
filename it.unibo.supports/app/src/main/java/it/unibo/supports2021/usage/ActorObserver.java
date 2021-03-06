package it.unibo.supports2021.usage;

import it.unibo.supports2021.ActorBasicJava;
import org.jetbrains.annotations.NotNull;

public class ActorObserver extends ActorBasicJava {

    public ActorObserver(String name){
        super(name);
    }
    @Override
    protected void handleInput(String info) {
        System.out.println( name + " | " + info + " " + aboutThreads() );
    }

}
