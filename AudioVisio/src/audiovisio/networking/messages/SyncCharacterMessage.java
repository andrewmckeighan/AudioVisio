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

    public SyncCharacterMessage(long id, BetterCharacterControl character, Vector3f location, Vector3f walkDirection, Vector3f camDir) {
        this.syncId = id;
        this.location = location;
//        this.walkDirection.set(character.getWalkDirection());
//        this.viewDirection.set(character.getViewDirection());
        this.walkDirection = walkDirection;
    }

    public void readData(BetterCharacterControl character, Vector3f location, Vector3f walkDirection, Vector3f camDir) {
        this.location = location;
        this.walkDirection.set(character.getWalkDirection());
        //this.viewDirection.set(character.getViewDirection());
        this.viewDirection = camDir;
        this.walkDirection = walkDirection;
    }

    public void applyData(Object character) {
        LogHelper.info("SyncCharacterMessage.applyData " + this);
        ((Player) character).update(location, walkDirection);

//        ((Spatial) character).getControl(BetterCharacterControl.class).setWalkDirection(walkDirection);
//        ((Spatial) character).getControl(BetterCharacterControl.class).setViewDirection(viewDirection);
//        ((Spatial) character).setLocalTranslation(location);
//        ((Player) character).model.setLocalTranslation(((Spatial) character).getWorldTranslation());
    }

    @Override
    public String toString() {
        return String.format("[" + "Obj: " + this.syncId + ", located: " + this.location + ", walking: " + this.walkDirection + ", looking: " + this.viewDirection + "]");
    }
}
