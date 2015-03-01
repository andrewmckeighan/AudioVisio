package audiovisio.networking.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

/**
 * Sent by the client to the server when the player is moving.
 * 
 * When the player is moved on the client (one of the movement
 * keys is pressed), the client sends this message to the server
 * telling it what direction it is traveling in.
 */
@Serializable
public class PlayerUpdateMessage extends AbstractMessage{
	private Vector3f direction;
	
	public PlayerUpdateMessage(){
		
	}

	/**
	 * @param direction The direction the player is currently moving
	 */
	public PlayerUpdateMessage(Vector3f direction){
		this.direction = direction;
	}
	
	public Vector3f getDirection(){
		return direction;
	}
}
