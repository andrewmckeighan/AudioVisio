package audiovisio.rsle.editor.dialogs.triggers;

import audiovisio.rsle.editor.components.LocationField;
import audiovisio.rsle.editor.dialogs.NewDialog;
import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewTextTriggerDialog extends NewDialog {
    JLabel lblLocation = new JLabel("Location");
    JLabel lblText     = new JLabel("Text");

    LocationField location = new LocationField();
    JTextField    text     = new JTextField();

    public NewTextTriggerDialog( Frame owner, boolean modal ){
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init(){
        this.setTitle("New Text Trigger");
        this.setLayout(new GridLayout(3, 2));

        this.add(lblLocation);
        this.add(location);

        this.add(lblText);
        this.add(text);

        super.init();

        this.setSize(250, 90);
    }

    @Override
    protected void okClicked(){
        if (!this.location.isLocationValid() || this.text.getText().isEmpty()){
            this.setStatus(false);
        } else {
            this.setStatus(true);
        }
    }

    public Vector3f getLevelLocation(){
        return this.location.getLocationVector();
    }

    public String getText(){
        return this.text.getText();
    }
}
