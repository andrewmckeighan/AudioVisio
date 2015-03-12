package audiovisio.rsle.level;

import audiovisio.rsle.editor.EditorNode;
import audiovisio.utils.JSONHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Matt Gerst
 */
public class Trigger extends LevelItem {
    public Trigger(int ID, Location location) {
        super(ID, location);
    }

    public Trigger(int ID, Vector3f location) {
        super(ID, location);
    }

    @Override
    public void attachToTree(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode trigger = new DefaultMutableTreeNode(this);
        trigger.add(new DefaultMutableTreeNode(new EditorNode("ID", ID)));
        this.location.attachToTree(trigger);

        parent.add(trigger);
    }

    public static Trigger fromJSON(JSONObject obj) {
        JSONObject loc = (JSONObject) obj.get("location");
        int id = ((Long) obj.get("id")).intValue();
        return new Trigger(id, JSONHelper.readVector3f(loc));
    }

    @Override
    public String toString() {
        return String.format("#%d @ %s", ID, location.location);
    }
}
