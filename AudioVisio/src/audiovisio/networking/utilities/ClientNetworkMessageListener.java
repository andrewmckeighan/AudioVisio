package audiovisio.networking.utilities;

import audiovisio.networking.messages.NetworkMessage;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ClientNetworkMessageListener implements MessageListener<Client>{
		//runs separate thread from renderer
		audiovisio.networking.Client myClient;
		audiovisio.networking.Server myServer;
		
	
		public ClientNetworkMessageListener(audiovisio.networking.Client myClient){
			this.myClient = myClient;
		}
		
		@Override
		public void messageReceived(Client source, Message m){
			if(m instanceof NetworkMessage){
				NetworkMessageHandler((NetworkMessage) m);
			}
	
		}
		
		public void NetworkMessageHandler(NetworkMessage handle){
			myClient.messageQueue.add(handle.getMessage());
		}

}
