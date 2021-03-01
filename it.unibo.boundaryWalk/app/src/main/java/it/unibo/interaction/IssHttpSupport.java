package it.unibo.interaction;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.net.URI;


public class IssHttpSupport implements IssOperations{
    private CloseableHttpClient httpclient;
    private  String URL  = "unknown";

    public IssHttpSupport(String url ){
        httpclient = HttpClients.createDefault();
        URL        = url;
        System.out.println( "IssHttpSupport | created IssHttpSupport url=" + url  );
    }

    @Override
    public void forward( String msg)  {
        //System.out.println( "IssHttpSupport | forward:" + msg  );
        performrequest(msg);
    }

    @Override
    public String request( String msg) {
        //System.out.println( "IssHttpSupport | request:" + msg  );
        return performrequest(msg);
    }


 //===================================================================

    protected String performrequest( String msg )  {
        boolean endmove = false;
        try {
            System.out.println( "IssHttpSupport | request:" + msg + " URL=" + URL );
            StringEntity entity     = new StringEntity(msg);
            HttpUriRequest httppost = RequestBuilder.post()
                    .setUri(new URI(URL))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Accept", "application/json")
                    .setEntity(entity)
                    .build();
            CloseableHttpResponse response = httpclient.execute(httppost);
            //System.out.println( "IssHttpSupport | response:" + response  );
            String jsonStr = EntityUtils.toString( response.getEntity() );
            JSONObject jsonObj = new JSONObject(jsonStr) ;
            if( jsonObj.get("endmove") != null ) {
                endmove = jsonObj.getBoolean("endmove");
                //System.out.println("IssHttpSupport | response=" + endmove);
            }
        } catch(Exception e){
            System.out.println("IssHttpSupport | ERROR:" + e.getMessage());
         }
        return ""+endmove;
    }

}
