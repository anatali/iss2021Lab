package it.unibo.wenv;
//See
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;



public class ClientUsingSocketIO {
	private  final String localHostName    = "localhost"; //"localhost"; 192.168.1.7
	private  final int port                = 8090;
	private  final String URL              = "http://"+localHostName+":"+port+"/api/move";
	private  final String containerHostName= "wenv";
	//private  String sep              =";";
	private Socket mSocket;

	public ClientUsingSocketIO() {
 	}

	private Emitter.Listener handleCollision = new Emitter.Listener() {
		@Override
		public void call(final Object... args) {
			/* new Thread() {
				public void run() {
					System.out.println(" ... " + args);
				}
			}.start();*/
			System.out.println("handleCollision " + args);
		}
	};
  	protected void doJob( )  {
 		try {
			System.out.println(" doJob starts "  );
			mSocket = IO.socket("http://localhost:8090");
			mSocket.on("collision", handleCollision);
			mSocket.connect();
			//mSocket.emit("new message", "Hello from Java");
			Thread.sleep(30000);
			mSocket.disconnect();
		} catch(Exception e){
			System.out.println("ERROR:" + e.getMessage());
 		}
	}


	public static void main(String[] args)   {
		new ClientUsingSocketIO().doJob();
	}
	
 }

