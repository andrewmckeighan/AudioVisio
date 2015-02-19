package audiovisio.entities;


/*
//creat geometry for our button
  Box box = new Box(2,2,2);
  Geometry buttonGeometry = new Geometry("button", box);
  buttonGeometry.setMaterial(pondMat);
  rootNode.attachChild(buttonGeometry);

  //position our button
  buttonGeometry.setLocalTranslation(new Vector3f(2f,2f,2f));

  //make button physics
  RigidBodyControl buttonPhysics = new RigidBodyControl(2f);

  //add button physics to our space
  buttonGeometry.addControl(buttonPhysics);
  bulletAppState.getPhysicsSpace().add(buttonPhysics);

  shootables = new Node("Shootables");
  rootNode.attachChild(shootables);
  shootables.attachChild(buttonGeometry);
 */
public class Button extends InteractableEntity {

    private Cylinder shape;
    private Geometry geometry;
    private Material material;
    //rootNode.attachChild(this.geometry);

    private Vector3f position;
    private RigidBodyControl collision; //(physics)
    //RigidBodyControl  (CollisionShape shape, float mass)
    //buttonGeometry.addControl(collision)

    public Button(){

    }

    public getGeometry(){
        return this.geometry;
    }

    public getPos(){
        return this.position;
    }

    public setPos(Vector3f position){
        this.position = position;
    }
}