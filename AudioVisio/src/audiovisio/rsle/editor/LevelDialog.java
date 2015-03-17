package audiovisio.rsle.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Matt Gerst
 */
public class LevelDialog extends JDialog implements ActionListener {
    JLabel lblName = new JLabel("Name");
    JLabel lblAuthor = new JLabel("Author");
    JLabel lblVersion = new JLabel("Version");

    JTextField name = new JTextField();
    JTextField author = new JTextField();
    JTextField version = new JTextField("1.0");

    JButton ok = new JButton("Ok");
    JButton cancel = new JButton("Cancel");

    boolean status = false;

    public LevelDialog(Frame owner, boolean modal) {
        super(owner, modal);
        init();
    }

    private void init() {
        this.setTitle("New Level");
        this.setLayout(new GridLayout(4, 2));

        this.add(lblName);
        this.add(name);

        this.add(lblAuthor);
        this.add(author);

        this.add(lblVersion);
        this.add(version);

        ok.addActionListener(this);
        cancel.addActionListener(this);
        this.add(ok);
        this.add(cancel);

        this.setSize(250, 120);
    }

    public String getName() {
        return name.getText();
    }

    public String getAuthor() {
        return author.getText();
    }

    public String getVersion() {
        return version.getText();
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);

        if (e.getActionCommand().equals(ok.getActionCommand())) {
            if (getName().isEmpty() || getAuthor().isEmpty() || getVersion().isEmpty())
                return;

            status = true;
        }
    }
}
