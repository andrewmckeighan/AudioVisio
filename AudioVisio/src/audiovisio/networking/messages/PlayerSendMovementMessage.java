package audiovisio.networking.messages;

import audiovisio.utils.LogHelper;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * Sent by the client to the server when the player is moving.
 * 
 * When the player is moved on the client (one of the movement
 * keys is pressed), the client sends this message to the server
 * telling it what direction it is traveling in.
 */
@Serializable
public class PlayerSendMovementMessage extends PhysicsSyncMessage{
    private Vector3f location;
    private Vector3f direction;

    public PlayerSendMovementMessage(){
        this.setReliable(false);
    }

    /**
     * @param direction The direction the player is currently moving
     */

    public PlayerSendMovementMessage( Vector3f location, Vector3f direction ){
        this.location = location;
        this.direction = direction;

        this.setReliable(false);
    }

    public Vector3f getlocation(){
        return this.location;
    }

    public Vector3f getDirection(){
        return this.direction;
    }

    @Override
    public String toString() {
        return String.format("[" + this.location + ":" + this.direction + "]");
    }

    @Override
    public void applyData( Object player ){
        LogHelper.warn("This message is not used! Please switch to SyncCharacterMessage!");
        //((Player) player).update(this.location, this.direction);

    }
}
