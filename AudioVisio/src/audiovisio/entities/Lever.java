package audiovisio.entities;

import audiovisio.level.Level;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.states.ClientAppState;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import org.json.simple.JSONObject;

public class Lever extends InteractableEntity {
    public static final  String     KEY_EDGE  = "edge";
    public static final  String     KEY_ISON  = "isOn";
    public static final  Quaternion ANGLE     = new Quaternion().fromAngles((float) -(Math.PI / 6.0), (float) (Math.PI / 2.0), 0);
    private static final Quaternion ROTATION  = new Quaternion().fromAngles(0, (float) (Math.PI / 2.0), 0);
    private static final float      MASS      = 0.0F;
    private static final Box        SHAPE     = new Box(0.05F * Level.SCALE.getX(),
            0.15F * Level.SCALE.getY(),
            0.05F * Level.SCALE.getZ());
    private static final Vector3f   OFFSET    = new Vector3f(0.0F, Level.SCALE.getY() / 2.0F, 0.0F);
    private static final Quaternion OFF_ANGLE = new Quaternion().fromAngles((float) -(Math.PI / 6.0), (float) (Math.PI / 2.0), 0);

    private Direction direction;

    private Boolean isOn;

    public Lever(){
        this(new Vector3f(0f, 0f, 0f));
    }

    public Lever( Vector3f location ){
        this.location = location;
        this.isOn = false;
    }

    public Lever( float x, float y, float z ){
        this(new Vector3f(x, y, z));
    }

    public Lever( boolean stuck ){
        super(stuck);
    }

    @Override
    public void init( AssetManager assetManager ){
        Box shape = Lever.SHAPE;
        this.geometry = new Geometry("level", shape);
        this.geometry.setLocalRotation(Lever.ANGLE);

        if (this.direction == Direction.NORTH){
            this.location = this.location.add(0.5F, 0.0F, 0.0F);
        } else if (this.direction == Direction.SOUTH){
            this.location = this.location.add(-0.5F, 0.0F, 0.0F);
        } else if (this.direction == Direction.EAST){
            this.location = this.location.add(0.0F, 0.0F, -0.5F);
            this.geometry.setLocalRotation(Lever.ROTATION);//Probably in Radians
        } else if (this.direction == Direction.WEST){
            this.location = this.location.add(0.0F, 0.0F, -0.5F);
            this.geometry.setLocalRotation(Lever.ROTATION);//Probably in Radians
        }
        this.location = this.location.mult(Level.SCALE);
        this.location = this.location.add(Lever.OFFSET);

        this.geometry.setLocalTranslation(this.location);

        Material randomMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        randomMaterial.setColor("Color", ColorRGBA.randomColor());
//        this.material = randomMaterial;
        this.geometry.setMaterial(randomMaterial);

        this.physics = new RigidBodyControl(Lever.MASS);//TODO: this might not be needed if we don't want collision detection
        this.attachChild(this.geometry);
        this.addControl(this.physics);
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        this.rootNode = rootNode;
        this.physicsSpace = physics;
        if (!ClientAppState.isAudio){
            rootNode.attachChild(this);
        }
        physics.add(this);
    }

    @Override
    public LevelNode getLevelNode(){
        LevelNode root = new LevelNode(String.format("#%d lever @ %s", this.ID, this.location), true);
        LevelNode typeNode = new LevelNode("Type", "lever", true);
        LevelNode idNode = new LevelNode("ID", this.ID, false);
        LevelNode nameNode = new LevelNode("Name", this.name, false);
        LevelNode stateNode = new LevelNode("Is On", this.isOn, false);
        LevelNode locationNode = LevelUtils.vector2node(this.location);

        root.add(typeNode);
        root.add(idNode);
        root.add(nameNode);
        root.add(stateNode);
        root.add(locationNode);

        return root;
    }

    @Override
    public void load( JSONObject loadObj ){
        super.load(loadObj);
        this.isOn = (Boolean) loadObj.get(Lever.KEY_ISON);
        this.direction = Direction.valueOf((String) loadObj.get(Lever.KEY_EDGE));
    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);
        codeObj.put(JSONHelper.KEY_TYPE, "lever");

        codeObj.put(Lever.KEY_ISON, this.isOn);
        codeObj.put(Lever.KEY_EDGE, this.direction.toString());
    }

    public void setOn( boolean state ){
        this.isOn = state;
    }

    public void switchLever(){
        this.isOn = !this.isOn;
        if (this.isOn){
            this.turnedOnEvent();
            this.geometry.setLocalRotation(Lever.ANGLE);
        }
        if (!this.isOn){
            this.turnedOffEvent();
            this.geometry.setLocalRotation(Lever.OFF_ANGLE);
        }
    }

    private void turnedOnEvent(){

    }

    private void turnedOffEvent(){

    }
}