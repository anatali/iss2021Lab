package it.unibo.executor;

import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.usage.prodCons.ActorNaiveObserver;

public class MainStepRobotActor {

    public static void main(String args[]){
        System.out.println("================================================================");
        System.out.println("MainStepRobotActor | main " + ActorBasicJava.aboutThreads() );
        System.out.println("================================================================");
    //Configure the system
        ActorNaiveObserver obs = new ActorNaiveObserver("obs");
        IJavaActor stepper     = new StepRobotActor("stepper", obs );
    //Activate the system
        ActorBasicJava.delay(1000);
        stepper.send(ApplMsgs.stepMsg.replace("TIME", "350")  );
        //ActorBasicJava.delay(5000);
    }
}
