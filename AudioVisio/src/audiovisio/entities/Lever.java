package audiovisio.entities;

import com.jme3.scene.shape.Box;

public class Lever extends InteractableEntity {

    private Box shape;
    private Boolean isOn;

    public Lever(){
        this.isOn = false;
    }

    public void switchLever(){
        this.isOn = !this.isOn;
        if(this.isOn){
            this.turnedOnEvent();
        }
        if(!this.isOn){
            this.turnedOffEvent();
        }
    }

    private void turnedOnEvent(){

    }

    private void turnedOffEvent(){

    }

}