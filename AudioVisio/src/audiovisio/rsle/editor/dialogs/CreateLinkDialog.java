package audiovisio.rsle.editor.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * @author Matt Gerst
 */
public class CreateLinkDialog extends NewDialog {
    JLabel lblFrom = new JLabel("From:");
    JLabel lblTo   = new JLabel("To:");

    JComboBox<Long> from = new JComboBox<Long>();
    JComboBox<Long> to   = new JComboBox<Long>();

    public CreateLinkDialog( Frame owner, boolean modal ){
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init(){
        this.setTitle("Create Link");
        this.setLayout(new GridLayout(3, 2));

        this.add(this.lblFrom);
        this.add(this.from);

        this.add(this.lblTo);
        this.add(this.to);

        super.init();

        this.setSize(250, 90);
    }

    @Override
    protected void okClicked(){
        this.setStatus(true);
    }

    public void setValidIds( Set<Long> ids ){
        for (Long id : ids){
            this.from.addItem(id);
            this.to.addItem(id);
        }
    }

    public Long getFrom(){
        return (Long) this.from.getSelectedItem();
    }

    public Long getTo(){
        return (Long) this.from.getSelectedItem();
    }
}
