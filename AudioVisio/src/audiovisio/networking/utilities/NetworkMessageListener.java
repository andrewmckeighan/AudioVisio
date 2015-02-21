package audiovisio.networking.utilities;

import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class NetworkMessageListener implements MessageListener{
	
	private class NetworkMessageListener implements MessageListener<Client>{
		//runs seperate thread from renderer
		public void messageRecieved(Client source, Message m){
			if(m instanceof NetworkMessage){
				NetworkMessage message = (NetworkMessage) m;
			}
			
			messageQueue.add(message.getMessage());
			
		}
		
	}

	@Override
	public void messageReceived(Object arg0, Message arg1) {
		// TODO Auto-generated method stub
		
	}

}
