package audiovisio.rsle.editor.dialogs;

import audiovisio.rsle.RSLE;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewTriggerDialog extends NewDialog {
    JLabel lblLocation = new JLabel("Location");

    JTextField location = new JTextField("<x,y,z>");

    private int id;

    public NewTriggerDialog(Frame owner, boolean modal) {
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init() {
        this.setTitle("New Trigger");
        this.setLayout(new GridLayout(2, 2));

        this.add(this.lblLocation);
        this.add(this.location);

        super.init();

        this.setSize(250, 60);
    }

    public int getId() {
        return this.id;
    }

    public String getLevelLocation() {
        return this.location.getText();
    }

    @Override
    protected void okClicked() {
        if (this.getLevelLocation().isEmpty())
            this.setStatus(false);
        else {
            this.id = RSLE.getNextID();
            this.setStatus(true);
        }
    }

    @Override
    protected void cancelClicked() {}
}
