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
        init();
    }

    @Override
    protected void init() {
        this.setTitle("Create Floor");
        this.setLayout(new GridLayout(6, 2));

        this.add(lblStartX);
        this.add(startX);

        this.add(lblStartY);
        this.add(startY);

        this.add(lblSizeX);
        this.add(sizeX);

        this.add(lblSizeY);
        this.add(sizeY);

        this.add(lblZPlane);
        this.add(zPlane);

        super.init();

        this.setSize(250, 180);
    }

    public int getStartX() {
        return Integer.parseInt(startX.getText());
    }

    public int getStartY() {
        return Integer.parseInt(startY.getText());
    }

    public int getSizeX() {
        return Integer.parseInt(sizeX.getText());
    }

    public int getSizeY() {
        return Integer.parseInt(sizeY.getText());
    }

    public int getZPlane() {
        return Integer.parseInt(zPlane.getText());
    }

    @Override
    protected void okClicked() {
        if (startX.getText().isEmpty()
                || startY.getText().isEmpty()
                || sizeX.getText().isEmpty()
                || sizeY.getText().isEmpty()
                || zPlane.getText().isEmpty())
            setStatus(false);
        else {
            setStatus(true);
        }
    }
}
