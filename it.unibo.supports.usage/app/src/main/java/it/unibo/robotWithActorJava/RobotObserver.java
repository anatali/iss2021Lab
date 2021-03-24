package it.unibo.robotWithActorJava;

import it.unibo.supports2021.ActorBasicJava;

public class RobotObserver extends ActorBasicJava {
    public RobotObserver(String name) {
        super(name);
    }

    @Override
    protected void handleInput(String msgJson) {
        System.out.println( name + " | " + msgJson);
    }
}
