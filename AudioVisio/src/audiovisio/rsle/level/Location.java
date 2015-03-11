package audiovisio.rsle.level;

import com.jme3.math.Vector3f;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A wrapper around a Vector3 that will handle its
 * representation in the tree.
 *
 * @author Matt Gerst
 */
public class Location {
    public Vector3f location;

    public Location(Vector3f loc) {
        this.location = loc;
    }

    public void attachToTree(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode loc = new DefaultMutableTreeNode(this);
        loc.add(new DefaultMutableTreeNode(location.x));
        loc.add(new DefaultMutableTreeNode(location.y));
        loc.add(new DefaultMutableTreeNode(location.z));

        parent.add(loc);
    }

    @Override
    public String toString() {
        return "Location";
    }
}
