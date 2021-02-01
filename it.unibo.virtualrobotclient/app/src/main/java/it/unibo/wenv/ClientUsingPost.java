package it.unibo.wenv;
//See

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;			//used to convert String in JSON obj
import org.json.simple.parser.JSONParser;	//used to convert String in JSON obj


import javax.json.Json;
import java.io.InputStreamReader;
import java.net.URI;

public class ClientUsingPost {
	private  final String localHostName    = "localhost"; //"localhost"; 192.168.1.7
	private  final int port                = 8090;
	private  final String URL              = "http://"+localHostName+":"+port+"/api/move";
	private  final String containerHostName= "wenv";
	//private  String sep              =";";
 
	public ClientUsingPost() {
	}
  	protected boolean sendCmd(String move)  {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			System.out.println( move + " sendCmd "  );
			String json         = "{\"robotmove\":\"" + move + "\"}";
			StringEntity entity = new StringEntity(json);
			HttpUriRequest httppost = RequestBuilder.post()
					.setUri(new URI(URL))
					.setHeader("Content-Type", "application/json")
					.setHeader("Accept", "application/json")
					.setEntity(entity)
					.build();
			CloseableHttpResponse response = httpclient.execute(httppost);
			//System.out.println( "ClientUsingPost | sendCmd response= " + response );
			boolean collision = checkCollision(response);
			return collision;
		} catch(Exception e){
			System.out.println("ERROR:" + e.getMessage());
			return true;
		}
	}

	protected boolean checkCollision(CloseableHttpResponse response) throws Exception {
		boolean collision = false;
		javax.json.stream.JsonParser parser = Json.createParser(
				new InputStreamReader((response.getEntity().getContent())));
		//System.out.println( "ClientUsingPost | sendCmd parser= " + parser  );
		parser.next();    //START_OBJECT
		System.out.println( "ClientUsingPost | sendCmd START_OBJECT= " + parser.getString() );
		//{ "collision" : "false", "move": "turnLeft"}
		try{
			JSONParser simpleparser = new JSONParser();
			JSONObject jsonObj = (JSONObject) simpleparser.parse(parser.getString());
			collision = jsonObj.get("collision").equals("true");
			//System.out.println( "ClientUsingPost | collision= " +  collision );
			return collision;
		}catch(Exception e){
			System.out.println("ClientUsingPost | JSONParser ERROR:" + e.getMessage());
			throw(e);
		}
	}

	//TODO: explain this case
	protected boolean checkCollisionNotWorking(CloseableHttpResponse response) throws Exception {
		try{
			String jsonStr = EntityUtils.toString( response.getEntity() );
			System.out.println( "ClientUsingPost | sendCmd jsonStr= " + jsonStr );
			org.json.simple.parser.JSONParser simpleparser = new JSONParser();
			org.json.simple.JSONObject jsonObj = (JSONObject) simpleparser.parse( jsonStr );
			/*
			class java.lang.String cannot be cast to class org.json.simple.JSONObject
			(java.lang.String is in module java.base of loader 'bootstrap';
			org.json.simple.JSONObject is in unnamed module of loader 'app')
			 */
			boolean collision = jsonObj.get("collision").equals("true");
			System.out.println( "ClientUsingPost | collision= " +  collision );
			return collision;
		}catch(Exception e){
			System.out.println("ClientUsingPost | checkCollision ERROR:" + e.getMessage());
			throw(e);
		}

	}
	public boolean moveForward()  { return sendCmd("moveForward");  }
	public boolean moveBackward() { return sendCmd("moveBackward"); }
	public boolean moveLeft()     { return sendCmd("turnLeft");     }
	public boolean moveRight()    { return sendCmd("turnRight");    }
	public boolean moveStop()     { return sendCmd("alarm");        }

	protected void boundary(){
		try {
			System.out.println("STARTING boundary ... ");
			for( int i = 1; i<=4 ; i++ ) {
				boolean b = false;
				while (!b) {
					b = moveForward();
					Thread.sleep(500);
				}
 				b = moveLeft();
				Thread.sleep(500);
			}
			//Thread.sleep(3000);	//avoids premature termination (in docker-compose)
			System.out.println("END");
		} catch (Exception e) {
			System.out.println( "ERROR " + e.getMessage());
		}

	}

	protected void testAhead() {
		try {
			System.out.println("STARTING testAhead ... ");
			boolean collision = false;
			while( ! collision ) {
				Thread.sleep(500);
				collision = moveForward();
			}
			//Return to home
			Thread.sleep(1000);
			collision = false;
			while( ! collision ) {
				collision = moveBackward();
				Thread.sleep(500);
			}
			System.out.println("END testAhead");
		}catch (Exception e) {
			System.out.println( "ERROR " + e.getMessage());
		}
	}
	protected void test() {
		try {
			System.out.println("STARTING test ... ");
			boolean b = moveLeft();
 			System.out.println("END");
		}catch (Exception e) {
			System.out.println( "ERROR " + e.getMessage());
		}
	}
	public static void main(String[] args)   {
		new ClientUsingPost().testAhead();
		//new ClientUsingPost().boundary();
	}
	
 }

//json-simple is also JDK 1.2 compatible,
//which means you can use it on a legacy project which is not yet in Java 5
