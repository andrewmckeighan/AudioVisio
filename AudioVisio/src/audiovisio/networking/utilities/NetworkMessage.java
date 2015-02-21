package audiovisio.networking.utilities;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

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
	
}
