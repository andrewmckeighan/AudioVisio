package audiovisio.networking.utilities;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

public class MessageHandler implements MessageListener<HostedConnection>{

	@Override
	public void messageReceived(Client source, Message m) {
		// TODO Auto-generated method stub
		if(m instanceof NetworkMessage){
			NetworkMessageHandler((NetworkMessage)m, );
		}
		
	}
	
	public void NetworkMessageHandler(NetworkMessage handle, Server source){
		source.broadcast(handle);
	}

}
