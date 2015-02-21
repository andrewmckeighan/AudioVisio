package audiovisio.entities;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class Player extends MovingEntity {

    private Vector3f frontCam;
    private Vector3f leftCam;
    private Vector3f moveDirection;
    private Spatial model;
    private DirectionalLight light;
    private CharacterControl characterControl;


    public Player(){

    }

    public void setMoveDirection(Vector3f direction){
        this.moveDirection = direction;
        characterControl.setWalkDirection(direction);
        //cam.setLocation(characterControl.getPhysicsLocation());
    }

}