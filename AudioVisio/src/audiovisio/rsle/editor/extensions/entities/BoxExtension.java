package audiovisio.rsle.editor.extensions.entities;

import audiovisio.entities.Box;
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
public class BoxExtension implements IRSLEExtension {
    private RSLE rsle;
    private JMenuItem addBox;

    @Override
    public void init(RSLE rsle) {
        this.rsle = rsle;
    }

    @Override
    public void registerMenuItems() {
        this.addBox = new JMenuItem("Box");
        this.addBox.addActionListener(this);
        this.rsle.registerAddType(RSLE.ItemBucket.ENTITIES, this.addBox);
    }

    @Override
    public void onCompletion(String action, String type, String subtype) {
        if ("add_box".equals(action)){
            Vector3f loc = this.locationField.getLocationVector();
            String name = this.nameField.getText();
            String color = (String) this.colorField.getSelectedItem();

            audiovisio.entities.Box box = new Box();
            box.location = loc;
            box.setName(name);
            box.setColor(color);
            this.rsle.addItem(box, RSLE.ItemBucket.ENTITIES);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addBox){
            DefaultDialog dialog = this.rsle.getDialog("add_box", "box", null);
            dialog.setTitle("New Box");

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
    private JComboBox<String> colorField = new JComboBox<String>(new String[]{"lightGrey", "blue", "red", "blue"});
}
