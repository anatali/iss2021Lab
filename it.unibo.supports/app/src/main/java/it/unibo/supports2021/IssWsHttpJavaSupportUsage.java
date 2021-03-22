package it.unibo.supports2021;

import it.unibo.interaction.MsgRobotUtil;

public class IssWsHttpJavaSupportUsage {

    public void testHttp(){
        boolean wsconn = false;
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForHttp( "localhost:8090" );

        support.requestSynch( MsgRobotUtil.forwardMsg );
        support.requestSynch( MsgRobotUtil.backwardMsg );
        //support.forward( MsgRobotUtil.turnLeftMsg );

        support.close();

    }
    public void testWs(){
        boolean wsconn = true;
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091" );

        String answer = support.requestSynch( MsgRobotUtil.turnRightMsg );
        System.out.println("WebSocketJavaSupportUsage | testWs answer=" + answer);

        support.forward( MsgRobotUtil.turnLeftMsg );
        support.forward( MsgRobotUtil.turnRightMsg );   //notallowed

        support.close();
    }

    public static String aboutThreads(){
        return ""+Thread.currentThread().getName() +" nthreads="+ Thread.activeCount() ;
    }

    public static void main(String[] args) throws Exception{
        System.out.println("WebSocketJavaSupportUsage | BEGIN " + aboutThreads() );
        IssWsHttpJavaSupportUsage appl = new IssWsHttpJavaSupportUsage();
        appl.testWs();
        appl.testHttp();
        System.out.println("WebSocketJavaSupportUsage | END " + aboutThreads() );

    }
}
