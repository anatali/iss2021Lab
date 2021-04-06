package it.unibo.students.Fantazzini;

import it.unibo.executors.RobotMovesInfo;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import org.json.JSONObject;

public class CautiousActor extends ActorBasicJava {

    protected enum State {INIT, EXPLORE, TURN_1, BACK, TURN_2, HALT};

    protected IssWsHttpJavaSupport support;
    protected ExplorationStrategy strategy;
    protected RobotMovesInfo mapper;
    protected State state;
    protected StringBuilder path;
    protected String reversedPath;
    protected boolean lastSonar;

    public CautiousActor(String name, IssWsHttpJavaSupport support, ExplorationStrategy strategy) {
        super(name);
        this.support = support;
        this.strategy = strategy;
        this.mapper = new RobotMovesInfo(true);
        this.state = State.INIT;
        this.support.registerActor(this);
        this.path = new StringBuilder();
        this.reversedPath = null;
        this.lastSonar = false;
    }

    @Override
    protected void handleInput(String s) {
        if (s.equals("start")) {
            this.automaton(true);
            this.lastSonar = false;
        } else {
            JSONObject message = new JSONObject(s);
            if (message.has("endmove")) {
                this.automaton(Boolean.parseBoolean(message.getString("endmove")));
                this.lastSonar = false;
            }
            else if (message.has("sonarName") && !lastSonar) {
                this.delay(2000);
                this.lastSonar = true;
            }
        }
        System.out.println("INPUT --> " + s);
    }

    protected void automaton(boolean input) {
        switch(this.state) {
            case INIT:
                this.strategy.moveWithStrategy(this);
                this.state = State.EXPLORE;
                System.out.println("AUTOMATON --> init");
                break;
            case EXPLORE:
                if (input == true) {
                    this.strategy.moveWithStrategy(this);
                    this.state = State.EXPLORE;
                } else { // input == false
                    this.turnLeft();
                    this.state = State.TURN_1;
                }
                System.out.println("PATH --> " + this.path.toString());
                break;
            case TURN_1:
                this.reversePath(this.path);
                this.turnLeft();
                this.state = State.BACK;
                break;
            case BACK:
                boolean done = moveWithPath();
                if (!done) {
                    this.state = State.BACK;
                } else {
                    this.turnLeft();
                    this.state = State.TURN_2;
                }
                System.out.println("PATH --> " + this.reversedPath);
                break;
            case TURN_2:
                this.turnLeft();
                this.state = State.HALT;
            case HALT:
                System.out.println("AUTOMATON --> halt");
                break;
        }
        this.mapper.showRobotMovesRepresentation();
    }

    protected boolean moveWithPath() {
        boolean done = this.reversedPath.length() == 0;
        if (!done) {
            char move = this.reversedPath.charAt(0);
            this.reversedPath = this.reversedPath.substring(1);
            if (move == 'W') this.moveForward();
            else if (move == 'L') this.turnLeft();
            else if (move == 'R') this.turnRight();
        }
        return done;
    }

    protected void moveForward() {
        this.support.forward("{\"robotmove\":\"moveForward\", \"time\": 350}");
        this.mapper.updateMovesRep("w");
        this.delay(1000);
    }

    protected void turnLeft() {
        this.support.forward("{\"robotmove\":\"turnLeft\", \"time\": 300}");
        this.mapper.updateMovesRep("l");
        this.delay(500);
    }

    protected void turnRight() {
        this.support.forward("{\"robotmove\":\"turnRight\", \"time\": 300}");
        this.mapper.updateMovesRep("r");
        this.delay(500);
    }

    protected void reversePath(StringBuilder path) {
        String back = path.reverse().toString();
        back = back.replace('L', 'X');
        back = back.replace('R', 'L');
        back = back.replace('X', 'R');
        this.reversedPath = back;
    }

}
