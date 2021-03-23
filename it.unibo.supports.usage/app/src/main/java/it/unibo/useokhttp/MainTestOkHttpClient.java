package it.unibo.useokhttp;

import it.unibo.interaction.MsgRobotUtil;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import okhttp3.*;
import okhttp3.internal.http.RealResponseBody;

public class MainTestOkHttpClient {
    public void doTest() throws Exception{
        MediaType JSON_MediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient httpClient  = new OkHttpClient(); //
        String cmd = MsgRobotUtil.turnLeftMsg;
        RequestBody body = RequestBody.create( JSON_MediaType, cmd);

        Request request1 = new Request.Builder()
                .url( "http://localhost:8090/api/move" )
                .post(body)
                .build() ;
        Response response = httpClient.newCall(request1).execute(); //a stream
        System.out.println("MainTestOkHttpClient | response body=" + ((RealResponseBody)response.body()).string());
    }

  
    public static String aboutThreads(){
        return ""+Thread.currentThread().getName() +" nthreads="+ Thread.activeCount() ;
    }

    public static void main(String[] args) throws Exception{
        System.out.println("MainTestOkHttpClient | BEGIN " + aboutThreads() );
        MainTestOkHttpClient appl = new MainTestOkHttpClient();
        appl.doTest();
        System.out.println("MainTestOkHttpClient | END " + aboutThreads() );
    }
}
