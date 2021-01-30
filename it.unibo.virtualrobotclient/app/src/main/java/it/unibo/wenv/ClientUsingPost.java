package it.unibo.wenv;
//See

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
			String json = "{\"robotmove\":\"" + move + "\"}";
			StringEntity entity = new StringEntity(json);
			HttpUriRequest httppost = RequestBuilder.post()
					.setUri(new URI(URL))
					.setHeader("Content-Type", "application/json")
					.setHeader("Accept", "application/json")
					.setEntity(entity)
					.build();
			CloseableHttpResponse response = httpclient.execute(httppost);
			boolean collision ;
			javax.json.stream.JsonParser parser = Json.createParser(
						new InputStreamReader((response.getEntity().getContent())));
			parser.next();    //START_OBJECT
			parser.next();    // KEY_NAME (collision)
			parser.next();  // value
			collision = parser.getString().equals("true");
			response.close();
			System.out.println( move + " collision=" + collision);
			return collision;
		} catch(Exception e){
			System.out.println("ERROR:" + e.getMessage());
			return false;
		}
	}

	public boolean moveForward()  { return sendCmd("moveForward");  }
	public boolean moveBackward() { return sendCmd("moveBackward"); }
	public boolean moveLeft()     { return sendCmd("turnLeft");     }
	public boolean moveRight()    { return sendCmd("turnRight");    }
	public boolean moveStop()     { return sendCmd("alarm");        }

	protected void boundary(){
		try {
			System.out.println("STARTING ... ");
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

	public static void main(String[] args)   {
		new ClientUsingPost().boundary();
	}
	
 }

//json-simple is also JDK 1.2 compatible,
//which means you can use it on a legacy project which is not yet in Java 5
