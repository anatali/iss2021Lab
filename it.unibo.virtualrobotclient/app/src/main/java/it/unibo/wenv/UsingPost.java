package it.unibo.wenv;
//See
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/*
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
*/

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
 import java.util.HashMap;
import java.util.Map;


public class UsingPost {
	private  String localHostName    = "localhost"; //"localhost"; 192.168.1.7
	private String URL               = "http://"+localHostName+":8090/api/move";
	private  String containerHostName= "wenv";
	private  int port                = 8090;
	private  String sep              =";";
	//private  HttpClient client ;

	public UsingPost() {

	}

/*

	public   void sendCmd(String move) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("move", move);
		String requestBody = obj.toString() ;
		System.out.println(requestBody);

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create( URL ))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(requestBody))
				.build();

		//HttpResponse<String> response =
		HttpClient.newHttpClient().sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::statusCode)
				.thenAccept(System.out::println);
		//System.out.println(response.statusCode());
 	}

*/
 	protected void xxx(String move) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			String json = "{\"robotmove\":\""+move+"\"}";
			StringEntity entity = new StringEntity(json);
			HttpUriRequest httppost = RequestBuilder.post()
					.setUri(new URI(URL))
					.setHeader("Content-Type", "application/json")
					.setHeader("Accept", "application/json")
					.setEntity(entity)
					//.addParameter("robotMove", move)
 					.build();

			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				String answer =
						EntityUtils.toString(response.getEntity());
				System.out.println("answer= "+ answer);
				JsonReader reader = Json.createReader(
						new InputStreamReader( response.getEntity().getContent() ));
				String uuu= "{ \"collision\" : \"false\", \"move\": \"turnLeft\"}";
				System.out.println("answer= "+ uuu);
				javax.json.stream.JsonParser parser = Json.createParser(
						new InputStreamReader((response.getEntity().getContent())));
				//new InputStreamReader( new ByteArrayInputStream( uuu.getBytes() ) ) );

				println("xxx1:" + parser.next());	//START_OBJECT
				println("xxx2:" + parser.next());	// KEY_NAME
				println("xxx2b:" + parser.getString());
				println("xxx3:" + parser.next());
				println("xxx4:" + parser.getString());


				//JsonObject object = reader.readObject();
				//System.out.println("zzz= "+ object);

			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}
/*
	protected void yyy(String move) throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(URL );
		String JSON_STRING="{\"robotmove\" : \""+ move+"\"}";
		println("JSON_STRING:" + JSON_STRING);
		HttpEntity stringEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
		httpPost.setEntity(stringEntity);
		CloseableHttpResponse response = httpclient.execute(httpPost);
		println("RESPONSE:" + response.getStatusLine());
		println("RESPONSE:" + response.getEntity());
		String answer = EntityUtils.toString(response.getEntity(), "UTF-8");
		//String xxx    = answer ;
		//println("RESPONSE answer:" + answer);
		//JSONObject data = new JSONObject( xxx );
		//println("data:" + data);
		JSONParser parser = new JSONParser();
		println("xxx:" + parser.parse(answer));

		Object json   =   parser.parse(answer);
		Object obj    = JSONValue.parse(answer);
		JSONArray array = (JSONArray)obj;
		println("COLLISION:" + array.get(0).toString() );
	}
	*/

//json-simple is that it is also JDK 1.2 compatible,
//which means you can use it on a legacy project which is not yet in Java 5
//
//Read more: https://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html#ixzz6l1awd1qh



	protected void println(String msg) {
		System.out.println(  msg);
	}

/*
	public   void mbotForward() {
 		try {  sendCmd("moveForward"); } catch (Exception e) {e.printStackTrace();}
	}
	public   void mbotBackward() {
		try { sendCmd("moveBackward"); } catch (Exception e) {e.printStackTrace();}
	}
	public   void mbotLeft() {
		try { sendCmd("turnLeft"); } catch (Exception e) {e.printStackTrace();}
	}
	public   void mbotRight(  ) {
		try { sendCmd("turnRight"); } catch (Exception e) {e.printStackTrace();}
	}
	public   void mbotStop() {
		try { sendCmd("alarm"); } catch (Exception e) {e.printStackTrace();}
	}
*/
	public void doJob(){
		try {
			//Thread.sleep(5000);
			System.out.println("STARTING ... ");

			xxx("turnLeft");
/*			Thread.sleep(1000);
			mbotBackward();
			Thread.sleep(1000);

			mbotLeft() ;
			Thread.sleep(1000);
			mbotRight(  ) ;
			Thread.sleep(1000);

			mbotForward();
			Thread.sleep(1000);
			mbotForward();
			Thread.sleep(1000);

			mbotStop();*/
			Thread.sleep(3000);	//avoids premature termination (in docker-compose)
			System.out.println("END");
		} catch (Exception e) {
			System.out.println( "ERROR " + e.getMessage());
		}

	}
  	
//Just for testing
	public static void main(String[] args)   {
		new UsingPost().doJob();
	}
	
 }
