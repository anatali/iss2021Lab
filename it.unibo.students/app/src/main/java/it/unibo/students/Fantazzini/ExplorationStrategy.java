package it.unibo.students.Fantazzini;

public interface ExplorationStrategy {

    public void moveWithStrategy(CautiousActor actor);

}

class WhateverStrategy implements ExplorationStrategy {
    private int a = 0;
    private int b = 0;
    @Override
    public void moveWithStrategy(CautiousActor actor) {
        if (a % 2 == 0) {
            actor.moveForward();
            actor.path.append("W");
        } else {
            if (b % 2 == 0) {
                actor.turnLeft();
                actor.path.append("L");
            } else {
                actor.turnRight();
                actor.path.append("R");
            }
            b++;
        }
        a++;
    }
}
