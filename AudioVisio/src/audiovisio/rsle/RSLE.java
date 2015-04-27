package audiovisio.rsle;

import audiovisio.Items;
import audiovisio.RSLEExtensions;
import audiovisio.level.*;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.rsle.editor.LevelNodeEditor2;
import audiovisio.rsle.editor.LevelNodeRenderer;
import audiovisio.rsle.editor.dialogs.CreateLinkDialog;
import audiovisio.rsle.editor.dialogs.DefaultDialog;
import audiovisio.rsle.editor.dialogs.NewLevelDialog;
import audiovisio.rsle.editor.dialogs.SetSpawnDialog;
import audiovisio.rsle.editor.extensions.IRSLEExtension;
import audiovisio.utils.FileUtils;
import audiovisio.utils.LogHelper;
import com.jme3.math.Vector3f;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;

/**
 * A VERY simple level editor used to edit the level json files.
 * This is NOT intended to be used as the actual level editor. It is
 * intended to make development of the level system easier. This is
 * true for everything in this package.
 *
 * The editor works by using {@link audiovisio.level.Level} to load
 * the level and creates a Tree structure using the node obtained
 * from {@link audiovisio.level.ILevelItem#getLevelNode}. It adds
 * items to the Level as well as the tree. Each ILevelItem is in
 * charge of managing it's own {@link audiovisio.rsle.editor.LevelNode}s.
 *
 * @author Matt Gerst
 */
public class RSLE extends JPanel implements ActionListener, MouseListener {
    protected JMenuBar         menuBar;
    private   JTree            tree;
    private   DefaultTreeModel treeModel;
    private JPanel editor = new JPanel();

    // MENU ITEMS
    private JMenu     file;
    private JMenuItem file_new;
    private JMenuItem file_open;
    private JMenuItem file_save;
    private JMenuItem file_save_as;
    private JMenuItem file_close;
    private JMenuItem file_exit;

    // EDIT ITEMS
    private JMenu     edit;
    private JMenuItem edit_regen_id;
    private JMenuItem edit_set_p1_spawn;
    private JMenuItem edit_set_p2_spawn;

    // ADD ITEMS
    private JMenu     add;
    private JMenuItem addPanel;
    private JMenuItem addTrigger;
    private JMenuItem addEntity;

    // CREATE ITEMS
    private JMenu     create;
    private JMenuItem create_link;

    // CONTEXT MENU
    private JPopupMenu ctxMenu;
    private JMenuItem  ctxDelete;
    private LevelNode  triggers;
    private LevelNode  panels;
    private LevelNode  entities;

    // level STUFF
    private File  loadedFile;
    private Level currentLevel;

    private HashMap<String, IRSLEExtension> extensions = new HashMap<String, IRSLEExtension>();

    public RSLE() {
        super(new GridLayout(1, 0));
        this.setSize(600, 600);

        Items.init(); // Register all ILevelItems with the LevelRegistry

        this.menuBar = new JMenuBar();
        this.createMenu();

        this.tree = new JTree();
        this.tree.setEditable(false);

        DefaultTreeCellRenderer renderer = new LevelNodeRenderer();
        this.tree.setCellRenderer(renderer);
        this.tree.setCellEditor(new DefaultTreeCellEditor(this.tree, renderer, new LevelNodeEditor2()));
        this.tree.setShowsRootHandles(true);
        this.tree.addMouseListener(this);

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("RSLE");
        this.treeModel = new DefaultTreeModel(rootNode);
        this.tree.setModel(this.treeModel);
        this.add(new JScrollPane(this.tree));

        this.ctxMenu = new JPopupMenu();
        this.ctxDelete = new JMenuItem("Delete");
        this.ctxDelete.setEnabled(false);
        this.ctxDelete.setMnemonic(KeyEvent.VK_D);
        this.ctxDelete.addActionListener(this);
        this.ctxMenu.add(this.ctxDelete);

        RSLEExtensions.init();
        this.loadExtensions();
    }

    public void loadExtensions() {
        for (String item : LevelRegistry.getRegisteredTypes()) {
            IRSLEExtension ext = LevelRegistry.getExtensionForType(item);
            if (ext == null) continue;

            this.extensions.put(item, ext);

            ext.init(this);
            ext.registerMenuItems();
        }
    }

    public void registerAddType( ItemBucket bucket, JMenuItem item ){
        switch (bucket){
            case TRIGGERS:
                this.addTrigger.add(item);
                break;
            case PANELS:
                this.addPanel.add(item);
                break;
            case ENTITIES:
                this.addEntity.add(item);
                break;
        }
    }

