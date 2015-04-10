package audiovisio.rsle.editor.dialogs;

import audiovisio.rsle.editor.components.LocationField;
import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class SetSpawnDialog extends NewDialog {
    JLabel lblLocation = new JLabel("Location");
    JLabel lblRotation = new JLabel("Rotation");

    LocationField location = new LocationField();
    JTextField    rotation = new JTextField();

    public SetSpawnDialog( Frame owner, boolean modal ){
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init(){
        this.setTitle("Set Spawn");
        this.setLayout(new GridLayout(4, 2));

        this.add(this.lblLocation);
        this.add(this.location);

        this.add(this.lblRotation);
        this.add(this.rotation);

        super.init();

        this.setSize(180, 90);
    }

    @Override
    protected void okClicked(){
        if (this.location.isLocationValid() ||
                this.rotation.getText().isEmpty()){
            this.setStatus(true);
        }
    }

    public Vector3f getLocationVector(){
        return this.location.getLocationVector();
    }

    public float getRotation(){
        return Float.parseFloat(rotation.getText());
    }
}
