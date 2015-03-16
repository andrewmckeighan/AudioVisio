package audiovisio.rsle.level;

import audiovisio.rsle.editor.EditorNode;
import audiovisio.utils.JSONHelper;
import com.jme3.math.Vector3f;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class Panel extends LevelItem {
	private DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode("Type: panel");
	private DefaultMutableTreeNode idNode;

    private JTextField idField;
	
    public Panel(int ID, Location location) {
        super(ID, location);
        idNode = new DefaultMutableTreeNode(new EditorNode("ID", ID));
    }

    public Panel(int ID, Vector3f location) {
        super(ID, location);
        idNode = new DefaultMutableTreeNode(new EditorNode("ID", ID));
    }

    public void attachToTree(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode panel = new DefaultMutableTreeNode(this);
        panel.add(typeNode);
        panel.add(idNode);
        location.attachToTree(panel);

        parent.add(panel);
    }

    @Override
    public void getEditor(JPanel panel) {
        JPanel panel2 = new JPanel(new GridLayout(1,2));
        panel2.setBorder(BorderFactory.createTitledBorder("Panel"));

        JLabel idLbl = new JLabel("ID:");
        panel2.add(idLbl);

        idField = new JTextField(Integer.toString(((EditorNode) idNode.getUserObject()).getInt()));
        panel2.add(idField);

        panel.add(panel2);
        location.getEditor(panel);
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
