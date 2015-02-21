package audiovisio.networking.utilities;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class NetworkMessageListener implements MessageListener<Client>{
		//runs separate thread from renderer
		audiovisio.networking.Client myClient;
	
		public NetworkMessageListener(audiovisio.networking.Client myClient){
			this.myClient = myClient;
		}
	 
		@Override
		public void messageReceived(Client source, Message m){
			if(m instanceof NetworkMessage){
				NetworkMessage message = (NetworkMessage) m;
				myClient.messageQueue.add(message.getMessage());
			}
	
		}

}
