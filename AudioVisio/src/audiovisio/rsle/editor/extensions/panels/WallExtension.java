package audiovisio.rsle.editor.extensions.panels;

import audiovisio.level.ILevelItem;
import audiovisio.level.Wall;
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
public class WallExtension implements IRSLEExtension {
    private RSLE rsle;
    private JMenuItem addWall;
    private JMenuItem createWall;

    @Override
    public void init(RSLE rsle) {
        this.rsle = rsle;
    }

    @Override
    public void registerMenuItems() {
        this.addWall = new JMenuItem("Wall");
        this.addWall.addActionListener(this);
        this.rsle.registerAddType(RSLE.ItemBucket.PANELS, this.addWall);

        this.createWall = new JMenuItem("Create Wall");
        this.createWall.addActionListener(this);
        this.rsle.registerCreateItem(this.createWall);
    }

    @Override
    public void onCompletion(String action, String type, String subtype) {
        if ("add_wall".equals(action)){
            Vector3f location = this.location.getLocationVector();
            String direction = (String) this.direction.getSelectedItem();
            String color = (String) this.color.getSelectedItem();

            Wall wall = new Wall(location, ILevelItem.Direction.valueOf(direction));
            wall.setColor(color);
            this.rsle.addItem(wall, RSLE.ItemBucket.PANELS);
        } else if ("create_wall".equals(action)){
            int startX = Integer.parseInt(this.startX.getText());
            int startZ = Integer.parseInt(this.startZ.getText());
            int size = Integer.parseInt(this.size.getText());
            int y= Integer.parseInt(this.yPlane.getText());

            String direction = (String) this.wallDir.getSelectedItem();
            String color = (String) this.color.getSelectedItem();

            if ("NORTH-SOUTH".equals(direction) || "SOUTH-NORTH".equals(direction)) {
                ILevelItem.Direction face;
                if ("NORTH-SOUTH".equals(direction)) {
                    face = ILevelItem.Direction.EAST;
                } else {
                    face = ILevelItem.Direction.WEST;
                }

                for (int z = startZ; z < size; z++) {
                    Wall wall = new Wall(new Vector3f(startX, y, z), face);
                    wall.setColor(color);
                    this.rsle.addItem(wall, RSLE.ItemBucket.PANELS);
                }
            } else if ("EAST-WEST".equals(direction) || "WEST-EAST".equals(direction)){
                ILevelItem.Direction face;
                if ("EAST-WEST".equals(direction)){
                    face = ILevelItem.Direction.NORTH;
                } else {
                    face = ILevelItem.Direction.SOUTH;
                }

                for (int x= startX; x < size; x++) {
                    Wall wall = new Wall(new Vector3f(x, y, startZ), face);
                    wall.setColor(color);
                    this.rsle.addItem(wall, RSLE.ItemBucket.PANELS);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addWall){
            DefaultDialog dialog = this.rsle.getDialog("add_wall", "wall", null);
            dialog.setTitle("New Wall");

            this.color.setSelectedIndex(0);
            dialog.addRow("Color:", this.color);
            this.direction.setSelectedIndex(0);
            dialog.addRow("Direction:", this.direction);

            this.location = new LocationField();
            dialog.addRow("Location:", this.location);

            dialog.showDialog();
        } else if (e.getSource() == this.createWall){
            DefaultDialog dialog = this.rsle.getDialog("create_wall", "wall", null);
            dialog.setTitle("Create Wall");

            this.color.setSelectedIndex(0);
            dialog.addRow("Color:", this.color);

            this.startX = new JTextField();
            dialog.addRow("Start X:", this.startX);
            this.startZ = new JTextField();
            dialog.addRow("Start Z:", this.startZ);
            this.size = new JTextField();
            dialog.addRow("Size:", this.size);

            this.wallDir.setSelectedIndex(0);
            dialog.addRow("Direction:", this.wallDir);

            this.yPlane = new JTextField();
            dialog.addRow("Y Level:", this.yPlane);

            dialog.showDialog();
        }
    }

    private LocationField     location  = new LocationField();
    private JComboBox<String> color     = new JComboBox<String>(new String[]{"lightGrey", "red", "blue", "green"});
    private JComboBox<String> direction = new JComboBox<String>(new String[]{"NORTH", "SOUTH", "EAST", "WEST"});

    // Create Wall
    JTextField startX;
    JTextField startZ;
    JTextField size;
    JComboBox<String> wallDir = new JComboBox<String>(new String[]{"NORTH-SOUTH", "SOUTH-NORTH", "EAST-WEST", "WEST-EAST"});
    JTextField yPlane;
}
