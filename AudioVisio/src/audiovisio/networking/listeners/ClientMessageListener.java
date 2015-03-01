package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerJoinMessage;
import audiovisio.networking.messages.PlayerLeaveMessage;
import audiovisio.networking.messages.PlayerListMessage;
import audiovisio.networking.messages.PlayerLocationMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;
import audiovisio.utils.LogHelper;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

public class ClientMessageListener implements MessageListener<Server> {
	audiovisio.networking.Client myClient;

	public ClientMessageListener(audiovisio.networking.Client myClient) {
		this.myClient = myClient;
	}

	@Override
	public void messageReceived(Server server, Message message) {
		if (message instanceof NetworkMessage) {

		}
		if (message instanceof PlayerLocationMessage) {
			PlayerLocationMessage msg = (PlayerLocationMessage) message;
			myClient.getPlayer().update(msg);
			return;
		}
		if (message instanceof PlayerJoinMessage) {
			
		}
		if (message instanceof PlayerLeaveMessage) {

		}
		if (message instanceof PlayerListMessage) {
			PlayerListMessage msg = (PlayerListMessage) message;
			LogHelper.info("Received player list, punk");
		}
		LogHelper.warn("Received message of unknown type: " + message.getClass().getName());
	}

}
