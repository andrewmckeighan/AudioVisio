package audiovisio.rsle.editor.dialogs.triggers;

import audiovisio.rsle.editor.components.LocationField;
import audiovisio.rsle.editor.dialogs.NewDialog;
import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewLevelTriggerDialog extends NewDialog {
    JLabel lblLocation  = new JLabel("Location");
    JLabel lblLevelFile = new JLabel("Level File");

    LocationField location  = new LocationField();
    JTextField    levelFile = new JTextField();

    public NewLevelTriggerDialog( Frame owner, boolean modal ){
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init(){
        this.setTitle("New Level Trigger");
        this.setLayout(new GridLayout(3, 2));

        this.add(lblLocation);
        this.add(location);

        this.add(lblLevelFile);
        this.add(levelFile);

        super.init();

        this.setSize(250, 90);
    }

    @Override
    protected void okClicked(){
        if (this.location.isLocationValid()
                && !this.levelFile.getText().isEmpty()){
            this.setStatus(true);
        } else {
            this.setStatus(false);
        }
    }

    public Vector3f getLevelLocation(){
        return this.location.getLocationVector();
    }

    public String getLevelFile(){
        return this.levelFile.getText();
    }
}
