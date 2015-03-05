package audiovisio.networking.messages;

import audiovisio.entities.Player;

import audiovisio.utils.LogHelper;
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

    public SyncCharacterMessage(long id, BetterCharacterControl character, Vector3f location) {
        this.syncId = id;
        this.location = location;
        this.walkDirection.set(character.getWalkDirection());
        this.viewDirection.set(character.getViewDirection());
    }

    public void readData(BetterCharacterControl character, Vector3f location) {
        this.location = location;
        this.walkDirection.set(character.getWalkDirection());
        this.viewDirection.set(character.getViewDirection());
    }

    public void applyData(Object character) {
        LogHelper.info("Obj: " + syncId + " located " + location + " walking " + walkDirection + " looking " + viewDirection);
        ((Spatial) character).getControl(BetterCharacterControl.class).setWalkDirection(walkDirection);
        ((Spatial) character).getControl(BetterCharacterControl.class).setViewDirection(viewDirection);
        ((Spatial) character).setLocalTranslation(location);
        ((Player) character).model.setLocalTranslation(((Spatial) character).getWorldTranslation());
    }
}
