package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;
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
		if (message instanceof PlayerLocationMessage) {
			PlayerLocationMessage msg = (PlayerLocationMessage) message;
			myClient.getPlayer().update(msg);
			return;
		}
		if (message instanceof NetworkMessage) {

		}
		LogHelper.warn("Received message of unknown type: " + message.getClass().getName());
	}

}
