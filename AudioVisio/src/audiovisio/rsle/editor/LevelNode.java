package audiovisio.rsle.editor;

import audiovisio.level.ILevelItem;
import audiovisio.level.Level;
import audiovisio.utils.Pair;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A custom extension of Java's DefaultMutableTreeNode.
 *
 * LevelNode mainly adds some convenience functionality to the
 * default TreeNode. It can also contain a reference to the
 * {@link audiovisio.level.ILevelItem} that it represents which
 * is intended to be used for edit/delete operations.
 *
 * A LevelNode can be marked as a container, which causes the
 * editor to make that node (but not its children) read-only.
 *
 * @author Matt Gerst
 */
public class LevelNode extends DefaultMutableTreeNode {
    private boolean    container;
    private ILevelItem sourceItem;
    private Level      sourceLevel;

    public LevelNode( boolean container ){
        this.container = container;
    }

    /**
     * Create a new instance of LevelNode
     *
     * @param userObject Custom user data for the node
     * @param container  Whether this node is a container for other nodes.
     */
    public LevelNode( Object userObject, boolean container ){
        this(container);
        this.setUserObject(userObject);
    }

    /**
     * Create a new instance of LevelNode with a Pair as
     * the userObject, represented as a key-value pair. This is
     * intended to be used for attributes of a level item.
     *
     * @param key       The name of the property
     * @param value     The value of the property
     * @param container Whether this node is a container for other nodes.
     */
    public LevelNode( String key, Object value, boolean container ){
        this(container);
        this.setUserObject(new Pair<String, Object>(key, value));
    }

    /**
     * Is this node read-only? TODO: This should probably be called read-only instead of container
     *
     * @return true if read-only, false otherwise
     */
    public boolean isContainer(){
        return this.container;
    }

    /**
     * Is the data for this node a Pair?
     *
     * @return true if a pair, false otherwise
     */
    public boolean isPair(){
        return this.userObject instanceof Pair;
    }

    /**
     * Set the source item for the node. This is usually the
     * ILevelItem and is intended to be used by editors.
     *
     * @param item The source item
     */
    public void setSourceItem( ILevelItem item ){
        this.sourceItem = item;
    }

    public Level getSourceLevel(){
        return this.sourceLevel;
    }

    public void setSourceLevel( Level level ){
        this.sourceLevel = level;
    }

    /**
     * Get the value of the node. This is either the value
     * of the pair if userObject is a Pair, or the userObject
     * itself.
     *
     * @return The value of the node
     */
    public Object getValue(){
        if (this.isPair()){
            return ((Pair) this.userObject).getValue();
        }
        return this.userObject;
    }

    public void setValue( Object obj ){
        if (this.isPair()){
            ((Pair) this.userObject).setValue(obj);

            if (((LevelNode) this.parent).sourceLevel != null){
                Level lvl = ((LevelNode) this.parent).getSourceLevel();

                if (this.getKey().equals("Name")){
                    lvl.setName((String) obj);
                } else if (this.getKey().equals("Author")){
                    lvl.setAuthor((String) obj);
                } else if (this.getKey().equals("Version")){
                    lvl.setVersion((String) obj);
                }
            }
        } else {
            throw new IllegalStateException("Should not be setting a value that isn't a pair");
        }
    }

    public Object getKey(){
        if (this.isPair()){
            return ((Pair) this.userObject).getKey();
        }
        return null;
    }

    @Override
    public String toString(){
        if (this.userObject instanceof Pair){
            Pair<String, Object> obj = (Pair) this.userObject;
            return obj.getValue().toString();
        }

        if (this.userObject != null){
            return this.userObject.toString();
        }

        return super.toString();
    }
}
