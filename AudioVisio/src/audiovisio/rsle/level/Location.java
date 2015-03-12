package audiovisio.rsle.level;

import audiovisio.rsle.editor.EditorNode;
import com.jme3.math.Vector3f;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A wrapper around a Vector3 that will handle its
 * representation in the tree.
 *
 * @author Matt Gerst
 */
public class Location {
	/**
	 * The location vector to be represented.
	 */
    public Vector3f location;
    
    private DefaultMutableTreeNode x;
    private DefaultMutableTreeNode y;
    private DefaultMutableTreeNode z;
    
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

    @Override
    public String toString() {
        return "Location";
    }
}
