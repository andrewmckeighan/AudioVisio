package audiovisio.rsle.editor.dialogs;

import audiovisio.rsle.RSLE;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewStairDialog extends NewDialog {
    JLabel lblLocation = new JLabel("Location");
    JLabel lblDirection = new JLabel("Direction");

    JTextField location = new JTextField("<x,y,z>");
    JComboBox<String> direction = new JComboBox<String>();

    private int id;

    public NewStairDialog(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    @Override
    protected void init() {
        this.setTitle("New Panel");
        this.setLayout(new GridLayout(3, 2));

        this.add(lblLocation);
        this.add(location);

        direction.addItem("NORTH");
        direction.addItem("SOUTH");
        direction.addItem("EAST");
        direction.addItem("WEST");

        this.add(lblDirection);
        this.add(direction);

        super.init();

        this.setSize(250, 90);
    }

    public int getId() {
        return id;
    }

    public String getLevelLocation() {
        return location.getText();
    }

    public String getDirection() {
        return (String) direction.getSelectedItem();
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
