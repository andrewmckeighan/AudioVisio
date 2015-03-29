package audiovisio.level;

import audiovisio.rsle.editor.LevelNode;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import org.json.simple.JSONObject;

public class Trigger implements ILevelItem {
    private Vector3f location;
    private long ID = -3;

    public Trigger(){}

    public Trigger( Vector3f location ){
        this.location = location;
    }

    @Override
    public void load( JSONObject loadObj ){
        this.setID((Long) loadObj.get(JSONHelper.KEY_ID));

        JSONObject location = (JSONObject) loadObj.get(JSONHelper.KEY_LOCATION);
        this.location = JSONHelper.readVector3f(location);
    }

    @Override
    public void init(AssetManager assetManager) {

    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        
    }

    @Override
    public void save( JSONObject codeObj ){
        JSONObject location = JSONHelper.saveVector3f(this.location);
        codeObj.put(JSONHelper.KEY_TYPE, "trigger");
        codeObj.put(JSONHelper.KEY_ID, this.getID());
        codeObj.put(JSONHelper.KEY_LOCATION, location);
    }

    @Override
    public LevelNode getLevelNode() {
        LevelNode root = new LevelNode(String.format("#%d @ %s", this.ID, this.location), true);
        LevelNode typeNode = new LevelNode("Trigger", "trigger", true);
        LevelNode idNode = new LevelNode("ID", this.ID, false);
        LevelNode location = LevelUtils.vector2node(this.location);

        root.add(typeNode);
        root.add(idNode);
        root.add(location);

        return root;
    }

    public long getID() {
        return this.ID;
    }

    public void setID( long id ){
        this.ID = id;
    }

}