package audiovisio.networking.listeners;

import audiovisio.entities.Player;
import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;
import audiovisio.utils.LogHelper;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ServerMessageListener implements MessageListener<HostedConnection> {
	audiovisio.networking.Server myServer;

	public ServerMessageListener(audiovisio.networking.Server myServer) {
		this.myServer = myServer;
	}

	@Override
	public void messageReceived(HostedConnection source, Message message) {
		if (message instanceof NetworkMessage) {

		}
//		if (message instanceof PlayerSendMovementMessage) {
//			LogHelper.info("Server Received PlayerSendMovementMessage: " + message);
//			PlayerSendMovementMessage msg = (PlayerSendMovementMessage) message;
//			Player playerToUpdate = myServer.getPlayer(source.getId());
//			LogHelper.info("player's old info: " + playerToUpdate.getLocalTranslation() + ", " + playerToUpdate.characterControl.getWalkDirection());
//			playerToUpdate.update(msg);
//			LogHelper.info("player's new info: " + playerToUpdate.getLocalTranslation() + ", " + playerToUpdate.characterControl.getWalkDirection());
//			return;
//		}
		LogHelper.warn("Received unknown message type: "
				+ message.getClass().getName());
	}

}
