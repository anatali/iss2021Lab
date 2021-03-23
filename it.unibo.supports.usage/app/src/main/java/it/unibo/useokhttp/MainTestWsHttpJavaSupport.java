package it.unibo.useokhttp;

import it.unibo.interaction.IssObserver;
import it.unibo.interaction.MsgRobotUtil;
import it.unibo.supports2021.*;

public class MainTestWsHttpJavaSupport {
    public void testHttp(){
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForHttp( "localhost:8090" );
        support.requestSynch( MsgRobotUtil.turnLeftMsg );
        support.requestSynch( MsgRobotUtil.turnRightMsg );
        //support.close();
    }
    public void testWs(){
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091" );

        String answer = support.requestSynch( MsgRobotUtil.turnRightMsg );
        System.out.println("MainTestWsHttpJavaSupport | testWs answer=" + answer);

        support.forward( MsgRobotUtil.turnLeftMsg );
        support.forward( MsgRobotUtil.turnRightMsg );   //notallowed

        support.close();
    }

    public void TestWsWithObserver(){
        IssWsHttpJavaSupport support    = IssWsHttpJavaSupport.createForWs("localhost:8091" );
        RobotInputController controller = new RobotInputController(support, false, true );
        support.registerObserver(controller);
/*
        IssObserver actorObs0 = new ActorObserverNaive(0);
        support.registerObserver(actorObs0);

        ActorObserverNaive actorObs1 = new ActorObserverNaive(1);   //delay counter*500
        support.registerObserver(actorObs1);
*/
        String trip = controller.doBoundary();
        System.out.println("MainTestWsHttpJavaSupport | trip=" + trip);
    }

    public void TestWsWithConsoleSimulation(){
        IssWsHttpJavaSupport support    = IssWsHttpJavaSupport.createForWs("localhost:8091" );
        RobotInputController controller = new RobotInputController(support, false, true );
        support.registerObserver(controller);

        IssObserver actorObs0 = new ActorObserverNaive(0, controller);
        support.registerObserver(actorObs0);
/*
        ActorObserverNaive actorObs1 = new ActorObserverNaive(1);   //delay counter*500
        support.registerObserver(actorObs1);
*/
        controller.doBoundary();
        ActorObserverBasic.delay(10000);


    }



    public static void main(String[] args) throws Exception{
        System.out.println("MainTestWsHttpJavaSupport | BEGIN " + ActorObserverBasic.aboutThreads() );
        MainTestWsHttpJavaSupport appl = new MainTestWsHttpJavaSupport();
        //appl.testHttp();
        //appl.testWs();
        //appl.TestWsWithObserver();
        appl.TestWsWithConsoleSimulation();
        System.out.println("MainTestWsHttpJavaSupport | END " + ActorObserverBasic.aboutThreads() );
    }
}
