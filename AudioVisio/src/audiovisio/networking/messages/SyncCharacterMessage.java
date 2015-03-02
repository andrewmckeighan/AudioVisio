package audiovisio.networking.messages;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;

@Serializable
public class SyncCharacterMessage extends PhysicsSyncMessage {

    public Vector3f location = new Vector3f();
    public Vector3f walkDirection = new Vector3f();
    public Vector3f viewDirection = new Vector3f();

    public SyncCharacterMessage() {}

    public SyncCharacterMessage(long id, BetterCharacterControl character) {
        this.syncId = id;
        //location
        this.walkDirection.set(character.getWalkDirection());
        this.viewDirection.set(character.getViewDirection());
    }

    public void readData(BetterCharacterControl character) {
        this.walkDirection.set(character.getWalkDirection());
        this.viewDirection.set(character.getViewDirection());
    }

    public void applyData(Object character) {
        ((Spatial) character).getControl(BetterCharacterControl.class).warp(location);
        ((Spatial) character).getControl(BetterCharacterControl.class).setWalkDirection(walkDirection);
        ((Spatial) character).getControl(BetterCharacterControl.class).setViewDirection(viewDirection);
    }
}
