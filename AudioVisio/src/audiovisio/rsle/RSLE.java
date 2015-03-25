package audiovisio.rsle;

import audiovisio.rsle.editor.LevelNode;
import audiovisio.rsle.editor.LevelNodeEditor;
import audiovisio.rsle.editor.LevelNodeRenderer;
import audiovisio.rsle.editor.dialogs.*;
import audiovisio.utils.FileUtils;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Enumeration;

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
    public static final String CURRENT_LEVEL_FORMAT = "0.2";

    private   JTree            tree;
    private   DefaultTreeModel treeModel;
    protected JMenuBar         menuBar;

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
    private JMenuItem edit_copy;
    private JMenuItem edit_cut;
    private JMenuItem edit_paste;

    // ADD ITEMS
    private JMenu     add;
    private JMenuItem add_trigger;
    private JMenu     add_panels;
    private JMenuItem add_panels_panel;
    private JMenuItem add_panels_stair;
    private JMenuItem add_entities;
    private JMenuItem add_door;

    // CREATE ITEMS
    private JMenu     create;
    private JMenuItem create_floor;

    // IMPORTANT NODES
    private LevelNode levelName;
    private LevelNode levelAuthor;
    private LevelNode levelVersion;

    private LevelNode triggers;
    private LevelNode panels;
    private LevelNode entities;

    private File loadedFile;

    public RSLE() {
        super( new GridLayout( 1, 0 ) );
        this.setSize( 600, 400 );

        this.menuBar = new JMenuBar();
        this.createMenu();

        this.tree = new JTree();
        this.tree.setEditable(false);
        this.tree.setCellRenderer(new LevelNodeRenderer());
        this.tree.setCellEditor(new LevelNodeEditor(this.tree, (DefaultTreeCellRenderer) this.tree.getCellRenderer()));
        this.tree.setShowsRootHandles(true);

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode( "RSLE" );
        this.treeModel = new DefaultTreeModel( rootNode );
        this.tree.setModel(this.treeModel);
        this.add( new JScrollPane(this.tree) );
    }

    private void createMenu() {
        this.createFileMenu();
        this.createEditMenu();
        this.createAddMenu();
        this.createCreateMenu();
    }

    private void createFileMenu() {
        this.file = new JMenu( "File" );
        this.file.setMnemonic(KeyEvent.VK_F);

        this.file_new = new JMenuItem( "New" );
        this.file_new.addActionListener(this);
        this.file.add(this.file_new);

        this.file_open = new JMenuItem( "Open" );
        this.file_open.addActionListener(this);
        this.file.add(this.file_open);

        this.file_save = new JMenuItem( "Save" );
        this.file_save.addActionListener(this);
        this.file_save.setEnabled(false);
        this.file.add(this.file_save);

        this.file_save_as = new JMenuItem( "Save As" );
        this.file_save_as.addActionListener(this);
        this.file_save_as.setEnabled(false);
        this.file.add(this.file_save_as);

        this.file.addSeparator();

        this.file_close = new JMenuItem( "Close" );
        this.file_close.setEnabled(false);
        this.file_close.addActionListener(this);
        this.file.add(this.file_close);

        this.file_exit = new JMenuItem( "Exit" );
        this.file_exit.addActionListener(this);
        this.file.add(this.file_exit);

        this.menuBar.add(this.file);
    }

    private void createEditMenu() {
        this.edit = new JMenu( "Edit" );
        this.edit.setMnemonic(KeyEvent.VK_E);

        this.edit_copy = new JMenuItem( "Copy" );
        this.edit_copy.setEnabled(false);
        this.edit.add(this.edit_copy);

        this.edit_cut = new JMenuItem( "Cut" );
        this.edit_cut.setEnabled(false);
        this.edit.add(this.edit_cut);

        this.edit_paste = new JMenuItem( "Paste" );
        this.edit_paste.setEnabled(false);
        this.edit.add(this.edit_paste);

        this.menuBar.add(this.edit);
    }

    private void createAddMenu() {
        this.add = new JMenu( "Add" );
        this.add.setMnemonic(KeyEvent.VK_A);
        this.add.setEnabled(false);

        this.add_trigger = new JMenuItem( "Add Trigger" );
        this.add_trigger.addActionListener(this);
        this.add.add(this.add_trigger);

        this.add_panels = new JMenu( "Add Panel..." );

        this.add_panels_panel = new JMenuItem( "Panel" );
        this.add_panels_panel.addActionListener(this);
        this.add_panels.add(this.add_panels_panel);

        this.add_panels_stair = new JMenuItem( "Stair" );
        this.add_panels_stair.addActionListener(this);
        this.add_panels.add(this.add_panels_stair);
        this.add.add(this.add_panels);

        this.add_entities = new JMenu( "Add Entity..." );

        this.add_door = new JMenuItem( "Door" );
        this.add_door.addActionListener(this);
        this.add_entities.add(this.add_door);
        this.add.add(this.add_entities);

        this.menuBar.add(this.add);
    }

    private void createCreateMenu() {
        this.create = new JMenu( "Create" );
        this.create.setMnemonic(KeyEvent.VK_C);
        this.create.setEnabled(false);

        this.create_floor = new JMenuItem( "Create Floor" );
        this.create_floor.addActionListener(this);
        this.create.add(this.create_floor);

        this.menuBar.add(this.create);
    }

    /**
     * Create and show the JFrame that contains the editor
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame( "Really Simple Level Editor" );
        frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        RSLE rsle = new RSLE();
        frame.add( rsle );
        frame.pack();
        frame.setJMenuBar( rsle.menuBar );
        frame.setVisible( true );
        frame.setExtendedState( Frame.MAXIMIZED_BOTH );
    }

    public static void main( String[] args ) {
        SwingUtilities.invokeLater( new Runnable() {
            @Override
            public void run() {
                RSLE.createAndShowGUI();
            }
        } );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        if ( e.getSource() == this.file_exit) {
            System.exit( 0 );
        } else if ( e.getSource() == this.file_new) {
            this.actionNew();
        } else if ( e.getSource() == this.file_open) {
            this.actionOpen();
        } else if ( e.getSource() == this.file_save) {
            this.write(this.loadedFile);
        } else if ( e.getSource() == this.file_save_as) {
            this.actionSaveAs();
        } else if ( e.getSource() == this.file_close) {
            this.actionClose();
        } else if ( e.getSource() == this.add_trigger) {
            this.actionAddTrigger();
        } else if ( e.getSource() == this.add_door) {
            this.actionAddDoor();
        } else if ( e.getSource() == this.add_panels_panel) {
            this.actionAddPanel();
        } else if ( e.getSource() == this.add_panels_stair) {
            this.actionAddStair();
        } else if ( e.getSource() == this.create_floor) {
            this.actionCreateFloor();
        }
    }

    private void actionNew() {
        NewLevelDialog levelDialog = new NewLevelDialog( (JFrame) SwingUtilities.getWindowAncestor( this ), true );
        levelDialog.setVisible( true );

        if ( levelDialog.getStatus() ) {
            DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode( levelDialog.getName() );
            this.treeModel.setRoot(newRoot);

            this.levelName = new LevelNode( "Name", levelDialog.getName(), false );
            this.levelAuthor = new LevelNode( "Author", levelDialog.getAuthor(), false );
            this.levelVersion = new LevelNode( "Version", levelDialog.getVersion(), false );
            newRoot.add(this.levelName);
            newRoot.add(this.levelAuthor);
            newRoot.add(this.levelVersion);

            LevelNode level = new LevelNode( "Level", true );
            this.triggers = new LevelNode( "Triggers", true );
            this.panels = new LevelNode( "Panels", true );
            this.entities = new LevelNode( "Entities", true );

            level.add(this.triggers);
            level.add(this.panels);
            level.add(this.entities);
            newRoot.add( level );

            this.tree.setEditable(true);
            this.file_save_as.setEnabled(true);
            this.file_close.setEnabled(true);

            this.add.setEnabled(true);
            this.create.setEnabled(true);
        }
    }

    private void actionOpen() {
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter( new FileUtils.LevelFileFilter() );

        int returnVal = fc.showOpenDialog( this );

        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            this.loadedFile = fc.getSelectedFile();
            this.load(this.loadedFile);
            this.file_save.setEnabled(true);
            this.file_save_as.setEnabled(true);
            this.file_close.setEnabled(true);
            this.add.setEnabled(true);
            this.create.setEnabled(true);
        }
    }

    private void actionSaveAs() {
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter( new FileUtils.LevelFileFilter() );

        int returnVal = fc.showSaveDialog( this );

        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            this.loadedFile = fc.getSelectedFile();
            this.write(this.loadedFile);
            this.file_save.setEnabled(true);

            ( (DefaultMutableTreeNode) this.treeModel.getRoot() ).setUserObject(this.loadedFile.toString() );
        }
    }

    private void actionClose() {
        this.tree.setEditable(false);
        this.file_save.setEnabled(false);
        this.file_save_as.setEnabled(false);
        this.file_close.setEnabled(false);

        this.add.setEnabled(false);
        this.create.setEnabled(false);

        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode( "RSLE" );
        this.treeModel.setRoot(newRoot);
        this.triggers = null;
        this.panels = null;
        this.entities = null;
        this.levelAuthor = null;
        this.levelName = null;
        this.levelVersion = null;

        this.loadedFile = null;
    }

    private void actionAddTrigger() {
        NewTriggerDialog triggerDialog = new NewTriggerDialog( (JFrame) SwingUtilities.getWindowAncestor( this ), true );
        triggerDialog.setVisible( true );

        if ( triggerDialog.getStatus() ) {
            String loc = triggerDialog.getLevelLocation();
            int id = triggerDialog.getId();

            LevelNode triggerNode = new LevelNode( String.format( "#%d @ (%s)", id, loc ), true );
            LevelNode typeNode = new LevelNode( "Type", "trigger", true );
            LevelNode idNode = new LevelNode( "ID", id, false );
            LevelNode locNode = new LevelNode( "Location", loc, false );

            triggerNode.add( typeNode );
            triggerNode.add( idNode );
            triggerNode.add( locNode );

            this.treeModel.insertNodeInto(triggerNode, this.triggers, 0);
        }
    }

    private void actionAddDoor() {
        NewDoorDialog doorDialog = new NewDoorDialog( (JFrame) SwingUtilities.getWindowAncestor( this ), true );
        doorDialog.setVisible( true );

        if ( doorDialog.getStatus() ) {
            String loc = doorDialog.getLevelLocation();
            int id = doorDialog.getId();

            LevelNode doorNode = new LevelNode( String.format( "#%d door @ (%s)", id, loc ), true );
            LevelNode typeNode = new LevelNode( "Type", "door", true );
            LevelNode idNode = new LevelNode( "ID", id, false );
            LevelNode locNode = new LevelNode( "Location", loc, false );

            doorNode.add( typeNode );
            doorNode.add( idNode );
            doorNode.add( locNode );

            this.treeModel.insertNodeInto(doorNode, this.entities, 0);
        }
    }

    private void actionAddPanel() {
        NewPanelDialog panelDialog = new NewPanelDialog( (JFrame) SwingUtilities.getWindowAncestor( this ), true );
        panelDialog.setVisible( true );

        if ( panelDialog.getStatus() ) {
            String loc = panelDialog.getLevelLocation();
            int id = panelDialog.getId();

            LevelNode panelNode = new LevelNode( String.format( "#%d @ (%s)", id, loc ), true );
            LevelNode typeNode = new LevelNode( "Type", "panel", true );
            LevelNode idNode = new LevelNode( "ID", id, false );
            LevelNode locNode = new LevelNode( "Location", loc, false );

            panelNode.add( typeNode );
            panelNode.add( idNode );
            panelNode.add( locNode );

            this.treeModel.insertNodeInto(panelNode, this.panels, 0);
        }
    }

    private void actionAddStair() {
        NewStairDialog stairDialog = new NewStairDialog( (JFrame) SwingUtilities.getWindowAncestor( this ), true );
        stairDialog.setVisible( true );

        if ( stairDialog.getStatus() ) {
            String loc = stairDialog.getLevelLocation();
            String dir = stairDialog.getDirection();
            int id = stairDialog.getId();

            LevelNode stairNode = new LevelNode( String.format( "#%d stair @ (%s)", id, loc ), true );
            LevelNode typeNode = new LevelNode( "Type", "stair", true );
            LevelNode idNode = new LevelNode( "ID", id, false );
            LevelNode dirNode = new LevelNode( "Direction", dir, false );
            LevelNode locNode = new LevelNode( "Location", loc, false );

            stairNode.add( typeNode );
            stairNode.add( idNode );
            stairNode.add( dirNode );
            stairNode.add( locNode );

            this.treeModel.insertNodeInto(stairNode, this.panels, 0);
        }
    }

    private void actionCreateFloor() {
        CreateFloorDialog floorDialog = new CreateFloorDialog( (JFrame) SwingUtilities.getWindowAncestor( this ), true );
        floorDialog.setVisible( true );

        if ( floorDialog.getStatus() ) {
            int startX = floorDialog.getStartX();
            int startY = floorDialog.getStartY();
            int sizeX = floorDialog.getSizeX();
            int sizeY = floorDialog.getSizeY();
            int z = floorDialog.getZPlane();

            for ( int i = startX; i < startX + sizeX; i++ ) {
                for ( int j = startY; j < startY + sizeY; j++ ) {
                    String loc = String.format( "%d,%d,%d", i, j, z );
                    int id = RSLE.getNextID();

                    LevelNode panelNode = new LevelNode( String.format( "#%d @ (%s)", id, loc ), true );
                    LevelNode typeNode = new LevelNode( "Type", "panel", true );
                    LevelNode idNode = new LevelNode( "ID", id, false );
                    LevelNode locNode = new LevelNode( "Location", loc, false );

                    panelNode.add( typeNode );
                    panelNode.add( idNode );
                    panelNode.add( locNode );

                    this.treeModel.insertNodeInto(panelNode, this.panels, 0);
                }
            }
        }
    }

    private static int ID;

    public static int getNextID(){
        int newId = RSLE.ID;
        RSLE.ID++;
        return newId;
    }

    public void write( File file ){
        JSONObject level = new JSONObject();
        level.put("name", this.levelName.getValue());
        level.put("author", this.levelAuthor.getValue());
        level.put("version", this.levelVersion.getValue());
        level.put("format", RSLE.CURRENT_LEVEL_FORMAT);

        JSONArray levelArr = new JSONArray();

        Enumeration trigs = this.triggers.children();
        while (trigs.hasMoreElements()){
            Object next = trigs.nextElement();
            if (next instanceof LevelNode){
                JSONObject trigObj = new JSONObject();
                LevelNode trig = (LevelNode) next;

                Enumeration keys = trig.children();
                while (keys.hasMoreElements()){
                    LevelNode node = (LevelNode) keys.nextElement();
                    Pair pair = (Pair) node.getUserObject();
                    trigObj.put(((String) pair.getKey()).toLowerCase(), pair.getValue());
                }

                levelArr.add(trigObj);
            }
        }

        Enumeration pnls = this.panels.children();
        while (pnls.hasMoreElements()){
            Object next = pnls.nextElement();
            if (next instanceof LevelNode){
                JSONObject pnlObj = new JSONObject();
                LevelNode panel = (LevelNode) next;

                Enumeration keys = panel.children();
                while (keys.hasMoreElements()){
                    LevelNode node = (LevelNode) keys.nextElement();
                    Pair pair = (Pair) node.getUserObject();
                    pnlObj.put(((String) pair.getKey()).toLowerCase(), pair.getValue());
                }

                levelArr.add(pnlObj);
            }
        }

        Enumeration enty = this.entities.children();
        while (enty.hasMoreElements()){
            Object next = enty.nextElement();
            if (next instanceof LevelNode){
                JSONObject entyObj = new JSONObject();
                LevelNode entity = (LevelNode) next;

                Enumeration keys = entity.children();
                while (keys.hasMoreElements()){
                    LevelNode node = (LevelNode) keys.nextElement();
                    Pair pair = (Pair) node.getUserObject();
                    entyObj.put(((String) pair.getKey()).toLowerCase(), pair.getValue());
                }

                levelArr.add(entyObj);
            }
        }

        level.put("level", levelArr);

        try{
            FileWriter fw = new FileWriter(file);
            level.writeJSONString(fw);
            fw.flush();
            fw.close();
            JOptionPane.showMessageDialog(this, "File successfully saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e){
            JOptionPane.showMessageDialog(this, "There was an error writing to the file.", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error Writing File");
            e.printStackTrace();
        }
    }

    public void load( File file ){
        JSONParser parser = new JSONParser();

        FileReader fileReader = null;
        JSONObject obj = null;

        try{
            fileReader = new FileReader(file);
        } catch (FileNotFoundException ex){
            JOptionPane.showMessageDialog(this, "There was an error opening the file", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try{
            obj = (JSONObject) parser.parse(fileReader);
            fileReader.close();
        } catch (IOException ex){
            JOptionPane.showMessageDialog(this, "The JSON file was in a bad format", "JSON Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ParseException ex){
            JOptionPane.showMessageDialog(this, "The JSON file could not be parsed", "JSON Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(file.getName());

        this.levelName = new LevelNode("Name", obj.get("name"), false);
        this.levelAuthor = new LevelNode("Author", obj.get("author"), false);
        this.levelVersion = new LevelNode("Version", obj.get("version"), false);
        rootNode.add(this.levelName);
        rootNode.add(this.levelAuthor);
        rootNode.add(this.levelVersion);

        LevelNode level = new LevelNode("Level", true);
        this.triggers = new LevelNode("Triggers", true);
        this.panels = new LevelNode("Panels", true);
        this.entities = new LevelNode("Entities", true);

        JSONArray levelItems = (JSONArray) obj.get("level");
        for (Object item : levelItems){
            JSONObject ito = (JSONObject) item;
            String type = (String) ito.get("type");

            if (type.equals("trigger")){
                int id = JSONHelper.getInt(ito, "id");
                String location = (String) ito.get("location");
                LevelNode trigger = new LevelNode(String.format("#%d @ (%s)", id, location), true);
                LevelNode typeNode = new LevelNode("Type", "trigger", true);
                LevelNode idNode = new LevelNode("ID", id, false);
                LevelNode locNode = new LevelNode("Location", location, false);
                trigger.add(typeNode);
                trigger.add(idNode);
                trigger.add(locNode);

                this.triggers.add(trigger);
            } else if (type.equals("panel")){
                int id = JSONHelper.getInt(ito, "id");
                String location = (String) ito.get("location");
                LevelNode panel = new LevelNode(String.format("#%d @ (%s)", id, location), true);
                LevelNode typeNode = new LevelNode("Type", "panel", true);
                LevelNode idNode = new LevelNode("ID", id, false);
                LevelNode locNode = new LevelNode("Location", location, false);
                panel.add(typeNode);
                panel.add(idNode);
                panel.add(locNode);

                this.panels.add(panel);
            } else if (type.equals("stair")){
                int id = JSONHelper.getInt(ito, "id");
                String location = (String) ito.get("location");
                String direction = (String) ito.get("direction");
                LevelNode stair = new LevelNode(String.format("#%d stair @ (%s)", id, location), true);
                LevelNode typeNode = new LevelNode("Type", "stair", true);
                LevelNode idNode = new LevelNode("ID", id, false);
                LevelNode dirNode = new LevelNode("Direction", direction, false);
                LevelNode locNode = new LevelNode("Location", location, false);
                stair.add(typeNode);
                stair.add(idNode);
                stair.add(dirNode);
                stair.add(locNode);

                this.panels.add(stair);
            } else if (type.equals("door")){
                int id = JSONHelper.getInt(ito, "id");
                String location = (String) ito.get("location");
                LevelNode door = new LevelNode(String.format("#%d stair @ (%s)", id, location), true);
                LevelNode typeNode = new LevelNode("Type", "door", true);
                LevelNode idNode = new LevelNode("ID", id, false);
                LevelNode locNode = new LevelNode("Location", location, false);
                door.add(typeNode);
                door.add(idNode);
                door.add(locNode);

                this.entities.add(door);
            }
        }

        level.add(this.triggers);
        level.add(this.panels);
        level.add(this.entities);
        rootNode.add(level);

        this.treeModel.setRoot(rootNode);
        this.tree.setEditable(true);
    }
}
