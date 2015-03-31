package audiovisio.rsle.editor.components;

import audiovisio.rsle.editor.LevelNode;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Vector;

/**
 * @author Matt Gerst
 */
public class BoolEditor extends JComboBox<Boolean> implements CellEditor {
    boolean value;

    Vector listeners = new Vector();
    LevelNode node;

    public BoolEditor(){
        super(new Boolean[]{ true, false });
        this.setEditable(false);
        this.value = false;

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ){
                if (BoolEditor.this.stopCellEditing()){
                    BoolEditor.this.fireEditingStopped();
                }
            }
        });
    }

    protected void fireEditingStopped(){
        if (this.listeners.size() > 0){
            ChangeEvent ce = new ChangeEvent(this);
            for (int i = this.listeners.size() - 1; i >= 0; i--){
                ((CellEditorListener) this.listeners.elementAt(i)).editingStopped(ce);
            }
        }
    }

    public void setNode( LevelNode node ){
        this.node = node;
    }

    @Override
    public Object getCellEditorValue(){
        return this.value;
    }

    @Override
    public boolean isCellEditable( EventObject eo ){
        return (eo == null)
                || ((eo instanceof MouseEvent) && ((MouseEvent) eo).isMetaDown());
    }

    @Override
    public boolean shouldSelectCell( EventObject eo ){
        return true;
    }

    @Override
    public boolean stopCellEditing(){
        try{
            this.value = (Boolean) this.getSelectedItem();
            this.node.setValue(this.value);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public void cancelCellEditing(){
    }

    @Override
    public void addCellEditorListener( CellEditorListener cel ){
        this.listeners.addElement(cel);
    }

    @Override
    public void removeCellEditorListener( CellEditorListener cel ){
        this.listeners.removeElement(cel);
    }
}
