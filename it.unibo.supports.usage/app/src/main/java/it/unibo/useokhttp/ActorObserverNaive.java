package it.unibo.useokhttp;
import org.json.JSONObject;

public class ActorObserverNaive extends ActorObserverBasic{
    RobotInputController controller;
    public ActorObserverNaive(int counter, RobotInputController controller) {
        super(counter);
        this.controller = controller;
    }

    @Override
    protected void handleInput(String info){
        //System.out.println("ActorObserverNaive " + counter + " | " + info );
        //delay( counter*500 );
        JSONObject jsonInfo = new JSONObject( info );
        if( ! jsonInfo.has("sonarName"))
            System.out.println("ActorObserverNaive " + counter + " | " + jsonInfo + " " + aboutThreads());
        if( jsonInfo.has("collision")) simulateUserCmd();

    }

    protected void simulateUserCmd(){
        System.out.println("ActorObserverNaive " + counter + " | --- simulateUserCmd   "  );
        controller.handleInfo( "{\"robotcmd\":\"STOP\"}" );
    }
}
