package audiovisio.networking.listeners;

import audiovisio.entities.Player;
import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;
import audiovisio.utils.LogHelper;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ServerMessageListener implements MessageListener<HostedConnection> {
	audiovisio.networking.Server myServer;

	public ServerMessageListener(audiovisio.networking.Server myServer) {
		this.myServer = myServer;
	}

	@Override
	public void messageReceived(HostedConnection client, Message message) {
		if (message instanceof NetworkMessage) {

		}
		if (message instanceof PlayerSendMovementMessage) {
			PlayerSendMovementMessage msg = (PlayerSendMovementMessage) message;
			Player player = myServer.getPlayer(client.getId());
			player.update(msg);
			return;
		}
		LogHelper.warn("Received unknown message type: "
				+ message.getClass().getName());
	}

}
