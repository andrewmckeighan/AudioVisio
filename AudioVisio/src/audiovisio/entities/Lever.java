package audiovisio.entities;

import audiovisio.level.Level;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import org.json.simple.JSONObject;

public class Lever extends InteractableEntity {
    public static final String KEY_ISON = "isOn";
    public static final  float      ANGLE    = (float) (Math.PI / 6.0);
    private static final Quaternion ROTATION = new Quaternion().fromAngles(0, (float) Math.PI / 2, 0);
    private static final float      MASS     = 0.0F;
    private static final Box        SHAPE    = new Box(0.1F * Level.SCALE.getX(),
            0.3F * Level.SCALE.getY(),
            0.1F * Level.SCALE.getZ());

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
        this.geometry.setLocalRotation(new Quaternion()
                .fromAngles(Lever.ANGLE, 0, 0));

        this.geometry.setLocalTranslation(this.location);
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        this.physics = new RigidBodyControl(Lever.MASS);
        this.geometry.addControl(this.physics);
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
    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);
        codeObj.put(JSONHelper.KEY_TYPE, "lever");

        codeObj.put(Lever.KEY_ISON, this.isOn);
    }

    public void setOn( boolean state ){
        this.isOn = state;
    }

    public void switchLever(){
        this.isOn = !this.isOn;
        if (this.isOn){
            this.turnedOnEvent();
            this.geometry.setLocalRotation(new Quaternion().fromAngles(0, Lever.ANGLE, 0));
        }
        if (!this.isOn){
            this.turnedOffEvent();
            this.geometry.setLocalRotation(new Quaternion().fromAngles(0, -Lever.ANGLE, 0));
        }
    }

    private void turnedOnEvent(){

    }

    private void turnedOffEvent(){

    }
}