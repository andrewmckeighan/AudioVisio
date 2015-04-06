package audiovisio.rsle.editor.dialogs;

import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class NewPanelDialog extends NewDialog {
    JLabel     lblLocation = new JLabel("Location");
    JPanel     pnlLocation = new JPanel(new GridLayout(1, 3));
    JTextField xField      = new JTextField();
    JTextField yField      = new JTextField();
    JTextField zField      = new JTextField();

    public NewPanelDialog( Frame owner, boolean modal ){
        super(owner, modal);

        this.pnlLocation.add(this.xField);
        this.pnlLocation.add(this.yField);
        this.pnlLocation.add(this.zField);

        this.init();
    }

    @Override
    protected void init(){
        this.setTitle("New Panel");
        this.setLayout(new GridLayout(2, 2));

        this.add(this.lblLocation);
        this.add(this.pnlLocation);

        super.init();

        this.setSize(250, 60);
    }

    @Override
    public void okClicked(){
        if (this.xField.getText().isEmpty()
                || this.yField.getText().isEmpty()
                || this.zField.getText().isEmpty()){ this.setStatus(false); } else {
            this.setStatus(true);
        }
    }

    public Vector3f getLevelLocation(){
        float x = Float.parseFloat(this.xField.getText());
        float y = Float.parseFloat(this.yField.getText());
        float z = Float.parseFloat(this.zField.getText());

        return new Vector3f(x, y, z);
    }
}
