package audiovisio.rsle;

import audiovisio.rsle.level.Panel;
import audiovisio.rsle.level.Stair;
import audiovisio.rsle.level.Trigger;
import audiovisio.utils.JSONHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A class to contain helper methods when building the Tree
 *
 * @author Matt Gerst
 */
public class TreeHelper {

    public static DefaultMutableTreeNode getTree(JSONObject obj, String name) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(name);

        top.add(new DefaultMutableTreeNode(obj.get("name")));
        top.add(new DefaultMutableTreeNode(obj.get("version")));
        top.add(new DefaultMutableTreeNode(obj.get("author")));

        DefaultMutableTreeNode level = new DefaultMutableTreeNode("Level");
        top.add(level);

        JSONArray levelArr = (JSONArray) obj.get("level");

        DefaultMutableTreeNode triggers = new DefaultMutableTreeNode("Triggers");
        DefaultMutableTreeNode stairs = new DefaultMutableTreeNode("Stairs");
        DefaultMutableTreeNode panels = new DefaultMutableTreeNode("Panels");
        level.add(triggers);
        level.add(stairs);
        level.add(panels);

        for (Object entityObj : levelArr) {
            JSONObject entity = (JSONObject) entityObj;
            String type = (String) entity.get("type");

            if (type.equalsIgnoreCase("panel")) {
                Panel panel = Panel.fromJSON(entity);
                panel.attachToTree(panels);
            } else if (type.equalsIgnoreCase("stair")) {
                Stair stair = Stair.fromJSON(entity);
                stair.attachToTree(stairs);
            } else if (type.equalsIgnoreCase("trigger")) {
                Trigger trigger = Trigger.fromJSON(entity);
                trigger.attachToTree(triggers);
            }
        }

        return top;
    }

    private static void addLocation(DefaultMutableTreeNode top, Vector3f loc) {
        top.add(new DefaultMutableTreeNode(loc.x));
        top.add(new DefaultMutableTreeNode(loc.y));
        top.add(new DefaultMutableTreeNode(loc.z));
    }
}
