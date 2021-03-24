package it.unibo.supports2021.usage;

import it.unibo.interaction.MsgRobotUtil;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;

public class IssWsHttpJavaSupportUsage {

    public void testHttp(){

        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForHttp( "localhost:8090" );

        support.requestSynch( MsgRobotUtil.forwardMsg );
        support.requestSynch( MsgRobotUtil.backwardMsg );
        //support.forward( MsgRobotUtil.turnLeftMsg );

        support.close();

    }
    public void testWs(){
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091" );

        String answer = support.requestSynch( MsgRobotUtil.turnRightMsg );
        System.out.println("WebSocketJavaSupportUsage | testWs answer=" + answer);

        support.forward( MsgRobotUtil.turnLeftMsg );
        support.forward( MsgRobotUtil.turnRightMsg );   //notallowed

        support.close();
    }

    public void testObservers(){
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091" );
        ActorObserver[] observers = new ActorObserver[5];

        for( int i = 0; i<5; i++){
            observers[i] = new ActorObserver("a"+i);
            support.registerActor( observers[i] );
         }

        support.forward( MsgRobotUtil.turnLeftMsg );
        ActorBasicJava.delay(1000);

        for( int i = 0; i<5; i++){
            support.removeActor(observers[i]);
            observers[i].terminate();
        }
        support.close();
    }


    public static void main(String[] args) throws Exception{
        System.out.println("WebSocketJavaSupportUsage | BEGIN " + ActorBasicJava.aboutThreads() );
        IssWsHttpJavaSupportUsage appl = new IssWsHttpJavaSupportUsage();
        //appl.testWs();
        //appl.testHttp();
        appl.testObservers();
        System.out.println("WebSocketJavaSupportUsage | END " + ActorBasicJava.aboutThreads() );

    }
}
