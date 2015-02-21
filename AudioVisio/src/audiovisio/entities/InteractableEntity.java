package audiovisio.entities;

public class InteractableEntity extends Entity {

    private Entity linkedEntity;
    private boolean stuck; //if the entity keeps it state regardless of triggerEvents

    public InteractableEntity(){
        this(false);
    }

    public InteractableEntity(boolean stuck){

    }

    public void triggerEvent(){
        linkedEntity.onTriggeredEvent();
    }

    public Entity getLinkedEntity(){
        return this.linkedEntity;
    }

    public setLinkedEntity(Entity entity){
        this.linkedEntity = entity;
    }

}