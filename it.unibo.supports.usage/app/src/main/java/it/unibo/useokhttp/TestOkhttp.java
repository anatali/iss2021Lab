package it.unibo.useokhttp;


import it.unibo.oksupports.WebSocketJavaSupport;
import okhttp3.*;
import it.unibo.interaction.MsgRobotUtil;
import okhttp3.internal.http.RealResponseBody;

public class TestOkhttp {
    public void doTest() throws Exception{
        MediaType JSON_MediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient httpClient  = new OkHttpClient(); //
        String cmd = MsgRobotUtil.turnLeftMsg;
        RequestBody body = RequestBody.create( JSON_MediaType, cmd);
         //Request.Builder request0 = new Request.Builder();
        //request0.url( "http://localhost:8090/api/move" ).build() ;

        Request request1 = new Request.Builder()
                .url( "http://localhost:8090/api/move" )
                .post(body)
                .build() ;
        Response response = httpClient.newCall(request1).execute(); //a stream
        System.out.println("TestOkhttp | response body=" + ((RealResponseBody)response.body()).string());
    }

    public void testHttp(){
        WebSocketJavaSupport support =
                    WebSocketJavaSupport.createForHttp( "localhost:8090" );
        support.requestSynch( MsgRobotUtil.turnLeftMsg );
        support.requestSynch( MsgRobotUtil.turnRightMsg );
        //support.close();
    }
    public void testWs(){
        WebSocketJavaSupport support =
                WebSocketJavaSupport.createForWs("localhost:8091" );

        String answer = support.requestSynch( MsgRobotUtil.turnRightMsg );
        System.out.println("WebSocketJavaSupportUsage | testWs answer=" + answer);

        support.forward( MsgRobotUtil.turnLeftMsg );
        support.forward( MsgRobotUtil.turnRightMsg );   //notallowed

        support.close();
    }

    public void TestWsWithObserver(){
        WebSocketJavaSupport support    = WebSocketJavaSupport.createForWs("localhost:8091" );
        RobotInputController controller = new RobotInputController(support, false, true );
        support.registerObserver(controller);
/*
        ActorObserverNaive actorObs1 = new ActorObserverNaive(1);
        support.registerObserver(actorObs1);

 */
        ActorObserverNaive actorObs2 = new ActorObserverNaive(2);
        support.registerObserver(actorObs2);

        String trip = controller.doBoundary();
        System.out.println("TestWsWithObserver | trip=" + trip);
    }

    public static String aboutThreads(){
        return ""+Thread.currentThread().getName() +" nthreads="+ Thread.activeCount() ;
    }

    public static void main(String[] args) throws Exception{
        System.out.println("TestOkhttp | BEGIN " + aboutThreads() );
        TestOkhttp appl = new TestOkhttp();
        //appl.testHttp();
        //appl.testWs();
        appl.TestWsWithObserver();
        System.out.println("TestOkhttp | END " + aboutThreads() );
    }
}
