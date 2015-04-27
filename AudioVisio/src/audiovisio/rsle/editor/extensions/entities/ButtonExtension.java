package audiovisio.rsle.editor.extensions.entities;

import audiovisio.entities.Button;
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
public class ButtonExtension implements IRSLEExtension {
    private RSLE rsle;
    private JMenuItem addButton;

    @Override
    public void init(RSLE rsle) {
        this.rsle = rsle;
    }

    @Override
    public void registerMenuItems() {
        this.addButton = new JMenuItem("Button");
        this.addButton.addActionListener(this);
        this.rsle.registerAddType(RSLE.ItemBucket.ENTITIES, this.addButton);
    }

    @Override
    public void onCompletion(String action, String type, String subtype) {
        if ("add_button".equals(action)){
            Vector3f loc = this.locationField.getLocationVector();
            String color = (String) this.colorField.getSelectedItem();
            String name = this.nameField.getText();

            Button button = new Button();
            button.location = loc;
            button.setName(name);
            button.setColor(color);

            this.rsle.addItem(button, RSLE.ItemBucket.ENTITIES);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addButton){
            DefaultDialog dialog = this.rsle.getDialog("add_button", "button", null);
            dialog.setTitle("New Button");

            this.nameField = new JTextField();
            dialog.addRow("Name:", this.nameField);

            this.colorField.setSelectedIndex(0);
            dialog.addRow("Color:", this.colorField);

            this.locationField = new LocationField();
            dialog.addRow("Location:", this.locationField);

            dialog.showDialog();
        }
    }

    private JTextField    nameField;
    private LocationField locationField;
    private JComboBox<String> colorField = new JComboBox<String>(new String[]{"lightGrey", "red", "blue", "green"});
}
