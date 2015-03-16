package audiovisio.rsle.editor;

public class EditorNode {
    private String key;
    private Object value;

    public EditorNode(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public int getInt() {
        return (Integer) value;
    }

    public float getFloat() {
        return (Float) value;
    }

    public String getString() {
        return (String) value;
    }

    public double getDouble() {
        return (Double) value;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", key, value);
    }
}
