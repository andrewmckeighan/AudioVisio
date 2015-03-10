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

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) return;

        System.out.println(node);
    }

    private void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("Level 1");
        top.add(node);

        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Level 1.1");
        node.add(node1);
    }

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
        if (e.getActionCommand().equals("Exit")) {
            System.exit(1);
        }
    }
}
