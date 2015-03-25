package audiovisio.rsle.editor;

import audiovisio.utils.Pair;

import javax.swing.tree.DefaultMutableTreeNode;

public class LevelNode extends DefaultMutableTreeNode {
    private boolean container;

    public LevelNode(boolean container) {
        this.container = container;
    }

    public LevelNode(Object userObject, boolean container) {
        this(container);
        this.setUserObject(userObject);
    }

    public LevelNode(String key, Object value, boolean container) {
        this(container);
        this.setUserObject(new Pair<String, Object>(key, value));
    }

    public boolean isContainer() {
        return this.container;
    }

    public boolean isPair() {
        return this.userObject instanceof Pair;
    }

    public Object getValue() {
        if (this.isPair()) {
            return ((Pair) this.userObject).getValue();
        }
        return this.userObject;
    }

    @Override
    public String toString() {
        if (this.userObject instanceof Pair) {
            Pair<String, Object> obj = (Pair<String, Object>) this.userObject;
//            return String.format("%s: %s", obj.getKey(), obj.getValue());
            return obj.getValue().toString();
        }

        if (this.userObject != null) {
            return this.userObject.toString();
        }

        return super.toString();
    }
}
