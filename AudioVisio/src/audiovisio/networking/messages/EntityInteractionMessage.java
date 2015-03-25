package audiovisio.networking.messages;

import audiovisio.entities.InteractableEntity;
import audiovisio.utils.LogHelper;

import java.util.List;

/**
 * This message sends when an interactable object is triggered by the user.
 *
 * The object sent to this.applyData should be the object the user interacted with!
 *      (not the object connected to the original!)
 *
 * The object stores a list of objects, and is passed a list of indexes to match with the list.
 * The object updates itself, then the linked objects based on the list given.
 */
public class EntityInteractionMessage extends PhysicsSyncMessage {

    private List<Long> interactionList;
    private long entitiyId;
    private Boolean state;

    public EntityInteractionMessage() {}

    public EntityInteractionMessage(long id, long entityId,  List<Long> interactionList, Boolean state) {
        this.syncId = id;
        this.entitiyId = entityId;
        this.interactionList = interactionList;
        this.state = state;
    }

    @Override
    public void applyData(Object triggeredEntity) {
        assert triggeredEntity instanceof InteractableEntity;

        LogHelper.finer("EntityInteractionMessage: " + this + ":" + triggeredEntity);
        ((InteractableEntity) triggeredEntity).update(this.interactionList, this.state);
    }
}
