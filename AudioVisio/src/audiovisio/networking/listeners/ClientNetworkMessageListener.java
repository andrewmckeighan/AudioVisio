package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ClientNetworkMessageListener implements MessageListener<Client>{
		//runs separate thread from renderer
		audiovisio.networking.Client myClient;
		audiovisio.networking.Server myServer;
		
		/**
		 * Constructor
		 * @param myClient The Client for handling operations
		 */
		public ClientNetworkMessageListener(audiovisio.networking.Client myClient){
			this.myClient = myClient;
		}
		
		/**
		 * Ensures handling of correct message type
		 * @param source The source of the message (from client)
		 * @param m Generic typed Message to handle
		 */
		@Override
		public void messageReceived(Client source, Message m){
			if(m instanceof NetworkMessage){
				
			}
	
		}
		
}
