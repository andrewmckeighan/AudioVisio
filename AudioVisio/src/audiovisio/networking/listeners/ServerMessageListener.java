package audiovisio.networking.listeners;

import audiovisio.entities.Player;
import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;
import audiovisio.utils.LogHelper;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ServerMessageListener implements MessageListener<Client> {
	audiovisio.networking.Server myServer;

	public ServerMessageListener(audiovisio.networking.Server myServer) {
		this.myServer = myServer;
	}

	@Override
	public void messageReceived(Client client, Message message) {
		if (message instanceof PlayerSendMovementMessage) {
			PlayerSendMovementMessage msg = (PlayerSendMovementMessage) message;
			Player player = myServer.getPlayer(client.getId());
			player.update(msg);
			return;
		}
		if (message instanceof NetworkMessage) {

		}
		LogHelper.warn("Received unknown message type: " + message.getClass().getName());
	}

}
