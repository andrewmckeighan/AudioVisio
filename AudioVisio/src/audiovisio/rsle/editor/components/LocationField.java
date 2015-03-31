package audiovisio.rsle.editor.components;

import com.jme3.math.Vector3f;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class LocationField extends JPanel {

    JTextField xField = new JTextField();
    JTextField yField = new JTextField();
    JTextField zField = new JTextField();

    public LocationField(){
        super(new GridLayout(1, 3));

        this.add(this.xField);
        this.add(this.yField);
        this.add(this.zField);
    }

    public Vector3f getLocationVector(){
        float x = Float.parseFloat(this.xField.getText());
        float y = Float.parseFloat(this.yField.getText());
        float z = Float.parseFloat(this.zField.getText());

        return new Vector3f(x, y, z);
    }

    public boolean isLocationValid(){
        return !(this.xField.getText().isEmpty() || this.yField.getText().isEmpty() || this.zField.getText().isEmpty());
    }
}
