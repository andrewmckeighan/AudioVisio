package audiovisio.networking.utilities;

import audiovisio.networking.messages.NetworkMessage;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ClientPlayerDirectionMessageListener implements MessageListener<Client>{
		//runs separate thread from renderer
		audiovisio.networking.Client myClient;
		audiovisio.networking.Server myServer;
		
	
		public ClientPlayerDirectionMessageListener(audiovisio.networking.Client myClient){
			this.myClient = myClient;
		}
		
		@Override
		public void messageReceived(Client source, Message m){
			if(m instanceof NetworkMessage){
				
			}
	
		}
		
		public void NetworkMessageHandler(NetworkMessage handle){
			
		}

}