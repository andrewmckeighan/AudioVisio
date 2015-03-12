package audiovisio.rsle;

import audiovisio.level.LevelReader;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
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
public class RSLE extends JPanel implements TreeSelectionListener, ActionListener {
    private JTree tree;
    protected JMenuBar menuBar;
    private JMenu file;
    private JMenuItem newLevel;
    private JMenuItem openLevel;
    private JMenuItem save;
    private JMenuItem exit;

    public RSLE() {
        super(new GridLayout(1,0));
        setSize(600, 400);

        // Create the nodes.
        DefaultMutableTreeNode top = TreeHelper.getTree(LevelReader.loadJsonFile("../example_level.json"), "example_level.json");

        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        tree.addTreeSelectionListener(this);

        JScrollPane treeView = new JScrollPane(tree);
        add(treeView);

        menuBar = new JMenuBar();
        createMenu(menuBar);
    }

    /**
     * Create the menu system for the editor
     * @param menu The menu bar to attach the menus to
     */
    private void createMenu(JMenuBar menu) {
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        menu.add(file);

        newLevel = new JMenuItem("New");
        newLevel.setMnemonic(KeyEvent.VK_N);
        newLevel.addActionListener(this);
        file.add(newLevel);

        openLevel = new JMenuItem("Open");
        openLevel.setMnemonic(KeyEvent.VK_O);
        openLevel.setEnabled(false);
        openLevel.addActionListener(this);
        file.add(openLevel);

        save = new JMenuItem("Save");
        save.setMnemonic(KeyEvent.VK_S);
        save.setEnabled(false);
        save.addActionListener(this);
        file.add(save);

        file.add(new JSeparator());

        exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(this);
        file.add(exit);
    }

    /**
     * Called when the selected item in the Tree has changed.
     */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) return;

        System.out.println(node);
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

    /**
     * Called when a menu item has been clicked.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            System.exit(1);
        }
    }
}
