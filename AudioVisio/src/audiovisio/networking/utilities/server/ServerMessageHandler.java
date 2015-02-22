package audiovisio.networking.utilities.server;

import audiovisio.networking.Client;
import audiovisio.networking.utilities.NetworkMessage;
import com.jme3.network.Message;

public class ServerMessageHandler {
	
	audiovisio.networking.Server myServer;
	
	public void messageReceived(Client myClient , Message m){
		if(m instanceof NetworkMessage){
			NetworkMessage message = (NetworkMessage) m;
			myClient.messageQueue.add(message.getMessage());
		}

	}

}
