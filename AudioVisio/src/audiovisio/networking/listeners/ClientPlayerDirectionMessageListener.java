package audiovisio.networking.listeners;

import java.util.concurrent.Callable;

import audiovisio.entities.Player;
import audiovisio.networking.messages.PlayerUpdateMessage;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ClientPlayerDirectionMessageListener implements MessageListener<Client>{
		//runs separate thread from renderer
		audiovisio.networking.Client myClient;
		audiovisio.networking.Server myServer;
		
		/**
		 * Constructor
		 * @param myClient The Client for handling operations
		 */
		public ClientPlayerDirectionMessageListener(audiovisio.networking.Client myClient){
			this.myClient = myClient;
		}
		
		/**
		 * Handling of correct message type
		 * @Param source The source of the message (from client)
		 * @Param m Generic typed Message to handle
		 */
		@Override
		public void messageReceived(Client source, Message m){
			if(m instanceof PlayerUpdateMessage){
				
			}
	
		}
	
}