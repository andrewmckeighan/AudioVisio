package audiovisio.networking;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;

public class Client extends SimpleApplication{
	
	private Client myClient;
	private ConcurrentLinkedQueue<String> messageQueue;
	
	@Override
	public void simpleInitApp(){
		try{
			myClient = Network.connectToServer(/*port, hostname*/);
			myClient.start();
		}
		catch(IOException e){
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@Override
	public void simpleUpdate(float tpf){
		
		String message = messageQueue.poll();
		if(message !=null){
			fpsText.setText(message);
		}
		//if no messages
		else{
			fpsText.setText("No message in queue.");
		}
		
	}
	
	@Override
	public void destroy(){
		myClient.close();
		super.destroy();
	}
	
}