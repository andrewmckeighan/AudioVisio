package audiovisio.networking.utilities;

import java.util.concurrent.Callable;

import audiovisio.networking.messages.PlayerDirectionMessage;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ClientPlayerDirectionMessageListener implements MessageListener<Client>{
		//runs separate thread from renderer
		audiovisio.networking.Client myClient;
		audiovisio.networking.Server myServer;
		
	
		public ClientPlayerDirectionMessageListener(audiovisio.networking.Client myClient){
			this.myClient = myClient;
		}
		
		@Override
		public void messageReceived(Client source, Message m){
			if(m instanceof PlayerDirectionMessage){
				PlayerDirectionMessageHandler((PlayerDirectionMessage) m);
			}
	
		}
		
		public void PlayerDirectionMessageHandler(final PlayerDirectionMessage handle){
			myClient.enqueue(new Callable()){
				
				public Object call() throws Exception{
					myClient.audioPlayer.setWalkDirection(handle.getAudioDirection());
					myClient.visualPlayer.setWalkDirection(handle.getVisualDirection());
					
					return null;
				}
				
			};
		}

}