    public void registerCreateItem( JMenuItem menuItem ){
        this.create.add(menuItem);
    }

    public DefaultDialog getDialog( String action, String type, String subtype){
        DefaultDialog dialog = new DefaultDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
        dialog.setExtension(this.extensions.get(type), action, type, subtype);
        return dialog;
    }

    /**
     * Load the file from disk
     *
     * @param file The file to load
     */
    public void load(File file) {
        try {
            this.currentLevel = LevelLoader.read(file);
            this.currentLevel.loadLevel();
            this.buildTree();

            this.tree.setEditable(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "There was an error reading the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LogHelper.severe("Error reading level file!", ex);
        }
    }

    public static void main(String[] args) {
        LogHelper.init();
        LogHelper.toggleStackDump(); // I don't want to dump stack for warn or severe messages
        LogHelper.setLevel(java.util.logging.Level.INFO);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RSLE.createAndShowGUI();
            }
        });
    }

    /**
     * Create and show the JFrame that contains the editor
     */
    private static void createAndShowGUI(){
        JFrame frame = new JFrame("Really Simple Level Editor (v0.1)");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RSLE rsle = new RSLE();
        frame.add(rsle);
        frame.pack();
        frame.setJMenuBar(rsle.menuBar);
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    private void createMenu(){
        this.createFileMenu();
        this.createEditMenu();
        this.createAddMenu();
        this.createCreateMenu();
    }

    private void createFileMenu(){
        this.file = new JMenu("File");
        this.file.setMnemonic(KeyEvent.VK_F);

        this.file_new = new JMenuItem("New");
        this.file_new.addActionListener(this);
        this.file.add(this.file_new);

        this.file_open = new JMenuItem("Open");
        this.file_open.addActionListener(this);
        this.file.add(this.file_open);

        this.file_save = new JMenuItem("Save");
        this.file_save.addActionListener(this);
        this.file_save.setEnabled(false);
        this.file.add(this.file_save);

        this.file_save_as = new JMenuItem("Save As");
        this.file_save_as.addActionListener(this);
        this.file_save_as.setEnabled(false);
        this.file.add(this.file_save_as);

        this.file.addSeparator();

        this.file_close = new JMenuItem("Close");
        this.file_close.setEnabled(false);
        this.file_close.addActionListener(this);
        this.file.add(this.file_close);

        this.file_exit = new JMenuItem("Exit");
        this.file_exit.addActionListener(this);
        this.file.add(this.file_exit);

        this.menuBar.add(this.file);
    }

    private void createEditMenu(){
        this.edit = new JMenu("Edit");
        this.edit.setMnemonic(KeyEvent.VK_E);
        this.edit.setEnabled(false);

        this.edit_regen_id = new JMenuItem("Regenerate Ids");
        this.edit_regen_id.addActionListener(this);
        this.edit.add(this.edit_regen_id);

        this.edit_set_p1_spawn = new JMenuItem("Set Visual Spawn");
        this.edit_set_p1_spawn.addActionListener(this);
        this.edit.add(this.edit_set_p1_spawn);

        this.edit_set_p2_spawn = new JMenuItem("Set Audio Spawn");
        this.edit_set_p2_spawn.addActionListener(this);
        this.edit.add(this.edit_set_p2_spawn);

        this.menuBar.add(this.edit);
    }

    private void createAddMenu(){
        this.add = new JMenu("Add");
        this.add.setMnemonic(KeyEvent.VK_A);
        this.add.setEnabled(false);

        this.addTrigger = new JMenu("Add Trigger...");
        this.add.add(this.addTrigger);
        this.addPanel = new JMenu("Add Panel...");
        this.add.add(this.addPanel);
        this.addEntity = new JMenu("Add Entity...");
        this.add.add(this.addEntity);

        this.menuBar.add(this.add);
    }

    private void createCreateMenu(){
        this.create = new JMenu("Create");
        this.create.setMnemonic(KeyEvent.VK_C);
        this.create.setEnabled(false);

        this.create_link = new JMenuItem("Create Link");
        this.create_link.addActionListener(this);
        this.create.add(this.create_link);

        this.menuBar.add(this.create);
    }

    // FILE ACTIONS

    private void actionNew(){
        NewLevelDialog levelDialog = new NewLevelDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        levelDialog.setVisible(true);

        if (levelDialog.getStatus()){
            this.currentLevel = new Level(levelDialog.getName(), levelDialog.getAuthor(), levelDialog.getVersion());

            LevelNode newRoot = this.currentLevel.getLevelNode();

            LevelNode level = new LevelNode("Level", true);
            this.triggers = new LevelNode("Triggers", true);
            this.panels = new LevelNode("Panels", true);
            this.entities = new LevelNode("Entities", true);

            level.add(this.triggers);
            level.add(this.panels);
            level.add(this.entities);
            newRoot.add(level);

            this.treeModel.setRoot(newRoot);

            this.tree.setEditable(true);
            this.file_save_as.setEnabled(true);
            this.file_close.setEnabled(true);
            this.ctxDelete.setEnabled(true);

            this.add.setEnabled(true);
            this.create.setEnabled(true);
            this.edit.setEnabled(true);
        }
    }

    private void actionOpen(){
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileUtils.LevelFileFilter());

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION){
            this.loadedFile = fc.getSelectedFile();
            this.load(this.loadedFile);
            this.file_save.setEnabled(true);
            this.file_save_as.setEnabled(true);
            this.file_close.setEnabled(true);
            this.ctxDelete.setEnabled(true);
            this.add.setEnabled(true);
            this.create.setEnabled(true);
            this.edit.setEnabled(true);
        }
    }

    /**
     * Build the JTree from the contents of Level.
     */
    private void buildTree(){
        LevelNode rootNode = this.currentLevel.getLevelNode();
        LevelNode level = new LevelNode("Level", true);
        this.triggers = new LevelNode("Triggers", true);
        this.panels = new LevelNode("Panels", true);
        this.entities = new LevelNode("Entities", true);

        for (ILevelItem item : this.currentLevel.getTriggers()){
            if (item.getLevelNode() == null){
                LogHelper.severe("ILevelItem '" + item + "' gives a null LevelNode()! Skipping..");
                continue;
            }
            this.triggers.add(item.getLevelNode());
        }

        for (ILevelItem item : this.currentLevel.getPanels()){
            if (item.getLevelNode() == null){
                LogHelper.severe("ILevelItem '" + item + "' gives a null LevelNode()! Skipping..");
                continue;
            }
            this.panels.add(item.getLevelNode());
        }

        for (ILevelItem item : this.currentLevel.getEntities()){
            if (item.getLevelNode() == null){
                LogHelper.severe("ILevelItem '" + item + "' gives a null LevelNode()! Skipping..");
                continue;
            }
            this.entities.add(item.getLevelNode());
        }

        level.add(this.triggers);
        level.add(this.panels);
        level.add(this.entities);
        rootNode.add(level);

        this.treeModel.setRoot(rootNode);
    }

    private void actionSaveAs(){
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileUtils.LevelFileFilter());

        int returnVal = fc.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION){
            this.loadedFile = fc.getSelectedFile();
            this.write();
            this.file_save.setEnabled(true);

            ((DefaultMutableTreeNode) this.treeModel.getRoot()).setUserObject(this.loadedFile.toString());
        }
    }

    private void actionClose(){
        this.tree.setEditable(false);
        this.file_save.setEnabled(false);
        this.file_save_as.setEnabled(false);
        this.file_close.setEnabled(false);

        this.add.setEnabled(false);
        this.create.setEnabled(false);
        this.edit.setEnabled(false);
        this.ctxDelete.setEnabled(false);

        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode("RSLE");
        this.treeModel.setRoot(newRoot);
        this.triggers = null;
        this.panels = null;
        this.entities = null;

        this.loadedFile = null;
    }

    private void actionEditP1Spawn(){
        SetSpawnDialog setSpawn = new SetSpawnDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        setSpawn.setTitle("Set Visual Spawn");
        setSpawn.setVisible(true);

        if (setSpawn.getStatus()){
            Vector3f loc = setSpawn.getLocationVector();
            float rotation = setSpawn.getRotation();

            this.currentLevel.setVisualSpawn(loc, rotation);
            this.buildTree();
        }
    }

    private void actionEditP2Spawn(){
        SetSpawnDialog setSpawn = new SetSpawnDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        setSpawn.setTitle("Set Visual Spawn");
        setSpawn.setVisible(true);

        if (setSpawn.getStatus()){
            Vector3f loc = setSpawn.getLocationVector();
            float rotation = setSpawn.getRotation();

            this.currentLevel.setAudioSpawn(loc, rotation);
            this.buildTree();
        }
    }

    private void actionEditRegenId(){
        if (this.currentLevel == null){
            JOptionPane.showMessageDialog(this, "This action requires a level to be loaded.", "Regenerate IDs", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "This will save all changes, and might break object links.\nAre you sure you want to do this?", "Regenerate IDs", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION){
            if (this.currentLevel.getFileName() != null)
                this.write();

            this.currentLevel.regenIds();
            this.buildTree();

            JOptionPane.showMessageDialog(this, "IDs have ben regenerated.", "Regenerate IDs", JOptionPane.INFORMATION_MESSAGE);
            if (this.currentLevel.getFileName() != null)
                this.write();
        } else {
            JOptionPane.showMessageDialog(this, "IDs will not be re-generated.", "Regenerate IDs", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void addItem(ILevelItem item, ItemBucket bucket){
        item.setID(this.currentLevel.getNextId());
        this.currentLevel.addItem(item);

        switch (bucket){
            case PANELS:
                this.treeModel.insertNodeInto(item.getLevelNode(), this.panels, this.panels.getChildCount());
                break;
            case TRIGGERS:
                this.treeModel.insertNodeInto(item.getLevelNode(), this.triggers, this.triggers.getChildCount());
                break;
            case ENTITIES:
                this.treeModel.insertNodeInto(item.getLevelNode(), this.entities, this.entities.getChildCount());
                break;
        }
    }

    private void actionCreateLink(){
        CreateLinkDialog linkDialog = new CreateLinkDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        linkDialog.setValidIds(this.currentLevel.getLinkables());
        linkDialog.setVisible(true);

        if (linkDialog.getStatus()){
            Long from = linkDialog.getFrom();
            Long to = linkDialog.getTo();

            ITriggerable linkable = (ITriggerable) this.currentLevel.getItem(from);
            linkable.link(to);

            // TODO: Find a better way to handle this
            this.buildTree();
        }
    }

    private void actionDelete( ActionEvent e ){
        TreePath path = this.tree.getSelectionPath();
        System.out.println(path);
        if (path.getPathCount() == 4){
            LevelNode node = (LevelNode) path.getLastPathComponent();
            this.treeModel.removeNodeFromParent(node);
            this.currentLevel.removeItem(node.getSourceItem().getID());
        }
    }

    @Override
    public void actionPerformed( ActionEvent e ){
        if (e.getSource() == this.file_exit){
            System.exit(0);
        } else if (e.getSource() == this.file_new){
            this.actionNew();
        } else if (e.getSource() == this.file_open){
            this.actionOpen();
        } else if (e.getSource() == this.file_save){
            this.write();
        } else if (e.getSource() == this.file_save_as){
            this.actionSaveAs();
        } else if (e.getSource() == this.file_close){
            this.actionClose();
        } else if (e.getSource() == this.edit_regen_id){
            this.actionEditRegenId();
        } else if (e.getSource() == this.edit_set_p1_spawn){
            this.actionEditP1Spawn();
        } else if (e.getSource() == this.edit_set_p2_spawn){
            this.actionEditP2Spawn();
        } else if (e.getSource() == this.create_link){
            this.actionCreateLink();
        } else if (e.getSource() == this.ctxDelete){
            this.actionDelete(e);
        }
    }

    /**
     * Write the level to disk.
     */
    public void write(){
        try{
            this.currentLevel.setFile(this.loadedFile);
            this.currentLevel.saveLevel();
            LevelLoader.write(this.currentLevel);
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "There was an error saving the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LogHelper.severe("Error saving level file!", ex);
        }
    }

    @Override
    public void mouseClicked( MouseEvent e ){
        if (SwingUtilities.isRightMouseButton(e)){
            int row = this.tree.getClosestRowForLocation(e.getX(), e.getY());
            this.tree.setSelectionRow(row);
            TreePath path = this.tree.getClosestPathForLocation(e.getX(), e.getY());
            if (path.getPathCount() == 4){
                this.ctxMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mousePressed( MouseEvent e ){
        // NO-OP
    }

    @Override
    public void mouseReleased( MouseEvent e ){
        // NO-OP
    }

    @Override
    public void mouseEntered( MouseEvent e ){
        // NO-OP
    }

    @Override
    public void mouseExited( MouseEvent e ){
        // NO-OP
    }

    public enum ItemBucket {
        TRIGGERS,
        PANELS,
        ENTITIES
    }
}
