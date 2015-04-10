package audiovisio.level;

import audiovisio.entities.Player;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.rsle.editor.RSLESetter;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import audiovisio.utils.LogHelper;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import org.json.simple.JSONObject;

public class Trigger extends Node implements ILevelItem {
    protected Vector3f location;
    protected long ID = -3;

    protected Node rootNode;
    protected GhostControl ghost;

    public Trigger(){}

    public Trigger( Vector3f location ){
        this.location = location;
    }

    @Override
    public void load( JSONObject loadObj ){
        if (loadObj == null){
            throw new IllegalArgumentException("The JSONObject to load cannot be null");
        }
        if (loadObj.isEmpty()){
            throw new IllegalArgumentException("The JSONObject to load cannot be empty");
        }
        this.setID((Long) loadObj.get(JSONHelper.KEY_ID));

        JSONObject location = (JSONObject) loadObj.get(JSONHelper.KEY_LOCATION);
        this.location = JSONHelper.readVector3f(location);
    }

    @Override
    public void init( AssetManager assetManager ){
        this.ghost = new GhostControl(new BoxCollisionShape(Level.SCALE));
        this.addControl(ghost);
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        this.rootNode = rootNode;
        rootNode.attachChild(this);
        physics.add(this);
    }

    @Override
    public void save( JSONObject codeObj ){
        if (codeObj == null){
            throw new IllegalArgumentException("The JSONObject to save to cannot be null");
        }
        if (!codeObj.isEmpty()){
            throw new IllegalArgumentException("The JSONObject to save to cannot be empty");
        }

        JSONObject location = JSONHelper.saveVector3f(this.location);
        codeObj.put(JSONHelper.KEY_TYPE, "trigger");
        codeObj.put(JSONHelper.KEY_ID, this.getID());
        codeObj.put(JSONHelper.KEY_LOCATION, location);
    }

    @Override
    public LevelNode getLevelNode(){
        LevelNode root = new LevelNode(String.format("#%d @ %s", this.ID, this.location), true);
        LevelNode typeNode = new LevelNode("Type", "trigger", true);
        LevelNode idNode = new LevelNode("ID", this.ID, false);
        LevelNode location = LevelUtils.vector2node(this.location);

        root.add(typeNode);
        root.add(idNode);
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

    public Vector3f getLocation(){
        return this.location;
    }

    public void collide( Player player ){
        LogHelper.info("Collided with trigger");
    }

}