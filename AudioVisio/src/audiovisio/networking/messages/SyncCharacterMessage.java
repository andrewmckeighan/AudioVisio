package audiovisio.networking.messages;

import audiovisio.entities.Player;
import audiovisio.utils.LogHelper;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;


@Serializable
public class SyncCharacterMessage extends PhysicsSyncMessage {

    public Vector3f location = new Vector3f();
    public Vector3f walkDirection = new Vector3f();
    public Quaternion rotation = new Quaternion();

    public SyncCharacterMessage() {}

    public SyncCharacterMessage(long id, Vector3f location, Vector3f walkDirection, Quaternion rotation) {
        this.syncId = id;
        this.location = location;
        this.walkDirection = walkDirection;
        this.rotation = rotation;

    }

    public void readData(BetterCharacterControl character, Vector3f location, Vector3f walkDirection, Quaternion rotation) {
        this.location = location;
        this.walkDirection.set(character.getWalkDirection());
        this.rotation = rotation;
        this.walkDirection = walkDirection;
    }

    public void applyData(Object character) {
        assert  character instanceof Player;
        assert ((Player) character).getID() == this.syncId;

        LogHelper.finer("SyncCharacterMessage.applyData " + this + ":" + character);
        ((Player) character).update(this.location, this.walkDirection, this.rotation);
    }

    @Override
    public String toString() {
        return String.format("Obj: " + this.syncId + ", located: " + this.location + ", walking: " + this.walkDirection + ", looking: " + this.rotation);
    }
}
