package audiovisio.entities;

public class Door extends InteractableEntity {
    private boolean open;

    public Door(){
        this.open = false;
    }

    public Door(boolean open){
        this.open = open;
    }

    public Door(boolean open, boolean stuck){
        this.open = open;
        this.stuck = stuck;
    }

    private void onTriggeredEvent(){
        if(!this.stuck){
            this.open = !this.open;
        }
    }

}