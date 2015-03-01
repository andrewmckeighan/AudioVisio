package audiovisio.networking.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Sent by the client to the server when the player is moving.
 * 
 * When the player is moved on the client (one of the movement
 * keys is pressed), the client sends this message to the server
 * telling it what direction it is traveling in.
 */
@Serializable
public class PlayerSendMovementMessage extends AbstractMessage{
	private Vector3f direction;
	
	public PlayerSendMovementMessage(){
		setReliable(false);
	}

	/**
	 * @param direction The direction the player is currently moving
	 */

	public PlayerSendMovementMessage(Vector3f direction, Vector3f camDir, Vector3f camLeft){
		setReliable(false);
		this.direction = direction;
	}
	
	public Vector3f getDirection(){
		return direction;
	}
}
