package audiovisio.rsle.editor.extensions.panels;

import audiovisio.level.ILevelItem;
import audiovisio.level.Stair;
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
public class StairExtension implements IRSLEExtension {
    private RSLE      rsle;
    private JMenuItem addStair;

    private LocationField     location  = new LocationField();
    private JComboBox<String> direction = new JComboBox<String>(new String[]{"NORTH", "SOUTH", "EAST", "WEST"});
    private JComboBox<String> color     = new JComboBox<String>(new String[]{"lightGrey", "red", "green", "blue"});

    @Override
    public void init(RSLE rsle) {
        this.rsle = rsle;
    }

    @Override
    public void registerMenuItems() {
        this.addStair = new JMenuItem("Stair");
        this.addStair.addActionListener(this);
        this.rsle.registerAddType(RSLE.ItemBucket.PANELS, this.addStair);
    }

    @Override
    public void onCompletion(String action, String type, String subtype) {
        if ("add_stair".equals(action)){
            Vector3f loc = this.location.getLocationVector();
            String color = (String) this.color.getSelectedItem();
            String direction = (String) this.direction.getSelectedItem();

            Stair stair = new Stair(loc, ILevelItem.Direction.valueOf(direction));
            stair.setColor(color);
            this.rsle.addItem(stair, RSLE.ItemBucket.PANELS);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addStair){
            DefaultDialog dialog = this.rsle.getDialog("add_stair", "stair", null);
            dialog.setTitle("Add Stair");

            dialog.addRow("Direction:", this.direction);
            dialog.addRow("Color:", this.color);

            this.location = new LocationField();
            dialog.addRow("Location:", this.location);

            dialog.showDialog();
        }
    }
}
