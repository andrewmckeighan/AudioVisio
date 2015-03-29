package audiovisio.level;

import audiovisio.rsle.editor.LevelNode;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

/**
 * Represents a stair in the world
 *
 */
public class Stair extends Panel{
    public static final String KEY_DIRECTION = "direction";

    private Direction direction;
    
    public Stair() {}

    public Stair(Vector3f location, Direction direction){
        super(location);
        this.direction = direction;
    }

    @Override
    public void load( JSONObject loadObj ){
        super.load(loadObj);

        this.direction = Direction.valueOf((String) loadObj.get(Stair.KEY_DIRECTION));
    }
    
    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);

        codeObj.put(JSONHelper.KEY_TYPE, "stair");
        codeObj.put(Stair.KEY_DIRECTION, this.direction.toString());
    }

    @Override
    public LevelNode getLevelNode() {
        LevelNode root = new LevelNode(String.format("#%d stair @ %s", this.ID, this.location), true);
        LevelNode typeNode = new LevelNode("Type", "stair", true);
        LevelNode idNode = new LevelNode("ID", this.ID, false);
        LevelNode dirNode = new LevelNode("Direction", this.direction, false);
        LevelNode location = LevelUtils.vector2node(this.location);

        root.add(typeNode);
        root.add(idNode);
        root.add(dirNode);
        root.add(location);

        return root;
    }

}