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
public class ColorEditor extends JComboBox<String> implements CellEditor {
    static final String[] colors = new String[]{"lightGrey", "red", "blue", "green"};
    String value;
    Vector listeners = new Vector();
    LevelNode node;

    public ColorEditor(){
        super(ColorEditor.colors);
        this.setEditable(false);
        this.value = ColorEditor.colors[0];

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ColorEditor.this.stopCellEditing()){
                    ColorEditor.this.fireEditingStopped();
                }
            }
        });
    }

    protected void fireEditingStopped(){
        if (!this.listeners.isEmpty()){
            ChangeEvent ce = new ChangeEvent(this);
            for (int i = this.listeners.size() - 1; i >= 0; i--){
                ((CellEditorListener) this.listeners.elementAt(i)).editingStopped(ce);
            }
        }
    }

    @Override
    public boolean stopCellEditing() {
        try{
            this.value = (String) this.getSelectedItem();
            this.node.setValue(this.value);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public void setNode( LevelNode node ){
        this.node = node;
    }

    @Override
    public Object getCellEditorValue() {
        return this.value;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return (anEvent == null)
                || ((anEvent instanceof MouseEvent) && ((MouseEvent) anEvent).isMetaDown());
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public void cancelCellEditing() {
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        this.listeners.addElement(l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        this.listeners.removeElement(l);
    }
}
