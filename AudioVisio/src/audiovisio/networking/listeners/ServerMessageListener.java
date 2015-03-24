package audiovisio.networking.listeners;

import audiovisio.networking.messages.NetworkMessage;
import audiovisio.utils.LogHelper;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ServerMessageListener implements MessageListener<HostedConnection> {
	audiovisio.states.ServerAppState myServer;

	public ServerMessageListener(audiovisio.states.ServerAppState myServer) {
		this.myServer = myServer;
	}

	@Override
	public void messageReceived(HostedConnection source, Message message) {
		if (message instanceof NetworkMessage) {

		}
		LogHelper.warn("Received unknown message type: "
				+ message.getClass().getName());
	}

}
