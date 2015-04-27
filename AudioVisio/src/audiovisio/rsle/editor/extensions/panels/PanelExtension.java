package audiovisio.rsle.editor.extensions.panels;

import audiovisio.level.Panel;
import audiovisio.rsle.RSLE;
import audiovisio.rsle.editor.components.LocationField;
import audiovisio.rsle.editor.dialogs.DefaultDialog;
import audiovisio.rsle.editor.extensions.IRSLEExtension;
import audiovisio.utils.LogHelper;
import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Matt Gerst
 */
public class PanelExtension implements IRSLEExtension {
    private RSLE      rsle;
    private JMenuItem addPanel;
    private JMenuItem createFloor;

    private LocationField locationField = new LocationField();

    @Override
    public void init(RSLE rsle) {
        this.rsle = rsle;
    }

    @Override
    public void registerMenuItems() {

        this.addPanel = new JMenuItem("Panel");
        this.addPanel.addActionListener(this);
        this.rsle.registerAddType(RSLE.ItemBucket.PANELS, this.addPanel);

        this.createFloor = new JMenuItem("Create Floor");
        this.createFloor.addActionListener(this);
        this.rsle.registerCreateItem(this.createFloor);
    }

    @Override
    public void onCompletion(String action, String type, String subtype) {
        LogHelper.info("OnCompletion: " + action + ", " + type + ", " + subtype);
        if ("new_panel".equals(action)){
            Vector3f loc = this.locationField.getLocationVector();

            Panel panel = new Panel(loc);
            panel.setColor((String) this.color.getSelectedItem());
            this.rsle.addItem(panel, RSLE.ItemBucket.PANELS);
        } else if ("create_floor".equals(action)){
            int startX = Integer.parseInt(this.startX.getText());
            int startZ = Integer.parseInt(this.startZ.getText());
            int sizeX = Integer.parseInt(this.sizeX.getText());
            int sizeZ = Integer.parseInt(this.sizeZ.getText());
            int yPlane = Integer.parseInt(this.yPlane.getText());

            for (int x = startX; x < startX + sizeX; x++){
                for (int z = startZ; z < startZ + sizeZ; z++){
                    Panel panel = new Panel(new Vector3f(x, yPlane, z));
                    panel.setColor((String) this.color.getSelectedItem());
                    this.rsle.addItem(panel, RSLE.ItemBucket.PANELS);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LogHelper.info("Action Performed");
        if (e.getSource() == this.addPanel){
            DefaultDialog dialog = this.rsle.getDialog("new_panel", "panel", null);
            dialog.setTitle("New Panel");

            dialog.addRow("Color:", this.color);

            this.locationField = new LocationField();
            dialog.addRow("Location", this.locationField);

            dialog.showDialog();
        } else if (e.getSource() == this.createFloor) {
            LogHelper.info("Create Floor Dialog");
            DefaultDialog dialog = this.rsle.getDialog("create_floor", "panel", null);
            dialog.setTitle("Create Floor");

            this.startX = new JTextField();
            dialog.addRow("Start X:", this.startX);
            this.startZ = new JTextField();
            dialog.addRow("Start Z:", this.startZ);

            this.sizeX = new JTextField();
            dialog.addRow("Width:", this.sizeX);
            this.sizeZ = new JTextField();
            dialog.addRow("Height:", this.sizeZ);

            this.yPlane = new JTextField();
            dialog.addRow("Y Level:", this.yPlane);

            dialog.addRow("Color:", this.color);

            dialog.showDialog();
        }
    }

    // Create Floor Dialog Fields
    private JTextField startX;
    private JTextField startZ;
    private JTextField sizeX;
    private JTextField sizeZ;
    private JTextField yPlane;
    private JComboBox<String> color = new JComboBox<String>(new String[]{ "lightGrey", "green", "red", "blue"});
}
