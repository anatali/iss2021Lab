/*
===============================================================
ClientBoundaryWebsockArilAsynch.java
Use the aril language and the support specified in the
configuration file IssProtocolConfig.txt

The business logic is defined in RobotControllerArilBoundary
that is 'message-driven'
===============================================================
*/
package it.unibo.cautiousExplorer;
import it.unibo.annotations.ArilRobotSpec;
import it.unibo.consolegui.ConsoleGui;
import it.unibo.interaction.IssOperations;
import it.unibo.supports.IssCommSupport;
import it.unibo.supports.RobotApplicationStarter;


@ArilRobotSpec
public class CautiousExplorer {
    private CautiousRobotInputController controller;

    //Constructor
    public CautiousExplorer(IssOperations rs){
        IssCommSupport rsComm = (IssCommSupport)rs;
        controller = new CautiousRobotInputController(rsComm, true, true );
        rsComm.registerObserver( controller );
        System.out.println("CautiousExplorer | CREATED with rsComm=" + rsComm);
        new ConsoleGui(  controller );
    }


    public static void main(String args[]){
        try {
            System.out.println("CautiousExplorer | main start n_Threads=" + Thread.activeCount());
            Object appl = RobotApplicationStarter.createInstance(CautiousExplorer.class);
            System.out.println("CautiousExplorer  | appl n_Threads=" + Thread.activeCount());
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
