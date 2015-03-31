package audiovisio.rsle.editor.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class CreateFloorDialog extends NewDialog {
    JLabel lblStartX = new JLabel("Start X:");
    JLabel lblStartZ = new JLabel("Start Z:");
    JLabel lblSizeX  = new JLabel("Width:");
    JLabel lblSizeZ  = new JLabel("Height:");
    JLabel lblYPlane = new JLabel("Y Level:");

    JTextField startX = new JTextField();
    JTextField startZ = new JTextField();
    JTextField sizeX  = new JTextField();
    JTextField sizeZ  = new JTextField();
    JTextField yPlane = new JTextField(0);

    public CreateFloorDialog( Frame owner, boolean modal ){
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init(){
        this.setTitle("Create Floor");
        this.setLayout(new GridLayout(6, 2));

        this.add(this.lblStartX);
        this.add(this.startX);

        this.add(this.lblStartZ);
        this.add(this.startZ);

        this.add(this.lblSizeX);
        this.add(this.sizeX);

        this.add(this.lblSizeZ);
        this.add(this.sizeZ);

        this.add(this.lblYPlane);
        this.add(this.yPlane);

        super.init();

        this.setSize(250, 180);
    }

    @Override
    protected void okClicked(){
        if (this.startX.getText().isEmpty()
                || this.startZ.getText().isEmpty()
                || this.sizeX.getText().isEmpty()
                || this.sizeZ.getText().isEmpty()
                || this.yPlane.getText().isEmpty()){ this.setStatus(false); } else {
            this.setStatus(true);
        }
    }

    public int getStartX(){
        return Integer.parseInt(this.startX.getText());
    }

    public int getStartZ(){
        return Integer.parseInt(this.startZ.getText());
    }

    public int getSizeX(){
        return Integer.parseInt(this.sizeX.getText());
    }

    public int getSizeZ(){
        return Integer.parseInt(this.sizeZ.getText());
    }

    public int getYPlane(){
        return Integer.parseInt(this.yPlane.getText());
    }
}
