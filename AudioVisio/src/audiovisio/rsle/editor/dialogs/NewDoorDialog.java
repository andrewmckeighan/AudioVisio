package audiovisio.rsle.editor.dialogs;

import audiovisio.rsle.RSLE;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewDoorDialog extends NewDialog {
    JLabel lblLocation = new JLabel("Location");

    JTextField location = new JTextField("<x,y,z>");

    private int id;

    public NewDoorDialog(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    @Override
    protected void init() {
        this.setTitle("New Door");
        this.setLayout(new GridLayout(2, 2));

        this.add(lblLocation);
        this.add(location);

        super.init();

        this.setSize(250, 60);
    }

    public int getId() {
        return id;
    }

    public String getLevelLocation() {
        return location.getText();
    }

    @Override
    protected void okClicked() {
        if (getLevelLocation().isEmpty())
            setStatus(false);
        else {
            id = RSLE.getNextID();
            setStatus(true);
        }
    }

    @Override
    protected void cancelClicked() {}
}
