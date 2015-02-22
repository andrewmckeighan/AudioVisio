package audiovisio.networking.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

@Serializable
public class NetworkMessage extends AbstractMessage{

	private String message;
	
	public NetworkMessage(){}
	
	public NetworkMessage(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
	
	public static void initializeSerializables(){
		Serializer.registerClass(NetworkMessage.class);
	}
	
}
