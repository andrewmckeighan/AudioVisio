package Server;
//imports for jme3 server apps
import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;
import java.util.concurrent.ConcurrentLinkedQueue;
//import for creating geometric objects
import com.jme3.scene.Geometry;
//import for catching port exceptions
import java.io.IOException;
//import for server logs
import java.util.logging.Level;
import java.util.logging.Logger;
//import for network messaging
import AudioVisio.NetworkUtils.NetworkMessage;

public class ClientMain extends SimpleApplication{
	//Client instance variable
	private Client myClient;
	//Shares variables between threads
	//prevents message loss
	private ConcurrentLinkedQueue<String> messageQueue;
	
	public static void main(String[] args){
		//intializes serializable variables from networkutils class
		NetworkUtils.initializeSerializables();
		//starts Client normally
		ClientMain app = new ClientMain();
		app.start();
	}
	//connects to local host, at PORT
	@Override
	public void simpleInitApp(){
		try{
			myClient = Network.connectToServer("127.0.0.1", NetworkUtils.PORT);
			myClient.start();
		}
		catch(IOException e){
			Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, e);
		}
		//Generates the same box
		Geometry geometric = new CreateGeometrics(this).createBox();
		rootNode.attachChild(geometric);
		
		messageQueue = new ConcurrentLinkedQueue<String>;
		myClient.addMessageListener(new NetworkMessageListener());
	}
	//sends messages from messageQueue
	@Override
	public void simpleUIpdate(float tpf){
		String message = messageQueue.poll();
		if(message !=null){
			fpsText.setText(message);
		}
		//if no messages
		else{
			fpsText.setText("No message in queue.");
		}
	};
	
	private class NetworkMessageListener implements MessageListener<Client>{
		//runs seperate thread from renderer
		public void messageRecieved(Client source, Message m){
			if(m instanceof NetworkMessage){
				NetworkMessage message = (NetworkMessage) m;
			}
			
			messageQueue.add(message.getMessage());
		}
		
	}
	//must override client destroy method from super
	@Override
	public void destroy(){
		myClient.close();
		super.destroy();
	}

}