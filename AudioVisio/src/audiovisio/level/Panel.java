package audiovisio.level;

import audiovisio.rsle.editor.LevelNode;
import audiovisio.rsle.editor.RSLESetter;
import audiovisio.states.ClientAppState;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import org.json.simple.JSONObject;

/**
 * Represents a panel in the world.
 */
public class Panel implements ILevelItem {

    public static final  ColorRGBA COLOR = ColorRGBA.DarkGray;
    private static final Box       SHAPE = new Box(0.5F * Level.SCALE.getX(),
            0.01F * Level.SCALE.getY(),
            0.5F * Level.SCALE.getZ());
    /**
     * The location of the panel on the grid.
     */
    public Vector3f location;
    protected long ID = -3;
    protected Geometry         geometry;
    protected RigidBodyControl physics;
    protected ColorRGBA        color;

    public Panel(){}

    public Panel( Vector3f location ){
        this.location = location;
    }

    /**
     * Load the panel from a JSONObject
     *
     * @param loadObj The JSONObject to load from
     */
    @Override
    public void load( JSONObject loadObj ){
        if (loadObj == null){
            throw new IllegalArgumentException("The JSONObject to load from cannot be null");
        }
        if (loadObj.isEmpty()){
            throw new IllegalArgumentException("The JSONObject to load from cannot be empty");
        }

        this.ID = (Long) loadObj.get(JSONHelper.KEY_ID);
        JSONObject location = (JSONObject) loadObj.get(JSONHelper.KEY_LOCATION);
        this.location = JSONHelper.readVector3f(location);

        this.color = JSONHelper.readColor((String) loadObj.get(JSONHelper.KEY_COLOR));
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        if (!ClientAppState.isAudio){
            rootNode.attachChild(this.geometry);
        }
        physics.add(this.physics);
    }

    @Override
    public void init( AssetManager assetManager ){
        Box shape = Panel.SHAPE;

        this.geometry = new Geometry("Panel" + this.ID, shape);

        this.geometry.setLocalTranslation(this.location.mult(Level.SCALE));

        Material randomMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        randomMaterial.setColor("Color", this.color);
        this.geometry.setMaterial(randomMaterial);

        this.physics = new RigidBodyControl(0);
        this.geometry.addControl(this.physics);
    }

    /**
     * Save the panel to a JSONObject
     *
     * @param codeObj The JSONObject to save to
     */
    @Override
    public void save( JSONObject codeObj ){
        if (codeObj == null){
            throw new IllegalArgumentException("The given JSONObject to save to cannot be null");
        }
        if (!codeObj.isEmpty()){
            throw new IllegalArgumentException("The JSONObject to save to must be empty");
        }

        codeObj.put(JSONHelper.KEY_ID, this.getID());
        codeObj.put(JSONHelper.KEY_TYPE, "panel");
        JSONObject location = new JSONObject();
        codeObj.put(JSONHelper.KEY_LOCATION, JSONHelper.saveVector3f(this.location));
        codeObj.put(JSONHelper.KEY_COLOR, JSONHelper.writeColor(this.color));
    }

    @Override
    public LevelNode getLevelNode(){
        LevelNode root = new LevelNode(String.format("#%d @ %s", this.ID, this.location), true);
        LevelNode typeNode = new LevelNode("Type", "panel", true);
        LevelNode idNode = new LevelNode("ID", this.ID, false);
        LevelNode colorNode = new LevelNode("Color", JSONHelper.writeColor(this.color), false);
        LevelNode location = LevelUtils.vector2node(this.location);

        root.add(typeNode);
        root.add(idNode);
        root.add(colorNode);
        root.add(location);

        root.setSourceItem(this);

        return root;
    }

    public long getID(){
        return this.ID;
    }

    @RSLESetter("ID")
    public void setID( long id ){
        this.ID = id;
    }

    @RSLESetter("Color")
    public void setColor( String color ){
        this.color = JSONHelper.readColor(color);
    }

    public Vector3f getLocation(){
        return this.location;
    }
}