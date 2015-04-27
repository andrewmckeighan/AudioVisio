package audiovisio.rsle.editor.extensions.entities;

import audiovisio.entities.Door;
import audiovisio.rsle.RSLE;
import audiovisio.rsle.editor.components.LocationField;
import audiovisio.rsle.editor.dialogs.DefaultDialog;
import audiovisio.rsle.editor.extensions.IRSLEExtension;
import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Matt Gerst
 */
public class DoorExtension implements IRSLEExtension {
    private RSLE rsle;
    private JMenuItem addDoor;

    @Override
    public void init(RSLE rsle) {
        this.rsle = rsle;
    }

    @Override
    public void registerMenuItems() {
        this.addDoor = new JMenuItem("Door");
        this.addDoor.addActionListener(this);
        this.rsle.registerAddType(RSLE.ItemBucket.ENTITIES, this.addDoor);
    }

    @Override
    public void onCompletion(String action, String type, String subtype) {
        if ("add_door".equals(action)){
            Vector3f loc = this.locationField.getLocationVector();
            String name = this.nameField.getText();
            String edge = (String) this.directionField.getSelectedItem();
            boolean state = this.stateField.isSelected();
            String color = (String) this.colorField.getSelectedItem();

            Door door = new Door(state);
            door.location = loc;
            door.setName(name);
            door.setColor(color);
            door.setEdge(edge);
            this.rsle.addItem(door, RSLE.ItemBucket.ENTITIES);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addDoor){
            DefaultDialog dialog = this.rsle.getDialog("add_door", "door", null);
            dialog.setTitle("New Door");

            this.nameField = new JTextField();
            dialog.addRow("Name:", this.nameField);

            this.stateField = new JCheckBox();
            dialog.addRow("State:", this.stateField);

            this.colorField.setSelectedIndex(0);
            dialog.addRow("Color:", this.colorField);

            this.directionField.setSelectedIndex(0);
            dialog.addRow("Edge:", this.directionField);

            this.locationField = new LocationField();
            dialog.addRow("Location:", this.locationField);

            dialog.showDialog();
        }
    }

    private JTextField    nameField;
    private JCheckBox     stateField;
    private LocationField locationField;
    private JComboBox<String> directionField = new JComboBox<String>(new String[]{"NORTH", "SOUTH", "EAST", "WEST"});
    private JComboBox<String> colorField     = new JComboBox<String>(new String[]{"lightGrey", "blue", "green", "red"});
}
