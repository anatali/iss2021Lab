package it.unibo.students.Rondinini.controller;


import it.unibo.students.Rondinini.communication.*;

public interface CrilStrategy<
        C extends Cril & Command,
        R extends Cril & Result,
        E extends Cril & Event> {
    boolean hasNextCommand();
    C nextCommand();
    void setResult(R result);
    void setEvent(E event);
    void reset();
}

