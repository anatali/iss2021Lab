package it.unibo.students.Rondinini.communication;

import org.json.JSONObject;
import java.util.Optional;

public enum CrilResult implements Result, Cril {
    TRUE,
    FALSE,
    HALTED,
    NOT_ALLOWED;

    @Override
    public Optional<String> toCril() {
        return Optional.empty();
    }

    public static Optional<CrilResult> toCrilResult(final String crilMessage) {
        final JSONObject json =  new JSONObject(crilMessage);
        if (!json.has("endmove")) {
            return Optional.empty();
        } else {
            /*
            return switch (json.getString("endmove")) {
                case "true" -> Optional.of(TRUE);
                case "false" -> Optional.of(FALSE);
                case "halted" -> Optional.of(HALTED);
                case "notallowed" -> Optional.of(NOT_ALLOWED);
                default -> Optional.empty();
            };*/
            String v = json.getString("endmove");
            if( v.equals("true")) return Optional.of(TRUE);
            if( v.equals("false")) return Optional.of(FALSE);
            if( v.equals("halted")) return Optional.of(HALTED);
            if( v.equals("notallowed")) return Optional.of(NOT_ALLOWED);
            else return Optional.empty();

        }
    }
}

