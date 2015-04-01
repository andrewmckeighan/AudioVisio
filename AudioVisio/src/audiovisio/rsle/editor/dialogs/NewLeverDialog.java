package audiovisio.rsle.editor.dialogs;

import audiovisio.rsle.editor.components.LocationField;
import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewLeverDialog extends NewDialog {
    JLabel lblName     = new JLabel("Name");
    JLabel lblState    = new JLabel("Is On");
    JLabel lblLocation = new JLabel("Location");

    JTextField    nameField     = new JTextField();
    JCheckBox     stateField    = new JCheckBox();
    LocationField locationField = new LocationField();

    public NewLeverDialog( Frame owner, boolean modal ){
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init(){
        this.setTitle("New Door");
        this.setLayout(new GridLayout(4, 2));

        this.add(this.lblName);
        this.add(this.nameField);

        this.add(this.lblState);
        this.add(this.stateField);

        this.add(this.lblLocation);
        this.add(this.locationField);

        super.init();

        this.setSize(250, 120);
    }

    @Override
    protected void okClicked(){
        if (!this.locationField.isLocationValid() || this.nameField.getText().isEmpty()){
            this.setStatus(false);
        } else {
            this.setStatus(true);
        }
    }

    public Vector3f getLevelLocation(){
        return this.locationField.getLocationVector();
    }

    public String getName(){
        return this.nameField.getText();
    }

    public boolean getState(){
        return this.stateField.isSelected();
    }
}