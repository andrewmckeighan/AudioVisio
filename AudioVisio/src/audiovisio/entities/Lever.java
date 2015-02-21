package audiovisio.entities;

public class Lever extends InteractableEntity {

    private Box shape;
    private Bool isOn;

    public Lever(){
        this.isOn = false;
    }

    public void switch(){
        this.isOn = !this.isOn;
        if(this.isOn){
            this.turnedOnEvent();
        }
        if(this.isOff){
            this.turnedOffEvent();
        }
    }

    private void turnedOnEvent(){

    }

    private void turnedOffEvent(){

    }

}