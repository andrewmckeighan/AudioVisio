package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

public class ServerNetworkMessageListener implements MessageListener<Server>{
	
	audiovisio.networking.Server myServer;
	

	public ServerNetworkMessageListener(audiovisio.networking.Server myServer){
		this.myServer = myServer;
	}

	@Override
	public void messageReceived(Server myServer, Message m){
		if(m instanceof NetworkMessage){
			NetworkMessageHandler((NetworkMessage) m);
		}

	}
	
	public void NetworkMessageHandler(NetworkMessage handle) {
		
	}

}
