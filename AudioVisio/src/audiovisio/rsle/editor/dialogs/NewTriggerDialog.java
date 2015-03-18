package audiovisio.rsle.editor.dialogs;

import audiovisio.rsle.RSLE;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewTriggerDialog extends NewDialog {
    JLabel lblID = new JLabel("ID");
    JLabel lblLocation = new JLabel("Location");

    JTextField id = new JTextField(RSLE.getNextID());
    JTextField location = new JTextField("<x,y,z>");

    public NewTriggerDialog(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    @Override
    protected void init() {
        this.setTitle("New Trigger");
        this.setLayout(new GridLayout(3, 2));

        this.add(lblID);
        this.add(id);

        this.add(lblLocation);
        this.add(location);

        super.init();

        this.setSize(250, 120);
    }

    public int getId() {
        return Integer.parseInt(id.getText());
    }

    public String getLevelLocation() {
        return location.getText();
    }

    @Override
    protected void okClicked() {
        if (getLevelLocation().isEmpty())
            setStatus(false);
        else
            setStatus(true);
    }

    @Override
    protected void cancelClicked() {}
}
