package audiovisio.rsle.editor.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Matt Gerst
 */
public class NewLevelDialog extends JDialog implements ActionListener {
    JLabel lblName    = new JLabel("Name");
    JLabel lblAuthor  = new JLabel("Author");
    JLabel lblVersion = new JLabel("Version");

    JTextField name    = new JTextField();
    JTextField author  = new JTextField();
    JTextField version = new JTextField("1.0");

    JButton ok     = new JButton("Ok");
    JButton cancel = new JButton("Cancel");

    boolean status;

    public NewLevelDialog( Frame owner, boolean modal ){
        super(owner, modal);
        this.init();
    }

    private void init(){
        this.setTitle("New Level");
        this.setLayout(new GridLayout(4, 2));

        this.add(this.lblName);
        this.add(this.name);

        this.add(this.lblAuthor);
        this.add(this.author);

        this.add(this.lblVersion);
        this.add(this.version);

        this.ok.addActionListener(this);
        this.cancel.addActionListener(this);
        this.add(this.ok);
        this.add(this.cancel);

        this.setSize(250, 120);
    }

    public String getName(){
        return this.name.getText();
    }

    public String getAuthor(){
        return this.author.getText();
    }

    public String getVersion(){
        return this.version.getText();
    }

    public boolean getStatus(){
        return this.status;
    }

    @Override
    public void actionPerformed( ActionEvent e ){
        this.setVisible(false);

        if (e.getActionCommand().equals(this.ok.getActionCommand())){
            if (this.getName().isEmpty() || this.getAuthor().isEmpty() || this.getVersion().isEmpty()){ return; }

            this.status = true;
        }
    }
}
