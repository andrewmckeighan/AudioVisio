package audiovisio.rsle.editor.components;

import audiovisio.rsle.editor.LevelNode;
import audiovisio.utils.LogHelper;

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

    /**
     * This method is currently a NOP because when this event fires, one of the listeners
     * will change userObject. Since we handle updating userObject ourselves, we don't
     * want this to happen.
     */
    protected void fireEditingStopped(){
//        if (this.listeners.size() > 0){
//            ChangeEvent ce = new ChangeEvent(this);
//            for (int i = this.listeners.size() - 1; i >= 0; i--){
//                ((CellEditorListener) this.listeners.elementAt(i)).editingStopped(ce);
//            }
//        }
    }

    @Override
    public boolean stopCellEditing(){
        try{
            this.value = (Boolean) this.getSelectedItem();
            this.node.setValue(this.value);
            return true;
        } catch (Exception e){
            LogHelper.warn("There was an error setting the Boolean value", e);
            return false;
        }
    }

    public void setNode( LevelNode node ){
        LogHelper.info("Node set to " + node.getKey());
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
