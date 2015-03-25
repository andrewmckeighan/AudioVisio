package audiovisio.rsle.editor.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * @author Matt Gerst
 */
public class CreateFloorDialog extends NewDialog {
    JLabel lblStartX = new JLabel("Start X:");
    JLabel lblStartY = new JLabel("Start Y:");
    JLabel lblSizeX = new JLabel("Width:");
    JLabel lblSizeY = new JLabel("Height:");
    JLabel lblZPlane = new JLabel("Z:");

    JTextField startX = new JTextField();
    JTextField startY = new JTextField();
    JTextField sizeX = new JTextField();
    JTextField sizeY = new JTextField();
    JTextField zPlane = new JTextField(0);

    public CreateFloorDialog(Frame owner, boolean modal) {
        super(owner, modal);
        this.init();
    }

    @Override
    protected void init() {
        this.setTitle( "Create Floor" );
        this.setLayout( new GridLayout( 6, 2 ) );

        this.add(this.lblStartX);
        this.add(this.startX);

        this.add(this.lblStartY);
        this.add(this.startY);

        this.add(this.lblSizeX);
        this.add(this.sizeX);

        this.add(this.lblSizeY);
        this.add(this.sizeY);

        this.add(this.lblZPlane);
        this.add(this.zPlane);

        super.init();

        this.setSize( 250, 180 );
    }

    public int getStartX() {
        return Integer.parseInt(this.startX.getText());
    }

    public int getStartY() {
        return Integer.parseInt(this.startY.getText());
    }

    public int getSizeX() {
        return Integer.parseInt(this.sizeX.getText());
    }

    public int getSizeY() {
        return Integer.parseInt(this.sizeY.getText());
    }

    public int getZPlane() {
        return Integer.parseInt(this.zPlane.getText());
    }

    @Override
    protected void okClicked() {
        if (this.startX.getText().isEmpty()
                || this.startY.getText().isEmpty()
                || this.sizeX.getText().isEmpty()
                || this.sizeY.getText().isEmpty()
                || this.zPlane.getText().isEmpty()) {
            this.setStatus( false );
        } else {
            this.setStatus(true);
        }
    }
}
