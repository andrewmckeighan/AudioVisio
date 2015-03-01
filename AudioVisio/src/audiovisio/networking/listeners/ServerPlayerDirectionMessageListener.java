package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerUpdateMessage;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

public class ServerPlayerDirectionMessageListener implements MessageListener<Server>{
	
	audiovisio.networking.Server myServer;
	
	/**
	 * Constructor
	 * @param myServer The Server for handling operations
	 */
	public ServerPlayerDirectionMessageListener(audiovisio.networking.Server myServer){
		this.myServer = myServer;
	}

	/**
	 * Ensures handling of correct message type
	 * @Param myServer source The source of the message (from server)
	 * @Param m Generic typed Message to handle
	 */
	@Override
	public void messageReceived(Server myServer, Message m){
		if(m instanceof PlayerUpdateMessage){
			
		}

	}
	
}