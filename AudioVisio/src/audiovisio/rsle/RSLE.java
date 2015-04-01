package audiovisio.rsle;

import audiovisio.Items;
import audiovisio.entities.Button;
import audiovisio.entities.Door;
import audiovisio.entities.Lever;
import audiovisio.level.*;
import audiovisio.level.Panel;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.rsle.editor.LevelNodeEditor2;
import audiovisio.rsle.editor.LevelNodeRenderer;
import audiovisio.rsle.editor.dialogs.*;
import audiovisio.utils.FileUtils;
import audiovisio.utils.LogHelper;
import com.jme3.math.Vector3f;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

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
    private JMenuItem add_trigger;
    private JMenu     add_panels;
    private JMenuItem add_panels_panel;
    private JMenuItem add_panels_stair;
    private JMenuItem add_panels_wall;
    private JMenu     add_entities;
    private JMenuItem add_button;
    private JMenuItem add_door;
    private JMenuItem add_lever;

    // CREATE ITEMS
    private JMenu     create;
    private JMenuItem create_floor;
    private JMenuItem create_link;
    private JMenuItem create_wall;

    // CONTEXT MENU
    private JPopupMenu ctxMenu;
    private JMenuItem  ctxDelete;

    private LevelNode triggers;
    private LevelNode panels;
    private LevelNode entities;

    // level STUFF
    private File  loadedFile;
    private Level currentLevel;

    public RSLE(){
        super(new GridLayout(1, 0));
        this.setSize(600, 400);

        Items.init(); // Register all ILevelItems with the LevelRegistry
        LogHelper.toggleStackDump(); // I don't want to dump stack for warn or severe messages

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

        this.edit_regen_id = new JMenuItem("Regenerate Ids");
        this.edit_regen_id.addActionListener(this);
        this.edit.add(this.edit_regen_id);

        this.edit_set_p1_spawn = new JMenuItem("Set Player 1 Spawn");
        this.edit_set_p1_spawn.addActionListener(this);
        this.edit.add(this.edit_set_p1_spawn);

        this.edit_set_p2_spawn = new JMenuItem("Set Player 2 Spawn");
        this.edit_set_p2_spawn.addActionListener(this);
        this.edit.add(this.edit_set_p2_spawn);

        this.menuBar.add(this.edit);
    }

    private void createAddMenu(){
        this.add = new JMenu("Add");
        this.add.setMnemonic(KeyEvent.VK_A);
        this.add.setEnabled(false);

        this.add_trigger = new JMenuItem("Add Trigger");
        this.add_trigger.addActionListener(this);
        this.add.add(this.add_trigger);

        this.add_panels = new JMenu("Add Panel...");

        this.add_panels_panel = new JMenuItem("Panel");
        this.add_panels_panel.addActionListener(this);
        this.add_panels.add(this.add_panels_panel);

        this.add_panels_stair = new JMenuItem("Stair");
        this.add_panels_stair.addActionListener(this);
        this.add_panels.add(this.add_panels_stair);

        this.add_panels_wall = new JMenuItem("Wall");
        this.add_panels_wall.addActionListener(this);
        this.add_panels.add(this.add_panels_wall);
        this.add.add(this.add_panels);

        this.add_entities = new JMenu("Add Entity...");

        this.add_button = new JMenuItem("Button");
        this.add_button.addActionListener(this);
        this.add_entities.add(this.add_button);

        this.add_door = new JMenuItem("Door");
        this.add_door.addActionListener(this);
        this.add_entities.add(this.add_door);

        this.add_lever = new JMenuItem("Lever");
        this.add_lever.addActionListener(this);
        this.add_entities.add(this.add_lever);
        this.add.add(this.add_entities);

        this.menuBar.add(this.add);
    }

    private void createCreateMenu(){
        this.create = new JMenu("Create");
        this.create.setMnemonic(KeyEvent.VK_C);
        this.create.setEnabled(false);

        this.create_floor = new JMenuItem("Create Floor");
        this.create_floor.addActionListener(this);
        this.create.add(this.create_floor);

        this.create_link = new JMenuItem("Create Link");
        this.create_link.addActionListener(this);
        this.create.add(this.create_link);

        this.create_wall = new JMenuItem("Create Wall");
        this.create_wall.addActionListener(this);
        this.create.add(this.create_wall);

        this.menuBar.add(this.create);
    }

    public static void main( String[] args ){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                RSLE.createAndShowGUI();
            }
        });
    }

    /**
     * Create and show the JFrame that contains the editor
     */
    private static void createAndShowGUI(){
        JFrame frame = new JFrame("Really Simple Level Editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        RSLE rsle = new RSLE();
        frame.add(rsle);
        frame.pack();
        frame.setJMenuBar(rsle.menuBar);
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
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
        } else if (e.getSource() == this.add_trigger){
            this.actionAddTrigger();
        } else if (e.getSource() == this.add_button){
            this.actionAddButton();
        } else if (e.getSource() == this.add_door){
            this.actionAddDoor();
        } else if (e.getSource() == this.add_lever){
            this.actionAddLever();
        } else if (e.getSource() == this.add_panels_panel){
            this.actionAddPanel();
        } else if (e.getSource() == this.add_panels_stair){
            this.actionAddStair();
        } else if (e.getSource() == this.add_panels_wall){
            this.actionAddWall();
        } else if (e.getSource() == this.create_floor){
            this.actionCreateFloor();
        } else if (e.getSource() == this.create_link){
            this.actionCreateLink();
        } else if (e.getSource() == this.ctxDelete){
            this.actionDelete(e);
        }
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
        }
    }

    /**
     * Load the file from disk
     *
     * @param file The file to load
     */
    public void load( File file ){
        try{
            this.currentLevel = LevelReader.read(file);
            this.currentLevel.loadLevel();
            this.buildTree();

            this.tree.setEditable(true);
        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "There was an error reading the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LogHelper.severe("Error reading level file!", ex);
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

    // EDIT ACTIONS

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

    // ADD ACTIONS

    private void actionClose(){
        this.tree.setEditable(false);
        this.file_save.setEnabled(false);
        this.file_save_as.setEnabled(false);
        this.file_close.setEnabled(false);

        this.add.setEnabled(false);
        this.create.setEnabled(false);
        this.ctxDelete.setEnabled(true);

        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode("RSLE");
        this.treeModel.setRoot(newRoot);
        this.triggers = null;
        this.panels = null;
        this.entities = null;

        this.loadedFile = null;
    }

    // ADD > ENTITY ACTIONS

    private void actionEditRegenId(){
        if (this.currentLevel == null){
            JOptionPane.showMessageDialog(this, "This action requires a level to be loaded.", "Regenerate IDs", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "This will save all changes, and will break object links.\nAre you sure you want to do this?", "Regenerate IDs", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION){
            this.write();
            this.currentLevel.regenIds();
            this.buildTree();

            JOptionPane.showMessageDialog(this, "IDs have ben regenerated.", "Regenerate IDs", JOptionPane.INFORMATION_MESSAGE);
            this.write();
        } else {
            JOptionPane.showMessageDialog(this, "IDs will not be re-generated.", "Regenerate IDs", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actionAddTrigger(){
        NewTriggerDialog triggerDialog = new NewTriggerDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        triggerDialog.setVisible(true);

        if (triggerDialog.getStatus()){
            Vector3f loc = triggerDialog.getLevelLocation();

            Trigger trigger = new Trigger(loc);
            trigger.setID(this.currentLevel.getNextId());
            this.currentLevel.addItem(trigger);

            this.treeModel.insertNodeInto(trigger.getLevelNode(), this.triggers, 0);
        }
    }

    private void actionAddButton(){
        NewButtonDialog buttonDialog = new NewButtonDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        buttonDialog.setVisible(true);

        if (buttonDialog.getStatus()){
            Vector3f loc = buttonDialog.getLevelLocation();
            String name = buttonDialog.getName();

            audiovisio.entities.Button button = new Button();
            button.location = loc;
            button.setName(name);
            button.setID(this.currentLevel.getNextId());
            this.currentLevel.addItem(button);

            this.treeModel.insertNodeInto(button.getLevelNode(), this.entities, 0);
        }
    }

    // ADD > PANEL ACTIONS

    private void actionAddDoor(){
        NewDoorDialog doorDialog = new NewDoorDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        doorDialog.setVisible(true);

        if (doorDialog.getStatus()){
            Vector3f loc = doorDialog.getLevelLocation();
            String name = doorDialog.getName();
            boolean state = doorDialog.getState();

            Door door = new Door(state);
            door.setName(name);
            door.setID(this.currentLevel.getNextId());
            door.location = loc;
            this.currentLevel.addItem(door);

            this.treeModel.insertNodeInto(door.getLevelNode(), this.entities, 0);
        }
    }

    private void actionAddLever(){
        NewLeverDialog leverDialog = new NewLeverDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        leverDialog.setVisible(true);

        if (leverDialog.getStatus()){
            Vector3f loc = leverDialog.getLevelLocation();
            String name = leverDialog.getName();
            boolean state = leverDialog.getState();

            Lever lever = new Lever(loc);
            lever.setName(name);
            lever.setOn(state);
            lever.setID(this.currentLevel.getNextId());
            this.currentLevel.addItem(lever);

            this.treeModel.insertNodeInto(lever.getLevelNode(), this.entities, 0);
        }
    }

    private void actionAddPanel(){
        NewPanelDialog panelDialog = new NewPanelDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        panelDialog.setVisible(true);

        if (panelDialog.getStatus()){
            Vector3f loc = panelDialog.getLevelLocation();

            Panel panel = new Panel(loc);
            panel.setID(this.currentLevel.getNextId());
            this.currentLevel.addItem(panel);

            this.treeModel.insertNodeInto(panel.getLevelNode(), this.panels, 0);
        }
    }

    private void actionAddStair(){
        NewStairDialog stairDialog = new NewStairDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        stairDialog.setVisible(true);

        if (stairDialog.getStatus()){
            Vector3f loc = stairDialog.getLevelLocation();
            String dir = stairDialog.getDirection();

            Stair stair = new Stair(loc, ILevelItem.Direction.valueOf(dir));
            stair.setID(this.currentLevel.getNextId());
            this.currentLevel.addItem(stair);

            this.treeModel.insertNodeInto(stair.getLevelNode(), this.panels, 0);
        }
    }

    private void actionAddWall(){
        NewWallDialog wallDialog = new NewWallDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        wallDialog.setVisible(true);

        if (wallDialog.getStatus()){
            Vector3f loc = wallDialog.getLevelLocation();
            String dir = wallDialog.getDirection();

            Wall wall = new Wall(loc, Wall.Direction.valueOf(dir));
            wall.setID(this.currentLevel.getNextId());
            this.currentLevel.addItem(wall);

            this.treeModel.insertNodeInto(wall.getLevelNode(), this.panels, 0);
        }
    }

    // CREATE ACTIONS

    private void actionCreateFloor(){
        CreateFloorDialog floorDialog = new CreateFloorDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        floorDialog.setVisible(true);

        if (floorDialog.getStatus()){
            int startX = floorDialog.getStartX();
            int startZ = floorDialog.getStartZ();
            int sizeX = floorDialog.getSizeX();
            int sizeZ = floorDialog.getSizeZ();
            int y = floorDialog.getYPlane();

            for (int x = startX; x < startX + sizeX; x++){
                for (int z = startZ; z < startZ + sizeZ; z++){
                    Panel panel = new Panel(new Vector3f(x, y, z));
                    panel.setID(this.currentLevel.getNextId());

                    this.treeModel.insertNodeInto(panel.getLevelNode(), this.panels, 0);
                    this.currentLevel.addItem(panel);
                }
            }
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

    private void actionCreateWall(){
        CreateWallDialog wallDialog = new CreateWallDialog((JFrame) SwingUtilities.getWindowAncestor(this), true);
        wallDialog.setVisible(true);

        if (wallDialog.getStatus()){
            int startX = wallDialog.getStartX();
            int startZ = wallDialog.getStartZ();
            int size = wallDialog.getWallSize();
            int y = wallDialog.getYPlane();

            String direction = wallDialog.getDirection();

            if ("NORTH-SOUTH".equals(direction) || "SOUTH-NORTH".equals(direction)){
                ILevelItem.Direction face;
                if ("NORTH-SOUTH".equals(direction)){
                    face = ILevelItem.Direction.EAST;
                } else {
                    face = ILevelItem.Direction.WEST;
                }

                for (int x = startX; x < size; x++){
                    Wall wall = new Wall(new Vector3f(x, y, startZ), face);
                    wall.setID(this.currentLevel.getNextId());

                    this.treeModel.insertNodeInto(wall.getLevelNode(), this.panels, 0);
                    this.currentLevel.addItem(wall);
                }
            } else if ("EAST_WEST".equals(direction) || "WEST-EAST".equals(direction)){
                ILevelItem.Direction face;
                if ("EAST-WEST".equals(direction)){
                    face = ILevelItem.Direction.NORTH;
                } else {
                    face = ILevelItem.Direction.SOUTH;
                }

                for (int z = startZ; z < size; z++){
                    Wall wall = new Wall(new Vector3f(startX, y, z), face);
                    wall.setID(this.currentLevel.getNextId());

                    this.treeModel.insertNodeInto(wall.getLevelNode(), this.panels, 0);
                    this.currentLevel.addItem(wall);
                }
            }
        }
    }

    // OTHER ACTIONS

    private void actionDelete( ActionEvent e ){
        TreePath path = this.tree.getSelectionPath();
        System.out.println(path);
        if (path.getPathCount() == 4){
            this.treeModel.removeNodeFromParent((LevelNode) path.getLastPathComponent());
        }
    }

    /**
     * Write the level to disk.
     */
    public void write(){
        try{
            this.currentLevel.setFile(this.loadedFile);
            this.currentLevel.saveLevel();
            LevelWriter.write(this.currentLevel);
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
}
