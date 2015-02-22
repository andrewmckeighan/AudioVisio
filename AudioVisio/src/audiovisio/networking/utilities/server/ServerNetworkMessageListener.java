package audiovisio.networking.utilities.server;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

public class ServerNetworkMessageListener implements MessageListener<Server>{
	
	audiovisio.networking.Server myServer;
	

	public ServerNetworkMessageListener(audiovisio.networking.Server myServer){
		this.myServer = myServer;
	}


	@Override
	public void messageReceived(Server myServer, Message m) {
		// TODO Auto-generated method stub
		myServer.broadcast(m);
	}

}
