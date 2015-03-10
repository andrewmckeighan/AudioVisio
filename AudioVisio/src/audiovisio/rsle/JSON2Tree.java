package audiovisio.rsle;

import audiovisio.utils.JSONHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.tree.DefaultMutableTreeNode;

public class JSON2Tree {

    public static DefaultMutableTreeNode getTree(JSONObject obj, String name) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(name);

        top.add(new DefaultMutableTreeNode(obj.get("name")));
        top.add(new DefaultMutableTreeNode(obj.get("version")));
        top.add(new DefaultMutableTreeNode(obj.get("author")));

        DefaultMutableTreeNode level = new DefaultMutableTreeNode("Level");
        top.add(level);

        JSONArray levelArr = (JSONArray) obj.get("level");

        for (Object entityObj : levelArr) {
            JSONObject entity = (JSONObject) entityObj;
            Vector3f loc = JSONHelper.readVector3f((JSONObject) entity.get("location"));

            DefaultMutableTreeNode entityNode = new DefaultMutableTreeNode(loc);
            String type = (String) entity.get("type");
            entityNode.add(new DefaultMutableTreeNode(type));

            if (type.equalsIgnoreCase("stair")) {
                entityNode.add(new DefaultMutableTreeNode(entity.get("direction")));
            }

            level.add(entityNode);
        }

        return top;
    }
}
