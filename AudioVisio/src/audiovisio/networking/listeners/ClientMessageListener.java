package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerJoinMessage;
import audiovisio.networking.messages.PlayerLeaveMessage;
import audiovisio.networking.messages.PlayerListMessage;
import audiovisio.networking.messages.PlayerLocationMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;
import audiovisio.utils.LogHelper;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

public class ClientMessageListener implements MessageListener<Client> {
	audiovisio.networking.Client myClient;

	public ClientMessageListener(audiovisio.networking.Client myClient) {
		this.myClient = myClient;
	}

	@Override
	public void messageReceived(Client client, Message message) {
		if (message instanceof NetworkMessage) {

		}
		if (message instanceof PlayerLocationMessage) {
			LogHelper.info("Received PlayerLocationMessage");
			PlayerLocationMessage msg = (PlayerLocationMessage) message;
			//myClient.getPlayer().update(msg);
			myClient.updatePlayer(msg);
			return;
		}
		if (message instanceof PlayerJoinMessage) {
			LogHelper.info("Received PlayerJoinMessage");
			PlayerJoinMessage msg = (PlayerJoinMessage) message;
			LogHelper.info("Player " + msg.getPlayerID() + " has joined");
			return;
		}
		if (message instanceof PlayerLeaveMessage) {
			LogHelper.info("Received PlayerLeaveMessage");
			PlayerLeaveMessage msg = (PlayerLeaveMessage) message;
			LogHelper.info("Player " + msg.getPlayerID() + " has left");
			return;
		}
		if (message instanceof PlayerListMessage) {
			PlayerListMessage msg = (PlayerListMessage) message;
			LogHelper.info("Received player list of size: " + msg.getPlayerList().length);
			return;
		}
		LogHelper.warn("Received message of unknown type: " + message.getClass().getName());
	}

}
