package audiovisio.rsle.editor.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Matt Gerst
 */
public abstract class NewDialog extends JDialog implements ActionListener {
    private JButton ok = new JButton("Ok");
    private JButton cancel = new JButton("Cancel");
    private boolean status = false;

    public NewDialog(Frame owner, boolean modal) {
        super(owner, modal);
    }

    protected void init() {
        ok.addActionListener(this);
        cancel.addActionListener(this);
        this.add(ok);
        this.add(cancel);
    }

    protected void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    protected abstract void okClicked();
    protected void cancelClicked() {}

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);

        if (e.getActionCommand().equals(ok.getActionCommand())) {
            okClicked();
        } else if (e.getActionCommand().equals(cancel.getActionCommand())) {
            cancelClicked();
        }
    }
}
