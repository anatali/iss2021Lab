/*
===============================================================
ClientBoundaryWebsockArilAsynch.java
Use the aril language and the support specified in the
configuration file IssProtocolConfig.txt

The business logic is defined in RobotControllerArilBoundary
that is 'message-driven'
===============================================================
*/
package it.unibo.robotWithActorJava;

import it.unibo.consolegui.ConsoleGui;
import it.unibo.consolegui.ConsoleGuiActor;
import it.unibo.resumableWalker.RobotApplActorInputController;
import it.unibo.supports2021.IssWsHttpJavaSupport;

public class MainRobotActorJava {
    private RobotApplActorInputController controller;

    //Constructor
    public MainRobotActorJava( ){
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091" );

        RobotApplication ra = new RobotApplication("robotAppl", support);
        support.registerActor(ra);

        ConsoleGuiActor console = new ConsoleGuiActor();
        console.registerActor(ra);

        ra.startJob();

        System.out.println("MainRobotActorJava | CREATED  n_Threads=" + Thread.activeCount());

    }


    public static void main(String args[]){
        try {
            System.out.println("MainRobotActorJava | main start n_Threads=" + Thread.activeCount());
            new MainRobotActorJava();
            //System.out.println("MainRobotActorJava  | appl n_Threads=" + Thread.activeCount());
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
