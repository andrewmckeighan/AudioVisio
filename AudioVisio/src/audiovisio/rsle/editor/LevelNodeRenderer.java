package audiovisio.rsle.editor;

import audiovisio.utils.Pair;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Custom TreeCellRenderer for LevelNodes.
 *
 * @author Matt Gerst
 */
public class LevelNodeRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent( JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus ){
        Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof LevelNode){
            LevelNode node = (LevelNode) value;
            if (node.isPair()){
                Pair pair = (Pair) node.getUserObject();
                this.setText(String.format("%s: %s", pair.getKey(), pair.getValue()));
                return this;
            }
        }

        return comp;
    }
}
