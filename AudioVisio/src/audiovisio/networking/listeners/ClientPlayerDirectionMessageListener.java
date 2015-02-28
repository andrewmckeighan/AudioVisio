package audiovisio.networking.listeners;

import java.util.concurrent.Callable;

import audiovisio.entities.Player;
import audiovisio.networking.messages.PlayerDirectionMessage;

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
			if(m instanceof PlayerDirectionMessage){
				PlayerDirectionMessageHandler((PlayerDirectionMessage) m);
			}
	
		}
		
		public void PlayerDirectionMessageHandler(final PlayerDirectionMessage handle){
			
		}

}