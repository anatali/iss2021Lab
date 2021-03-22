package it.unibo.oksupports;

import it.unibo.interaction.IssCommSupport;
import it.unibo.interaction.IssObserver;
import okhttp3.*;
import okhttp3.internal.http.RealResponseBody;
import org.jetbrains.annotations.NotNull;
import java.util.Vector;

public class WebSocketJavaSupport extends WebSocketListener implements IssCommSupport {
    private boolean connectForWs           = true;
    private Vector<IssObserver> observers  = new Vector<IssObserver>();
    private WebSocket myWs;
    private OkHttpClient okHttpClient  = new OkHttpClient();
    final MediaType JSON_MediaType     = MediaType.get("application/json; charset=utf-8");

    public static WebSocketJavaSupport createForHttp( String addr ){
        return new WebSocketJavaSupport(addr, false);
    }
    public static WebSocketJavaSupport createForWs( String addr ){
        return new WebSocketJavaSupport(addr, true);
    }
    //Constructor
    private WebSocketJavaSupport(String addr, boolean wsconn ){  //localhost:8091
        connectForWs = wsconn;
        if( wsconn ) wsconnect(addr);
        else httpconnect(addr);
    }
    //----------------------------------------------------------------------
    @Override
    public void registerObserver(@NotNull IssObserver obs) {
        observers.add( obs );
    }

    @Override
    public void removeObserver(@NotNull IssObserver obs) {
        observers.remove(obs);
    }

    @Override
    public void close() {
        if( myWs != null ){
            boolean gracefulShutdown = myWs.close(1000, "appl_terminated");
            System.out.println("WebSocketJavaSupport | close gracefulShutdown=" + gracefulShutdown);
        }
    }

    //----------------------------------------------------------------------
    @Override
    public void forward(@NotNull String msgJson) {
        if(connectForWs) myWs.send(msgJson);
        else System.out.println("SORRY: not connected for ws");
    }

    @Override
    public void request(@NotNull String msgJson) {
        if(connectForWs) myWs.send(msgJson);
        else System.out.println("SORRY: not connected for ws");
    }

    @Override
    public void reply(@NotNull String msgJson) {
        if(connectForWs) myWs.send(msgJson);
        else System.out.println("SORRY: not connected for ws");
    }

    @NotNull
    @Override
    public String requestSynch(@NotNull String msg) {
        if( ! connectForWs)  return sendHttp( msg );
        else return "SORRY: not connected for HTTP";
    }

//----------------------------------------------------------------------
    @Override
    public void onOpen(WebSocket webSocket, Response response  ) {
        System.out.println("WebSocketJavaSupport | onOpen ");
    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason  ) {
        System.out.println("WebSocketJavaSupport | onClosing ");
    }
    @Override
    public void onMessage(WebSocket webSocket, String msg  ) {
        System.out.println("WebSocketJavaSupport | onMessage " + msg );
        updateObservers( msg );
    }

    protected void updateObservers( String msg ){
        //System.out.println("WebSocketJavaSupport | updateObservers " + observers.size() );
        observers.forEach( v -> v.handleInfo(msg));
    }

//----------------------------------------------------------------------
    public void wsconnect(String wsaddr){    // localhost:8091
        Request request = new Request.Builder()
                .url( "ws://"+wsaddr )
                .build() ;
        myWs = okHttpClient.newWebSocket(request, this);
        System.out.println("WebSocketJavaSupport | wsconnect myWs=" + myWs);
    }

    public void httpconnect(String httpaddr){    //localhost:8090/api/move
        Request request = new Request.Builder()
                .url( "http://"+httpaddr )
                .build() ;
        //myWs = okHttpClient.newWebSocket(request, this);
        System.out.println("WebSocketJavaSupport | httpconnect myWs=" + myWs);
    }

    public String sendHttp( String msgJson){
        try {
            RequestBody body = RequestBody.create(JSON_MediaType, msgJson);
            Request request = new Request.Builder()
                    .url("http://localhost:8090/api/move")
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute(); //a stream
            String answer     = ((RealResponseBody) response.body()).string();
            //System.out.println("WebSocketJavaSupport | response body=" + answer);
            return answer;
        }catch(Exception e){
            return "";
        }
    }

}
