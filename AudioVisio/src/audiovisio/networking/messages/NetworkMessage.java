package audiovisio.networking.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class NetworkMessage extends AbstractMessage{
	
	/**
	 * message contents
	 */
	private String message;
	
	/**
	 * Default constructor- empty
	 */
	public NetworkMessage(){
		setReliable(true);
	}
	/**
	 * Constructor
	 * @param message Message contents
	 */
	public NetworkMessage(String message){
		this.message = message;
	}
	
	/**
	 * @return Message contents
	 */
	public String getMessage(){
		return message;
	}
}
