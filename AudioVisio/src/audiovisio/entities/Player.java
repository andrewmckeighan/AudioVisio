package audiovisio.entities;

public class Player extends MovingEntity {

    private Vector3f frontCam;
    private Vector3f leftCam;
    private Vector3f walkDirection;
    private Spatial model;
    private DirectionalLight light;
    private CharacterControl characterControl;


    public Player(){

    }

    public setWalkDirection(Vector3f direction){
        this.walkDirection = direction;
        characterControl.setWalkDirection(direction);
        cam.setLocation(characterControl.getPhysicsLocation());
    }

}