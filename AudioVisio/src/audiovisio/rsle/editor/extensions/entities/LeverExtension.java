package audiovisio.rsle.editor.extensions.entities;

import audiovisio.entities.Lever;
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
public class LeverExtension implements IRSLEExtension {
    private RSLE      rsle;
    private JMenuItem addLever;

    @Override
    public void init(RSLE rsle) {
        this.rsle = rsle;
    }

    @Override
    public void registerMenuItems() {
        this.addLever = new JMenuItem("Lever");
        this.addLever.addActionListener(this);
        this.rsle.registerAddType(RSLE.ItemBucket.ENTITIES, this.addLever);
    }

    @Override
    public void onCompletion(String action, String type, String subtype) {
        if ("add_lever".equals(action)){
            Vector3f loc = this.locationField.getLocationVector();
            String name = this.nameField.getText();
            boolean state = this.stateField.isSelected();
            String color = (String) this.colorField.getSelectedItem();
            String edge = (String) this.edgeField.getSelectedItem();

            Lever lever = new Lever();
            lever.location = loc;
            lever.setName(name);
            lever.setState(state);
            lever.setDirection(edge);
            lever.setColor(color);

            this.rsle.addItem(lever, RSLE.ItemBucket.ENTITIES);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addLever) {
            DefaultDialog dialog = this.rsle.getDialog("add_lever", "lever", null);
            dialog.setTitle("New Lever");

            this.nameField = new JTextField();
            dialog.addRow("Name:", this.nameField);

            this.stateField = new JCheckBox();
            dialog.addRow("Is On:", this.stateField);

            this.edgeField.setSelectedIndex(0);
            dialog.addRow("Edge:", this.edgeField);

            this.colorField.setSelectedIndex(0);
            dialog.addRow("Color:", this.colorField);

            this.locationField = new LocationField();
            dialog.addRow("Location:", this.locationField);

            dialog.showDialog();
        }
    }

    private JTextField nameField;
    private JCheckBox  stateField;
    private JComboBox<String> edgeField  = new JComboBox<String>(new String[]{"NORTH", "SOUTH", "EAST", "WEST"});
    private JComboBox<String> colorField = new JComboBox<String>(new String[]{"lightGrey", "blue", "green", "red"});
    private LocationField locationField;
}
