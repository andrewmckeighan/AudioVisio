package audiovisio.rsle.level;

import com.jme3.math.Vector3f;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The Base class for all items in the level file. All level items
 * will have at minimum a location and an id. Each item is
 * responsible for adding itself and its children to the tree.
 *
 * @author Matt Gerst
 */
public abstract class LevelItem {
    public int ID;
    public Location location;

    public LevelItem(int ID, Location location) {
        this.ID = ID;
        this.location = location;
    }

    public LevelItem(int ID, Vector3f location) {
        this.ID = ID;
        this.location = new Location(location);
    }

    /**
     * Add the item to the tree.
     * @param parent The parent to add to
     */
    public abstract void attachToTree(DefaultMutableTreeNode parent);
}
