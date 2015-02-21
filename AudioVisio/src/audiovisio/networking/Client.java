package audiovisio.networking;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import audiovisio.networking.utilities.GeneralUtilities;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;

public class Client extends SimpleApplication{
	
	private com.jme3.network.Client myClient;
	public ConcurrentLinkedQueue<String> messageQueue;
	
	@Override
	public void simpleInitApp(){
		try{
			myClient = Network.connectToServer("127.0.0.1", GeneralUtilities.getPort());
			myClient.start();
		}
		catch(IOException e){
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	
	public void simpleInitApp(String IP){
		try{
			myClient = Network.connectToServer(IP, GeneralUtilities.getPort());
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