package audiovisio.rsle.level;

import audiovisio.utils.JSONHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Matt Gerst
 */
public class Panel extends LevelItem {
    public Panel(int ID, Location location) {
        super(ID, location);
    }

    public Panel(int ID, Vector3f location) {
        super(ID, location);
    }

    public void attachToTree(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode panel = new DefaultMutableTreeNode(this);
        panel.add(new DefaultMutableTreeNode("panel"));
        panel.add(new DefaultMutableTreeNode(this.ID));
        location.attachToTree(panel);

        parent.add(panel);
    }

    public static Panel fromJSON(JSONObject obj) {
        int id = ((Long) obj.get("id")).intValue();
        return new Panel(id, JSONHelper.readVector3f((JSONObject) obj.get("location")));
    }

    @Override
    public String toString() {
        return String.format("#%d @ %s", this.ID, this.location.location);
    }
}
