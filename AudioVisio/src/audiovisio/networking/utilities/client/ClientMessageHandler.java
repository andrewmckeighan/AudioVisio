package audiovisio.networking.utilities.client;

import audiovisio.networking.utilities.NetworkMessage;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

public class ClientMessageHandler implements MessageListener<HostedConnection>{
	
	audiovisio.networking.Client myClient;

	@Override
	public void messageReceived(HostedConnection arg0, Message m) {
		// TODO Auto-generated method stub
	}	
	
	public void NetworkMessageHandler(NetworkMessage handle){
		//TODO 
	}

}
