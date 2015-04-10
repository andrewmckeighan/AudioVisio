package audiovisio.rsle.editor;

import audiovisio.level.ILevelItem;
import audiovisio.level.Level;
import audiovisio.utils.LogHelper;
import audiovisio.utils.Pair;
import com.jme3.math.Vector3f;

import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    private Vector3f   sourceVector;

    /**
     * Create a new instance of LevelNode
     *
     * @param userObject Custom user data for the node
     * @param readonly  Whether this node is readonly.
     */
    public LevelNode( Object userObject, boolean readonly ){
        this(readonly);
        this.setUserObject(userObject);
    }

    public LevelNode( boolean container ){
        this.container = container;
    }

    /**
     * Create a new instance of LevelNode with a Pair as
     * the userObject, represented as a key-value pair. This is
     * intended to be used for attributes of a level item.
     *
     * @param key       The name of the property
     * @param value     The value of the property
     * @param readonly Whether this node is readonly.
     */
    public LevelNode( String key, Object value, boolean readonly ){
        this(readonly);
        this.setUserObject(new Pair<String, Object>(key, value));
    }

    /**
     * Is this node read-only?
     *
     * @return true if read-only, false otherwise
     */
    public boolean isReadOnly(){
        return this.container;
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

    /**
     * Is the data for this node a Pair?
     *
     * @return true if a pair, false otherwise
     */
    public boolean isPair(){
        return this.userObject instanceof Pair;
    }

    public void setValue( Object obj ){
        LogHelper.info("Node " + getKey() + " is a pair? " + isPair());
        if (this.isPair()){
            ((Pair) this.userObject).setValue(obj);
            LevelNode parent = (LevelNode) this.parent;

            if (parent.sourceLevel != null){
                Level lvl = ((LevelNode) this.parent).getSourceLevel();

                if (this.getKey().equals("Name")){
                    lvl.setName((String) obj);
                } else if (this.getKey().equals("Author")){
                    lvl.setAuthor((String) obj);
                } else if (this.getKey().equals("Version")){
                    lvl.setVersion((String) obj);
                }
            } else if (parent.sourceItem != null){
                ILevelItem item = parent.sourceItem;
                Method[] methods = item.getClass().getMethods();

                for(Method method : methods){
                    RSLESetter ann = method.getAnnotation(RSLESetter.class);
                    if (ann != null && getKey().equals(ann.value())){
                        if(!setterInvoke(item, method, obj)){
                            LogHelper.warn("Could not set value!");
                        }
                        break; // We found the correct setter method
                    }
                }
            } else if (parent.sourceVector != null){
                Vector3f vector = parent.getSourceVector();

                float value = Float.parseFloat((String) obj);

                if (this.getKey().equals("X")){
                    vector.setX(value);
                } else if (this.getKey().equals("Y")){
                    vector.setY(value);
                } else if (this.getKey().equals("Z")){
                    vector.setZ(value);
                }
            }
        } else {
            throw new IllegalStateException("Should not be setting a value that isn't a pair");
        }
    }

    public Level getSourceLevel(){
        return this.sourceLevel;
    }

    public void setSourceLevel( Level level ){
        this.sourceLevel = level;
    }

    public void setSourceVector( Vector3f vector ){
        this.sourceVector = vector;
    }

    public Vector3f getSourceVector(){
        return this.sourceVector;
    }

    public Object getKey(){
        if (this.isPair()){
            return ((Pair) this.userObject).getKey();
        }
        return null;
    }

    private boolean setterInvoke(ILevelItem item, Method method, Object obj){
        try{
            Class<?> type = method.getParameterTypes()[0];

            if (type.equals(String.class) && obj instanceof String){
                method.invoke(item, (String) obj);
            }
            else if((type.equals(long.class) || type.equals(Long.class)) && obj instanceof String){
                Long longValue = Long.parseLong((String) obj);
                method.invoke(item, longValue);
            }
            else if(type.equals(boolean.class) || type.equals(Boolean.class)){
                boolean boolValue;
                if (obj instanceof String){
                    boolValue = Boolean.valueOf((String) obj);
                } else {
                    boolValue = (Boolean) obj;
                }

                method.invoke(item, boolValue);
            }
            else{
                LogHelper.warn("Could not find type of given object");
                return false;
            }
        } catch (IllegalAccessException e){
            LogHelper.severe("There was an error setting a value", e);
            return false;
        } catch (InvocationTargetException e){
            LogHelper.severe("There was an error setting a value", e);
            return false;
        } catch (ClassCastException e){
            LogHelper.severe("There was an error setting a value", e);
            return false;
        }

        return true;
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

    public ILevelItem getSourceItem(){
        return sourceItem;
    }
}
