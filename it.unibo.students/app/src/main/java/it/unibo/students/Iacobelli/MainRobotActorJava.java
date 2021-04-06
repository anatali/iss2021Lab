package it.unibo.students.Iacobelli;

import it.unibo.supports2021.IssWsHttpJavaSupport;
//import it.unibo.virtualRobotClient.consolegui.ConsoleGuiActor;

public class MainRobotActorJava {


    //Constructor
    public MainRobotActorJava( ){
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091" );

        //while( ! support.isOpen() ) ActorBasicJava.delay(100);

        CautiousExplorerActor ra = new CautiousExplorerActor("robotAppl", support);
        support.registerActor(ra);

        ra.send("startApp");

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
