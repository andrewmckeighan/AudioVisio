package audiovisio.rsle;

import audiovisio.level.LevelReader;
import audiovisio.rsle.editor.IEditable;
import audiovisio.rsle.level.LevelItem;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * A VERY simple level editor used to edit the level json files.
 * This is NOT intended to be used as the actual level editor. It is
 * intended to make development of the level system easier. This is
 * true for everything in this package.
 *
 * The editor works by parsing the JSON file and creating a tree
 * structure from it. Please note that the classes in the level
 * package only care about the information saved in the level file
 * and need to be updated to match the classes in the actual
 * {@link audiovisio.level} package. They should NOT contain anything
 * else. They do not care about how the items are represented in the
 * world. They do not pass go, they do not collect $200.
 *
 * @author Matt Gerst
 */
public class RSLE extends JPanel implements ActionListener {
    private JTree tree;
    protected JMenuBar menuBar;

    private JPanel editor = new JPanel();

    // MENU ITEMS
    private JMenu file;
    private JMenuItem file_new;
    private JMenuItem file_open;
    private JMenuItem file_save;
    private JMenuItem file_save_as;
    private JMenuItem file_close;
    private JMenuItem file_exit;

    // EDIT ITEMS
    private JMenu edit;
    private JMenuItem edit_copy;
    private JMenuItem edit_cut;
    private JMenuItem edit_paste;

    // ADD ITEMS
    private JMenu add;
    private JMenuItem add_trigger;
    private JMenu add_panels;
    private JMenuItem add_panels_panel;
    private JMenuItem add_panels_stair;
    private JMenuItem add_door;

    public RSLE() {
        super(new GridLayout(1,0));
        setSize(600, 400);

        menuBar = new JMenuBar();
        createMenu();

        tree = new JTree();
        tree.setEditable(false);

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("RSLE");
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        tree.setModel(model);
        add(tree);
    }

    private void createMenu() {
        createFileMenu();
        createEditMenu();
        createAddMenu();
    }

    private void createFileMenu() {
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        file_new = new JMenuItem("New");
        file_new.addActionListener(this);
        file.add(file_new);

        file_open = new JMenuItem("Open");
        file.add(file_open);

        file_save = new JMenuItem("Save");
        file_save.setEnabled(false);
        file.add(file_save);

        file_save_as = new JMenuItem("Save As");
        file_save_as.setEnabled(false);
        file.add(file_save_as);

        file.addSeparator();

        file_close = new JMenuItem("Close");
        file_close.setEnabled(false);
        file_close.addActionListener(this);
        file.add(file_close);

        file_exit = new JMenuItem("Exit");
        file_exit.addActionListener(this);
        file.add(file_exit);

        menuBar.add(file);
    }

    private void createEditMenu() {
        edit = new JMenu("Edit");

        edit_copy = new JMenuItem("Copy");
        edit_copy.setEnabled(false);
        edit.add(edit_copy);

        edit_cut = new JMenuItem("Cut");
        edit_cut.setEnabled(false);
        edit.add(edit_cut);

        edit_paste = new JMenuItem("Paste");
        edit_paste.setEnabled(false);
        edit.add(edit_paste);

        menuBar.add(edit);
    }

    private void createAddMenu() {
        add = new JMenu("Add");
        add.setEnabled(false);

        add_trigger = new JMenuItem("Add Trigger");
        add.add(add_trigger);

        add_panels = new JMenu("Add Panel...");

        add_panels_panel = new JMenuItem("Panel");
        add_panels.add(add_panels_panel);

        add_panels_stair = new JMenuItem("Stair");
        add_panels.add(add_panels_stair);
        add.add(add_panels);

        add_door = new JMenuItem("Add Door");
        add.add(add_door);

        menuBar.add(add);
    }

    /**
     * Create and show the JFrame that contains the editor
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Really Simple Level Editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RSLE rsle = new RSLE();
        frame.add(rsle);
        frame.pack();
        frame.setJMenuBar(rsle.menuBar);
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(file_exit.getActionCommand())) {
            System.exit(0);
        }
        else if (e.getActionCommand().equals(file_new.getActionCommand())) {
            tree.setEditable(true);
            file_save.setEnabled(true);
            file_save_as.setEnabled(true);
            file_close.setEnabled(true);

            add.setEnabled(true);
        }
        else if (e.getActionCommand().equals(file_close.getActionCommand())) {
            tree.setEditable(false);
            file_save.setEnabled(false);
            file_save_as.setEnabled(false);
            file_close.setEnabled(false);

            add.setEnabled(false);
        }
    }
}
