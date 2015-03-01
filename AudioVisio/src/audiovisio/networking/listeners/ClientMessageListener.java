package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerMoveMessage;
import audiovisio.networking.messages.PlayerUpdateMessage;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

public class ClientMessageListener implements MessageListener<Server>{

	public ClientMessageListener(){
		
	}
	
	@Override
	public void messageReceived(Server server, Message message) {
		if(message instanceof PlayerMoveMessage){
			
		}
		if(message instanceof PlayerUpdateMessage){
					
				}
		if(message instanceof NetworkMessage){
			
		}
		
	}

}
