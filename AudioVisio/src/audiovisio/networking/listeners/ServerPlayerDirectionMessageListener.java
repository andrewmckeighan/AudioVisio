package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerDirectionMessage;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

public class ServerPlayerDirectionMessageListener implements MessageListener<Server>{
	
	audiovisio.networking.Server myServer;
	

	public ServerPlayerDirectionMessageListener(audiovisio.networking.Server myServer){
		this.myServer = myServer;
	}

	@Override
	public void messageReceived(Server myServer, Message m){
		if(m instanceof PlayerDirectionMessage){
			PlayerDirectionMessageHandler((PlayerDirectionMessage) m);
		}

	}
	
	public void PlayerDirectionMessageHandler(PlayerDirectionMessage handle) {
		
	}

}