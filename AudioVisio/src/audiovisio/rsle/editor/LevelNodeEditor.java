package audiovisio.rsle.editor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.EventObject;

/**
 * Override functionality of the DefaultTreeCellEditor to only allow edits on
 * LevelNodes that are not read-only. It also attempts to allow you to edit the
 * value of a Pair rather than the "Key: Value" representation that is displayed
 * in the tree (This currently does not work).
 *
 * @author Matt Gerst
 */
public class LevelNodeEditor extends DefaultTreeCellEditor {
    public LevelNodeEditor( JTree tree, DefaultTreeCellRenderer renderer ){
        super(tree, renderer);
    }

    @Override
    public Component getTreeCellEditorComponent( JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row ){
        Component comp = super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);

        if (value instanceof LevelNode){
            if (((LevelNode) value).isPair()){
                value = ((LevelNode) value).getValue();

                if (value instanceof Boolean){
                    JComboBox<Boolean> newComponent = new JComboBox<Boolean>();
                    newComponent.addItem(true);
                    newComponent.addItem(false);
                    newComponent.setSelectedIndex((Boolean) value ? 0 : 1); // Select the correct default value

                    this.editingComponent = newComponent; // Set the component to a dropdown instead of a textbox
                }
            }
        }

        return comp;
    }

    @Override
    public boolean isCellEditable( EventObject event ){
        if (!super.isCellEditable(event)){
            return false;
        }
        if (event != null && event.getSource() instanceof JTree && event instanceof MouseEvent){
            MouseEvent mouseEvent = (MouseEvent) event;
            JTree tree = (JTree) event.getSource();
            TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
            if (path != null){
                if (path.getLastPathComponent() instanceof LevelNode){
                    LevelNode node = (LevelNode) path.getLastPathComponent();
                    return !node.isContainer();
                }
                if (path.getLastPathComponent() instanceof DefaultMutableTreeNode){
                    DefaultMutableTreeNode last = (DefaultMutableTreeNode) path.getLastPathComponent();
                    return Collections.list(last.children()).size() == 0; // Only allow leaf nodes to be editable.
                }
                return path.getPathCount() > 1; // root is not editable
            }
        }
        return false;
    }
}
