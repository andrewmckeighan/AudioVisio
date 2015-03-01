package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerLocationMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

public class ClientMessageListener implements MessageListener<Server>{

	public ClientMessageListener(){
		
	}
	
	@Override
	public void messageReceived(Server server, Message message) {
		if(message instanceof PlayerLocationMessage){
			
		}
		if(message instanceof PlayerSendMovementMessage){
					
				}
		if(message instanceof NetworkMessage){
			
		}
		
	}

}
