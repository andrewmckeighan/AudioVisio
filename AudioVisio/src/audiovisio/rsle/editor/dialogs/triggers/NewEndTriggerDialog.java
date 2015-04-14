package audiovisio.rsle.editor.dialogs.triggers;

import audiovisio.rsle.editor.components.LocationField;
import audiovisio.rsle.editor.dialogs.NewDialog;
import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewEndTriggerDialog extends NewDialog {
    JLabel        lblLocation = new JLabel("Location");

    LocationField location    = new LocationField();

    public NewEndTriggerDialog( Frame owner, boolean modal ){
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init(){
        this.setTitle("New End Trigger");
        this.setLayout(new GridLayout(2, 2));

        this.add(lblLocation);
        this.add(location);

        super.init();

        this.setSize(250, 60);
    }

    @Override
    protected void okClicked(){
        if (this.location.isLocationValid()){
            this.setStatus(true);
        } else {
            this.setStatus(false);
        }
    }

    public Vector3f getLevelLocation(){
        return this.location.getLocationVector();
    }
}
