package audiovisio.rsle.editor.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Matt Gerst
 */
public abstract class NewDialog extends JDialog implements ActionListener {
    private JButton ok     = new JButton("Ok");
    private JButton cancel = new JButton("Cancel");
    private boolean status;

    public NewDialog( Frame owner, boolean modal ){
        super(owner, modal);
    }

    protected void init(){
        this.ok.addActionListener(this);
        this.cancel.addActionListener(this);
        this.add(this.ok);
        this.add(this.cancel);
    }

    public boolean getStatus(){
        return this.status;
    }

    protected void setStatus( boolean status ){
        this.status = status;
    }

    protected abstract void okClicked();

    protected void cancelClicked(){}

    @Override
    public void actionPerformed( ActionEvent e ){
        this.setVisible(false);

        if (e.getActionCommand().equals(this.ok.getActionCommand())){
            this.okClicked();
        } else if (e.getActionCommand().equals(this.cancel.getActionCommand())){
            this.cancelClicked();
        }
    }
}
