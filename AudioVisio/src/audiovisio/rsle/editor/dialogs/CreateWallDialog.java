package audiovisio.rsle.editor.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class CreateWallDialog extends NewDialog {
    JLabel            lblStartX    = new JLabel("Start X:");
    JLabel            lblStartZ    = new JLabel("Start Z:");
    JLabel            lblSize      = new JLabel("Size:");
    JLabel            lblDirection = new JLabel("Direction:");
    JLabel            lblYPlane    = new JLabel("Y Level:");
    JTextField        startX       = new JTextField();
    JTextField        startZ       = new JTextField();
    JTextField        size         = new JTextField();
    JComboBox<String> direction    = new JComboBox<String>();
    JTextField        yPlane       = new JTextField(0);

    public CreateWallDialog( Frame owner, boolean modal ){
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init(){
        this.setTitle("Create Wall");
        this.setLayout(new GridLayout(5, 2));

        this.add(this.lblStartX);
        this.add(this.startX);

        this.add(this.lblStartZ);
        this.add(this.startZ);

        this.add(this.lblSize);
        this.add(this.size);

        this.direction.addItem("NORTH-SOUTH");
        this.direction.addItem("SOUTH-NORTH");
        this.direction.addItem("EAST-WEST");
        this.direction.addItem("WEST-EAST");

        this.add(this.lblDirection);
        this.add(this.direction);

        this.add(this.lblYPlane);
        this.add(this.yPlane);

        super.init();

        this.setSize(250, 150);
    }

    @Override
    protected void okClicked(){
        if (this.startX.getText().isEmpty()
                || this.size.getText().isEmpty()
                || this.yPlane.getText().isEmpty()){
            this.setStatus(false);
        }

        this.setStatus(true);
    }

    public int getStartX(){
        return Integer.parseInt(this.startX.getText());
    }

    public int getStartZ(){
        return Integer.parseInt(this.startZ.getText());
    }

    public int getWallSize(){
        return Integer.parseInt(this.size.getText());
    }

    public String getDirection(){
        return (String) this.direction.getSelectedItem();
    }

    public int getYPlane(){
        return Integer.parseInt(this.yPlane.getText());
    }
}
