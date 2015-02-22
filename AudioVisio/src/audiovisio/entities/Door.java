package audiovisio.entities;

public class Door extends InteractableEntity {
    private Boolean isOpen;

    public Door(){
        this.isOpen = false;
    }

    public Door(Boolean open){
        this.isOpen = open;
    }

    public Door(Boolean open, Boolean stuck){
        this.isOpen = open;
        this.stuck = stuck;
    }

    public void load(JSONObject obj){
        super.load(obj);

        this.isOpen = (Boolean) obj.get("isOpen");
    }

    private void onTriggeredEvent(){
        if(!this.stuck){
            this.isOpen = !this.isOpen;
        }
    }

}