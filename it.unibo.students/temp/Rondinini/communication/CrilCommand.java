package it.unibo.students.Rondinini.communication;

import org.json.JSONObject;

import java.util.Optional;

public enum CrilCommand implements Command, Cril {
    FORWARD("moveForward", 300),
    BACKWARD("moveBackward", 300),
    LEFT("turnLeft", 300),
    RIGHT("turnRight", 300),
    ALARM("alarm", 10);


    private final String crilRepresentation;

    CrilCommand(final String move, final int time) {
        this.crilRepresentation = "{\"robotmove\":\"" + move + "\", \"time\":" + time + "}";
    }

    @Override
    public Optional<String> toCril() {
        return Optional.of(this.crilRepresentation);
    }
}
