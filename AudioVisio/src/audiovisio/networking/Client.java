package audiovisio.networking;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import audiovisio.networking.utilities.GeneralUtilities;
import audiovisio.utils.LogHelper;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.network.Network;

public class Client extends SimpleApplication{
	
	private com.jme3.network.Client myClient;
	public ConcurrentLinkedQueue<String> messageQueue;
	
	public Client(){
		super(new StatsAppState());
	}
	
	@Override
	public void simpleInitApp(){
		try{
			myClient = Network.connectToServer("127.0.0.1", GeneralUtilities.getPort());
			myClient.start();
		}
		catch(IOException e){
			LogHelper.severe("Error on client start", e);
		}
	}
	
	
	public void simpleInitApp(String IP){
		try{
			myClient = Network.connectToServer(IP, GeneralUtilities.getPort());
			myClient.start();
		}
		catch(IOException e){
			LogHelper.severe("Error on client start", e);
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