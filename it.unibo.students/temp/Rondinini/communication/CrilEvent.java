package it.unibo.students.Rondinini.communication;

import org.json.JSONObject;

import java.util.Optional;

public enum CrilEvent implements Cril, Event{
    COLLISION,
    SENSED;

    @Override
    public Optional<String> toCril() {
        return Optional.empty();
    }

    public static Optional<CrilEvent> toCrilEvent(final String crilMessage) {
        final JSONObject json =  new JSONObject(crilMessage);
        if (json.has("collision") && json.getBoolean("collision")) {
            return Optional.of(COLLISION);
        } else if (json.has("sonarName")) {
            return Optional.of(SENSED);
        } else {
            return Optional.empty();
        }
    }
}

