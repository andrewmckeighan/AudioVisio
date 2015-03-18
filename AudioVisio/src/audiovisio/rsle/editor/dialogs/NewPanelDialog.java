package audiovisio.rsle.editor.dialogs;

import audiovisio.rsle.RSLE;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewPanelDialog extends NewDialog {
    JLabel lblLocation = new JLabel("Location");

    JTextField location = new JTextField("<x,y,z>");

    private int id;

    public NewPanelDialog(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    @Override
    protected void init() {
        this.setTitle("New Panel");
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
    public void okClicked() {
        if (getLevelLocation().isEmpty())
            setStatus(false);
        else {
            id = RSLE.getNextID();
            setStatus(true);
        }
    }
}
