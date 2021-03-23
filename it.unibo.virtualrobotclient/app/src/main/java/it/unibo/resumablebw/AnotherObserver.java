package it.unibo.resumablebw;
import it.unibo.interaction.IssObserver;
import org.json.JSONObject;

public class AnotherObserver implements IssObserver {
    @Override
    public void handleInfo(String info) {
        System.out.println("AnotherObserver | " + info);
        try { Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleInfo(JSONObject info) {
        handleInfo( info.toString() );
    }
}
