package audiovisio.rsle.editor;

import audiovisio.rsle.editor.components.BoolEditor;
import audiovisio.rsle.editor.components.ColorEditor;
import audiovisio.rsle.editor.components.DirectionEditor;
import audiovisio.rsle.editor.components.TextEditor;
import audiovisio.utils.LogHelper;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.EventObject;

/**
 * @author Matt Gerst
 */
public class LevelNodeEditor2 implements TreeCellEditor {
    BoolEditor      boolEditor;
    TextEditor      textEditor;
    DirectionEditor directionEditor;
    ColorEditor     colorEditor;
    CellEditor      currentEditor;

    public LevelNodeEditor2(){
        this.boolEditor = new BoolEditor();
        this.textEditor = new TextEditor();
        this.directionEditor = new DirectionEditor();
        this.colorEditor = new ColorEditor();

        this.currentEditor = this.textEditor;
    }

    @Override
    public Component getTreeCellEditorComponent( JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row ){
        if (value instanceof LevelNode){
            if (((LevelNode) value).isPair()){
                LevelNode node = (LevelNode) value;
                value = node.getValue();

                // Type based overrides
                if (value instanceof Boolean){
                    this.boolEditor.setNode(node);
                    this.boolEditor.setSelectedItem(value);
                    this.currentEditor = this.boolEditor;
                } else {
                    // Key based overrides
                    if ("Direction".equals(node.getKey())) {
                        this.directionEditor.setSelectedItem(value);
                        this.directionEditor.setNode(node);
                        this.currentEditor = this.directionEditor;
                    } else if ("Color".equals(node.getKey())){
                        this.colorEditor.setSelectedItem(value);
                        this.colorEditor.setNode(node);
                        this.currentEditor = this.colorEditor;
                    } else {
                        // Everything else
                        this.textEditor.setText(value.toString());
                        this.textEditor.setNode(node);
                        this.currentEditor = this.textEditor;
                    }
                }
            }
        } else {
            LogHelper.info("Non-LevelNode edited");
            this.textEditor.setText(value.toString());
            this.currentEditor = this.textEditor;
        }

        return (Component) this.currentEditor;
    }

    @Override
    public Object getCellEditorValue(){
        return this.currentEditor.getCellEditorValue();
    }

    @Override
    public boolean isCellEditable( EventObject event ){
        if (event != null && event.getSource() instanceof JTree && event instanceof MouseEvent){
            MouseEvent mouseEvent = (MouseEvent) event;
            JTree tree = (JTree) event.getSource();
            TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
            if (path != null){
                if (path.getLastPathComponent() instanceof LevelNode){
                    LevelNode node = (LevelNode) path.getLastPathComponent();
                    return !node.isReadOnly();
                }
                if (path.getLastPathComponent() instanceof DefaultMutableTreeNode){
                    DefaultMutableTreeNode last = (DefaultMutableTreeNode) path.getLastPathComponent();
                    return Collections.list(last.children()).size() == 0;
                }
                return path.getPathCount() > 1;
            }
        }

        return false;
    }

    @Override
    public boolean shouldSelectCell( EventObject anEvent ){
        return this.currentEditor.shouldSelectCell(anEvent);
    }

    @Override
    public boolean stopCellEditing(){
        return this.currentEditor.stopCellEditing();
    }

    @Override
    public void cancelCellEditing(){
        this.currentEditor.stopCellEditing();
    }

    @Override
    public void addCellEditorListener( CellEditorListener l ){
        this.boolEditor.addCellEditorListener(l);
        this.textEditor.addCellEditorListener(l);
    }

    @Override
    public void removeCellEditorListener( CellEditorListener l ){
        this.boolEditor.removeCellEditorListener(l);
        this.textEditor.removeCellEditorListener(l);
    }
}
