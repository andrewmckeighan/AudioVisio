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
public class PlayerLocationMessage extends AbstractMessage {
    private int      playerID;
    private Vector3f location;
    private Vector3f direction;

    public PlayerLocationMessage(){
        this.setReliable(false);
    }

    /**
     * @param playerID The ID of the player being updated with this message
     * @param direction The direction the player is moving
     * @param location The location the player is currently in
     */
    public PlayerLocationMessage( int playerID, Vector3f location, Vector3f direction ){
        this.playerID = playerID;
        this.location = location;
        this.direction = direction;

        this.setReliable(false);
    }

    public int getPlayerID(){
        return this.playerID;
    }

    public Vector3f getDirection(){
        return this.direction;
    }

    public Vector3f getlocation(){
        return this.location;
    }

    @Override
    public String toString() {
        return String.format(this.playerID + ":" + this.location + ":" + this.direction);
    }

}
