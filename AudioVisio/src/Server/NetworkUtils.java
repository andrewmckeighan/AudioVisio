package Server;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

public class NetworkUtils {
	//port variable
	public static final int PORT = 6000;
	
	//initializes serializable variables, (server sends serializables, not sure what this means yet)
	public static void initializeSerializables(){
		Serializer.registerClass(NetworkMessage.class);
	}
	//creates a message for the network to send
	@Serializable
	public static class NetworkMessage extends AbstractMessage{
		
		private String message;
		
		public NetworkMessage(){}
		
		public NetworkMessage(String message){
			this.message = message;
		}
		
		public String getMessage(){
			return message;
		}
	}
}