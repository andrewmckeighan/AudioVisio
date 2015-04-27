package audiovisio.rsle.editor.dialogs;

import audiovisio.rsle.editor.extensions.IRSLEExtension;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Matt Gerst
 */
public class DefaultDialog extends JDialog implements ActionListener{
    private JButton ok     = new JButton("Ok");
    private JButton cancel = new JButton("Cancel");

    private IRSLEExtension extension;
    private String action;
    private String type;
    private String subtype;

    private int rows = 1;

    public DefaultDialog( Frame owner, boolean modal ){
        super(owner, modal);

        this.ok.addActionListener(this);
        this.cancel.addActionListener(this);
        this.setSize(250, 30);
    }

    public void setExtension(IRSLEExtension ext, String action, String type, String subtype){
        this.extension = ext;
        this.action = action;
        this.type = type;
        this.subtype = subtype;
    }

    public void showDialog(){
        this.add(this.ok);
        this.add(this.cancel);
        this.setSize(250, this.rows * 30);
        this.setLayout(new GridLayout(this.rows, 2));

        this.setVisible(true);
    }

    public void addRow(String name, JComponent editor){
        this.add(new JLabel(name));
        this.add(editor);
        this.rows++;
    }

    protected void okClicked(){
        this.extension.onCompletion(this.action, this.type, this.subtype);
    }

    protected void cancelClicked(){

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);

        if (e.getActionCommand().equals(this.ok.getActionCommand())){
            this.okClicked();
        } else if (e.getActionCommand().equals(this.cancel.getActionCommand())){
            this.cancelClicked();
        }
    }
}
