package audiovisio.rsle.editor.dialogs;

import audiovisio.rsle.editor.components.LocationField;
import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewStairDialog extends NewDialog {
    JLabel lblLocation = new JLabel("Location");
    JLabel lblDirection = new JLabel("Direction");

    LocationField location = new LocationField();
    JComboBox<String> direction = new JComboBox<String>();

    private int id;

    public NewStairDialog( Frame owner, boolean modal ){
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init(){
        this.setTitle("New Panel");
        this.setLayout(new GridLayout(3, 2));

        this.add(this.lblLocation);
        this.add(this.location);

        this.direction.addItem("NORTH");
        this.direction.addItem("SOUTH");
        this.direction.addItem("EAST");
        this.direction.addItem("WEST");

        this.add(this.lblDirection);
        this.add(this.direction);

        super.init();

        this.setSize(250, 90);
    }

    @Override
    public void okClicked(){
        if (!this.location.isLocationValid()){ this.setStatus(false); } else {
            this.setStatus(true);
        }
    }

    public int getId(){
        return this.id;
    }

    public Vector3f getLevelLocation(){
        return this.location.getLocationVector();
    }

    public String getDirection(){
        return (String) this.direction.getSelectedItem();
    }
}
