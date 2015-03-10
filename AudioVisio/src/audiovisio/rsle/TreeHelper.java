package audiovisio.rsle;

import audiovisio.utils.JSONHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.tree.DefaultMutableTreeNode;

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
            Vector3f loc = JSONHelper.readVector3f((JSONObject) entity.get("location"));

            String type = (String) entity.get("type");

            DefaultMutableTreeNode entityNode = new DefaultMutableTreeNode(type + " @ " + loc);
            entityNode.add(new DefaultMutableTreeNode(type));

            DefaultMutableTreeNode location = new DefaultMutableTreeNode(loc);
            location.add(new DefaultMutableTreeNode(loc.x));
            location.add(new DefaultMutableTreeNode(loc.y));
            location.add(new DefaultMutableTreeNode(loc.z));
            entityNode.add(location);

            if (type.equalsIgnoreCase("stair")) {
                entityNode.add(new DefaultMutableTreeNode(entity.get("direction")));
                stairs.add(entityNode);
            } else if (type.equalsIgnoreCase("trigger")) {
                triggers.add(entityNode);
            } else if (type.equalsIgnoreCase("panel")) {
                panels.add(entityNode);
            } else {
                level.add(entityNode);
            }
        }

        return top;
    }
}
