package audiovisio.networking.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Sent by the server to the client when the player has moved.
 * 
 * When the server updates the location of a player it sends the
 * client this message telling the client which player it is,
 * which direction the player is walking, and where the player is.
 * 
 * This message is also used by the server to tell the clients where
 * the other players are.
 */
@Serializable
public class PlayerMoveMessage extends AbstractMessage {
	private Vector3f position;
	private Vector3f direction;
	private int ID;
	
	public PlayerMoveMessage() {}
	
	/**
	 * @param playerID The ID of the player being updated with this message
	 * @param direction The direction the player is moving
	 * @param position The position the player is currently in
	 */
	public PlayerMoveMessage(int playerID, Vector3f direction, Vector3f position) {
		this.direction = direction;
		this.position = position;
	}
	
	public Vector3f getDirection() {
		return direction;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}
}
