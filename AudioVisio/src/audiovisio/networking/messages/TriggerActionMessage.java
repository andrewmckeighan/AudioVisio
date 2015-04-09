package audiovisio.networking.messages;

import audiovisio.entities.InteractableEntity;
import audiovisio.utils.LogHelper;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * This message sends when an interactable object is triggered by the user.
 *
 * The object sent to this.applyData should be the object the user interacted with!
 * (not the object connected to the original!)
 *
 * The object stores a list of objects, and is passed a list of indexes to match with the list.
 * The object updates itself, then the linked objects based on the list given.
 */
@Serializable
public class TriggerActionMessage extends PhysicsSyncMessage {

    private Boolean state;

    public TriggerActionMessage(){}

    public TriggerActionMessage( long id, Boolean state, Vector3f location ){
        this.syncId = id;
        this.state = state;
        this.location = location;
    }

    public Boolean getState(){
        return this.state;
    }

    @Override
    public void applyData( Object triggeredEntity ){
        assert triggeredEntity instanceof InteractableEntity;
        InteractableEntity interactableEntity = (InteractableEntity) triggeredEntity;
        LogHelper.finer("TriggerActionMessage: " + this + ":" + interactableEntity);
        interactableEntity.update(this);
    }

    public Vector3f location;
}
