package audiovisio.rsle.level;

import audiovisio.rsle.editor.EditorNode;
import audiovisio.rsle.editor.IEditable;
import com.jme3.math.Vector3f;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * A wrapper around a Vector3 that will handle its
 * representation in the tree.
 *
 * @author Matt Gerst
 */
public class Location implements IEditable {
	/**
	 * The location vector to be represented.
	 */
    public Vector3f location;
    
    private DefaultMutableTreeNode x;
    private DefaultMutableTreeNode y;
    private DefaultMutableTreeNode z;

    private JTextField xField;
    private JTextField yField;
    private JTextField zField;
    
    /**
     * Create a new Location instances. Handles
     * creation of the x,y,z nodes.
     * 
     * @param loc The location to be represented
     */
    public Location(Vector3f loc) {
        this.location = loc;
        
        x = new DefaultMutableTreeNode(new EditorNode("X", loc.x));
        y = new DefaultMutableTreeNode(new EditorNode("Y", loc.y));
        z = new DefaultMutableTreeNode(new EditorNode("Z", loc.z));
    }

    /**
     * Attach the location to a given node in the tree.
     * Handles attaching the x,y,z nodes to the location node.
     * 
     * @param parent The parent node to attach to.
     */
    public void attachToTree(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode loc = new DefaultMutableTreeNode(this);
        loc.add(x);
        loc.add(y);
        loc.add(z);

        parent.add(loc);
    }

    public void getEditor(JPanel panel) {
        JPanel group = new JPanel(new GridLayout(3,2));
        group.setBorder(BorderFactory.createTitledBorder("Location"));

        JLabel xLbl = new JLabel("X:");
        JLabel yLbl = new JLabel("Y:");
        JLabel zLbl = new JLabel("Z:");

        xField = new JTextField(Float.toString(((EditorNode) x.getUserObject()).getFloat()), 5);
        yField = new JTextField(Float.toString(((EditorNode) y.getUserObject()).getFloat()), 5);
        zField = new JTextField(Float.toString(((EditorNode) z.getUserObject()).getFloat()), 5);

        group.add(xLbl);
        group.add(xField);

        group.add(yLbl);
        group.add(yField);

        group.add(zLbl);
        group.add(zField);

        panel.add(group);
    }

    @Override
    public String toString() {
        return "Location";
    }
}
