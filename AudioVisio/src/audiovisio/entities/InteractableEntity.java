package audiovisio.entities;

public class InteractableEntity extends Entity {

    private InteractableEntity linkedEntity;
    public boolean stuck; //if the entity keeps it state regardless of triggerEvents

    public InteractableEntity(){
        this(false);
    }

    public InteractableEntity(boolean stuck){

    }

    public void triggerEvent(){
        linkedEntity.onTriggeredEvent();
    }

    private void onTriggeredEvent() {
		// TODO Auto-generated method stub
		
	}

	public Entity getLinkedEntity(){
        return this.linkedEntity;
    }

    public void setLinkedEntity(InteractableEntity entity){
        this.linkedEntity = entity;
    }

}