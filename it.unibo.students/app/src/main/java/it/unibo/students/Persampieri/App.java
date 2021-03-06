package it.unibo.students.Persampieri;

import it.unibo.supports2021.IssWsHttpJavaSupport;
import org.json.JSONObject;

public class App {
    public static void main(String[] args) throws InterruptedException {
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091");
        CautiousExplorerActor c      = new CautiousExplorerActor("prova", support);
        support.registerActor(c);
        ControllerActor controllerActor = new ControllerActor("controller");
        c.registerActor(controllerActor);
        controllerActor.registerActor(c);

        JSONObject json = new JSONObject();
        json.put("action", "doBasicMoves");

        controllerActor.send(json.toString());

        json = new JSONObject();
        json.put("stop", "true");
        controllerActor.send(json.toString());

        while (controllerActor.isAlive()) {
            Thread.sleep(1000);
        }

        support.close();

        return;
    }
}
