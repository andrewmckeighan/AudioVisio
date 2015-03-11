package audiovisio.rsle.level;

import audiovisio.utils.JSONHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Matt Gerst
 */
public class Stair extends Panel {
    public String direction;

    public Stair(int ID, Location location, String direction) {
        super(ID, location);
        this.direction = direction;
    }

    public Stair(int ID, Vector3f location, String direction) {
        super(ID, location);
        this.direction = direction;
    }

    @Override
    public void attachToTree(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode stair = new DefaultMutableTreeNode(this);
        stair.add(new DefaultMutableTreeNode("stair"));
        stair.add(new DefaultMutableTreeNode(this.ID));
        stair.add(new DefaultMutableTreeNode(this.direction));
        location.attachToTree(stair);

        parent.add(stair);
    }

    public static Stair fromJSON(JSONObject obj) {
        JSONObject loc = (JSONObject) obj.get("location");
        String dir = (String) obj.get("direction");
        int id = ((Long) obj.get("id")).intValue();
        return new Stair(id, JSONHelper.readVector3f(loc), dir);
    }

    @Override
    public String toString() {
        return String.format("#%d @ %s facing %s", ID, location.location, direction);
    }
}
