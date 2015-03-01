package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerMoveMessage;
import audiovisio.networking.messages.PlayerUpdateMessage;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Client;

public class ClientMessageListener implements MessageListener<Client>{

	public ClientMessageListener(){
		
	}
	
	@Override
	public void messageReceived(Client client, Message message) {
		if(message instanceof PlayerMoveMessage){
			
		}
		if(message instanceof PlayerUpdateMessage){
					
				}
		if(message instanceof NetworkMessage){
			
		}
		
	}

}
