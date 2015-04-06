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
public class TextEditor extends JTextField implements CellEditor {
    String value     = "";
    Vector listeners = new Vector();
    LevelNode node;

    public TextEditor(){
        this("", 5);
    }

    public TextEditor( String s, int w ){
        super(s, w);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ){
                if (TextEditor.this.stopCellEditing()){
                    TextEditor.this.fireEditingStopped();
                }
            }
        });
    }

    public TextEditor( String s ){
        this(s, 5);
    }

    public TextEditor( int w ){
        this("", w);
    }

    protected void fireEditingStopped(){
        if (this.listeners.size() > 0){
            ChangeEvent ce = new ChangeEvent(this);
            for (int i = this.listeners.size() - 1; i >= 0; i--){
                ((CellEditorListener) this.listeners.elementAt(i)).editingStopped(ce);
            }
        }
    }

    @Override
    public boolean stopCellEditing(){
        try{
            String tmp = this.getText();
            if (tmp == null){
                return false;
            }
            this.value = tmp;
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
    public Object getCellEditorValue(){
        return this.value;
    }

    @Override
    public boolean isCellEditable( EventObject eo ){
        return (eo == null)
                || ((eo instanceof MouseEvent) && (((MouseEvent) eo).isMetaDown()));
    }

    @Override
    public boolean shouldSelectCell( EventObject eo ){
        return true;
    }

    @Override
    public void cancelCellEditing(){
        this.setText("");
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
