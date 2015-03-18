package audiovisio.rsle.editor;

import javafx.util.Pair;

import javax.swing.tree.DefaultMutableTreeNode;

public class LevelNode extends DefaultMutableTreeNode {
    private boolean container;

    public LevelNode(boolean container) {
        super();
        this.container = container;
    }

    public LevelNode(Object userObject, boolean container) {
        this(container);
        setUserObject(userObject);
    }

    public LevelNode(String key, Object value, boolean container) {
        this(container);
        setUserObject(new Pair<String, Object>(key, value));
    }

    public boolean isContainer() {
        return this.container;
    }

    public boolean isPair() {
        return userObject instanceof Pair;
    }

    public Object getValue() {
        if (isPair()) {
            return ((Pair) userObject).getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        if (userObject instanceof Pair) {
            Pair<String, Object> obj = (Pair<String, Object>) userObject;
//            return String.format("%s: %s", obj.getKey(), obj.getValue());
            return obj.getValue().toString();
        }

        if (userObject != null) {
            return userObject.toString();
        }

        return super.toString();
    }
}
