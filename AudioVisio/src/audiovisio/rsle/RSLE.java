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
        setSize( 600, 400 );

        menuBar = new JMenuBar();
        createMenu();

        tree = new JTree();
        tree.setEditable( false );
        tree.setCellRenderer( new LevelNodeRenderer() );
        tree.setCellEditor( new LevelNodeEditor( tree, (DefaultTreeCellRenderer) tree.getCellRenderer() ) );
        tree.setShowsRootHandles( true );

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode( "RSLE" );
        treeModel = new DefaultTreeModel( rootNode );
        tree.setModel( treeModel );
        add( new JScrollPane( tree ) );
    }

    private void createMenu() {
        createFileMenu();
        createEditMenu();
        createAddMenu();
        createCreateMenu();
    }

    private void createFileMenu() {
        file = new JMenu( "File" );
        file.setMnemonic( KeyEvent.VK_F );

        file_new = new JMenuItem( "New" );
        file_new.addActionListener( this );
        file.add( file_new );

        file_open = new JMenuItem( "Open" );
        file_open.addActionListener( this );
        file.add( file_open );

        file_save = new JMenuItem( "Save" );
        file_save.addActionListener( this );
        file_save.setEnabled( false );
        file.add( file_save );

        file_save_as = new JMenuItem( "Save As" );
        file_save_as.addActionListener( this );
        file_save_as.setEnabled( false );
        file.add( file_save_as );

        file.addSeparator();

        file_close = new JMenuItem( "Close" );
        file_close.setEnabled( false );
        file_close.addActionListener( this );
        file.add( file_close );

        file_exit = new JMenuItem( "Exit" );
        file_exit.addActionListener( this );
        file.add( file_exit );

        menuBar.add( file );
    }

    private void createEditMenu() {
        edit = new JMenu( "Edit" );
        edit.setMnemonic( KeyEvent.VK_E );

        edit_copy = new JMenuItem( "Copy" );
        edit_copy.setEnabled( false );
        edit.add( edit_copy );

        edit_cut = new JMenuItem( "Cut" );
        edit_cut.setEnabled( false );
        edit.add( edit_cut );

        edit_paste = new JMenuItem( "Paste" );
        edit_paste.setEnabled( false );
        edit.add( edit_paste );

        menuBar.add( edit );
    }

    private void createAddMenu() {
        add = new JMenu( "Add" );
        add.setMnemonic( KeyEvent.VK_A );
        add.setEnabled( false );

        add_trigger = new JMenuItem( "Add Trigger" );
        add_trigger.addActionListener( this );
        add.add( add_trigger );

        add_panels = new JMenu( "Add Panel..." );

        add_panels_panel = new JMenuItem( "Panel" );
        add_panels_panel.addActionListener( this );
        add_panels.add( add_panels_panel );

        add_panels_stair = new JMenuItem( "Stair" );
        add_panels_stair.addActionListener( this );
        add_panels.add( add_panels_stair );
        add.add( add_panels );

        add_entities = new JMenu( "Add Entity..." );

        add_door = new JMenuItem( "Door" );
        add_door.addActionListener( this );
        add_entities.add( add_door );
        add.add( add_entities );

        menuBar.add( add );
    }

    private void createCreateMenu() {
        create = new JMenu( "Create" );
        create.setMnemonic( KeyEvent.VK_C );
        create.setEnabled( false );

        create_floor = new JMenuItem( "Create Floor" );
        create_floor.addActionListener( this );
        create.add( create_floor );

        menuBar.add( create );
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
                createAndShowGUI();
            }
        } );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        if ( e.getSource() == file_exit ) {
            System.exit( 0 );
        } else if ( e.getSource() == file_new ) {
            actionNew();
        } else if ( e.getSource() == file_open ) {
            actionOpen();
        } else if ( e.getSource() == file_save ) {
            write( loadedFile );
        } else if ( e.getSource() == file_save_as ) {
            actionSaveAs();
        } else if ( e.getSource() == file_close ) {
            actionClose();
        } else if ( e.getSource() == add_trigger ) {
            actionAddTrigger();
        } else if ( e.getSource() == add_door ) {
            actionAddDoor();
        } else if ( e.getSource() == add_panels_panel ) {
            actionAddPanel();
        } else if ( e.getSource() == add_panels_stair ) {
            actionAddStair();
        } else if ( e.getSource() == create_floor ) {
            actionCreateFloor();
        }
    }

    private void actionNew() {
        NewLevelDialog levelDialog = new NewLevelDialog( (JFrame) SwingUtilities.getWindowAncestor( this ), true );
        levelDialog.setVisible( true );

        if ( levelDialog.getStatus() ) {
            DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode( levelDialog.getName() );
            treeModel.setRoot( newRoot );

            levelName = new LevelNode( "Name", levelDialog.getName(), false );
            levelAuthor = new LevelNode( "Author", levelDialog.getAuthor(), false );
            levelVersion = new LevelNode( "Version", levelDialog.getVersion(), false );
            newRoot.add( levelName );
            newRoot.add( levelAuthor );
            newRoot.add( levelVersion );

            LevelNode level = new LevelNode( "Level", true );
            triggers = new LevelNode( "Triggers", true );
            panels = new LevelNode( "Panels", true );
            entities = new LevelNode( "Entities", true );

            level.add( triggers );
            level.add( panels );
            level.add( entities );
            newRoot.add( level );

            tree.setEditable( true );
            file_save_as.setEnabled( true );
            file_close.setEnabled( true );

            add.setEnabled( true );
            create.setEnabled( true );
        }
    }

    private void actionOpen() {
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter( new FileUtils.LevelFileFilter() );

        int returnVal = fc.showOpenDialog( this );

        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            loadedFile = fc.getSelectedFile();
            load( loadedFile );
            file_save.setEnabled( true );
            file_save_as.setEnabled( true );
            file_close.setEnabled( true );
            add.setEnabled( true );
            create.setEnabled( true );
        }
    }

    private void actionSaveAs() {
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter( new FileUtils.LevelFileFilter() );

        int returnVal = fc.showSaveDialog( this );

        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            loadedFile = fc.getSelectedFile();
            write( loadedFile );
            file_save.setEnabled( true );

            ( (DefaultMutableTreeNode) treeModel.getRoot() ).setUserObject( loadedFile.toString() );
        }
    }

    private void actionClose() {
        tree.setEditable( false );
        file_save.setEnabled( false );
        file_save_as.setEnabled( false );
        file_close.setEnabled( false );

        add.setEnabled( false );
        create.setEnabled( false );

        DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode( "RSLE" );
        treeModel.setRoot( newRoot );
        triggers = null;
        panels = null;
        entities = null;
        levelAuthor = null;
        levelName = null;
        levelVersion = null;

        loadedFile = null;
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

            treeModel.insertNodeInto( triggerNode, triggers, 0 );
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

            treeModel.insertNodeInto( doorNode, entities, 0 );
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

            treeModel.insertNodeInto( panelNode, panels, 0 );
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

            treeModel.insertNodeInto( stairNode, panels, 0 );
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

                    treeModel.insertNodeInto( panelNode, panels, 0 );
                }
            }
        }
    }

    private static int ID = 0;

    public static int getNextID() {
        int newId = ID;
        ID++;
        return newId;
    }

    public void write( File file ) {
        JSONObject level = new JSONObject();
        level.put( "name", levelName.getValue() );
        level.put( "author", levelAuthor.getValue() );
        level.put( "version", levelVersion.getValue() );
        level.put( "format", CURRENT_LEVEL_FORMAT );

        JSONArray levelArr = new JSONArray();

        Enumeration trigs = triggers.children();
        while ( trigs.hasMoreElements() ) {
            Object next = trigs.nextElement();
            if ( next instanceof LevelNode ) {
                JSONObject trigObj = new JSONObject();
                LevelNode trig = (LevelNode) next;

                Enumeration keys = trig.children();
                while ( keys.hasMoreElements() ) {
                    LevelNode node = (LevelNode) keys.nextElement();
                    Pair pair = (Pair) node.getUserObject();
                    trigObj.put( ( (String) pair.getKey() ).toLowerCase(), pair.getValue() );
                }

                levelArr.add( trigObj );
            }
        }

        Enumeration pnls = panels.children();
        while ( pnls.hasMoreElements() ) {
            Object next = pnls.nextElement();
            if ( next instanceof LevelNode ) {
                JSONObject pnlObj = new JSONObject();
                LevelNode panel = (LevelNode) next;

                Enumeration keys = panel.children();
                while ( keys.hasMoreElements() ) {
                    LevelNode node = (LevelNode) keys.nextElement();
                    Pair pair = (Pair) node.getUserObject();
                    pnlObj.put( ( (String) pair.getKey() ).toLowerCase(), pair.getValue() );
                }

                levelArr.add( pnlObj );
            }
        }

        Enumeration enty = entities.children();
        while ( enty.hasMoreElements() ) {
            Object next = enty.nextElement();
            if ( next instanceof LevelNode ) {
                JSONObject entyObj = new JSONObject();
                LevelNode entity = (LevelNode) next;

                Enumeration keys = entity.children();
                while ( keys.hasMoreElements() ) {
                    LevelNode node = (LevelNode) keys.nextElement();
                    Pair pair = (Pair) node.getUserObject();
                    entyObj.put( ( (String) pair.getKey() ).toLowerCase(), pair.getValue() );
                }

                levelArr.add( entyObj );
            }
        }

        level.put( "level", levelArr );

        try {
            FileWriter fw = new FileWriter( file );
            level.writeJSONString( fw );
            fw.flush();
            fw.close();
            JOptionPane.showMessageDialog( this, "File successfully saved.", "Success", JOptionPane.INFORMATION_MESSAGE );
        } catch ( IOException e ) {
            JOptionPane.showMessageDialog( this, "There was an error writing to the file.", "Error", JOptionPane.ERROR_MESSAGE );
            System.err.println( "Error Writing File" );
            e.printStackTrace();
        }
    }

    public void load( File file ) {
        JSONParser parser = new JSONParser();

        FileReader fileReader = null;
        JSONObject obj = null;

        try {
            fileReader = new FileReader( file );
        } catch ( FileNotFoundException ex ) {
            JOptionPane.showMessageDialog( this, "There was an error opening the file", "Error", JOptionPane.ERROR_MESSAGE );
            return;
        }

        try {
            obj = (JSONObject) parser.parse( fileReader );
            fileReader.close();
        } catch ( IOException ex ) {
            JOptionPane.showMessageDialog( this, "The JSON file was in a bad format", "JSON Error", JOptionPane.ERROR_MESSAGE );
            return;
        } catch ( ParseException ex ) {
            JOptionPane.showMessageDialog( this, "The JSON file could not be parsed", "JSON Error", JOptionPane.ERROR_MESSAGE );
            return;
        }

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode( file.getName() );

        levelName = new LevelNode( "Name", obj.get( "name" ), false );
        levelAuthor = new LevelNode( "Author", obj.get( "author" ), false );
        levelVersion = new LevelNode( "Version", obj.get( "version" ), false );
        rootNode.add( levelName );
        rootNode.add( levelAuthor );
        rootNode.add( levelVersion );

        LevelNode level = new LevelNode( "Level", true );
        triggers = new LevelNode( "Triggers", true );
        panels = new LevelNode( "Panels", true );
        entities = new LevelNode( "Entities", true );

        JSONArray levelItems = (JSONArray) obj.get( "level" );
        for ( Object item : levelItems ) {
            JSONObject ito = (JSONObject) item;
            String type = (String) ito.get( "type" );

            if ( type.equals( "trigger" ) ) {
                int id = JSONHelper.getInt( ito, "id" );
                String location = (String) ito.get( "location" );
                LevelNode trigger = new LevelNode( String.format( "#%d @ (%s)", id, location ), true );
                LevelNode typeNode = new LevelNode( "Type", "trigger", true );
                LevelNode idNode = new LevelNode( "ID", id, false );
                LevelNode locNode = new LevelNode( "Location", location, false );
                trigger.add( typeNode );
                trigger.add( idNode );
                trigger.add( locNode );

                triggers.add( trigger );
            } else if ( type.equals( "panel" ) ) {
                int id = JSONHelper.getInt( ito, "id" );
                String location = (String) ito.get( "location" );
                LevelNode panel = new LevelNode( String.format( "#%d @ (%s)", id, location ), true );
                LevelNode typeNode = new LevelNode( "Type", "panel", true );
                LevelNode idNode = new LevelNode( "ID", id, false );
                LevelNode locNode = new LevelNode( "Location", location, false );
                panel.add( typeNode );
                panel.add( idNode );
                panel.add( locNode );

                panels.add( panel );
            } else if ( type.equals( "stair" ) ) {
                int id = JSONHelper.getInt( ito, "id" );
                String location = (String) ito.get( "location" );
                String direction = (String) ito.get( "direction" );
                LevelNode stair = new LevelNode( String.format( "#%d stair @ (%s)", id, location ), true );
                LevelNode typeNode = new LevelNode( "Type", "stair", true );
                LevelNode idNode = new LevelNode( "ID", id, false );
                LevelNode dirNode = new LevelNode( "Direction", direction, false );
                LevelNode locNode = new LevelNode( "Location", location, false );
                stair.add( typeNode );
                stair.add( idNode );
                stair.add( dirNode );
                stair.add( locNode );

                panels.add( stair );
            } else if ( type.equals( "door" ) ) {
                int id = JSONHelper.getInt( ito, "id" );
                String location = (String) ito.get( "location" );
                LevelNode door = new LevelNode( String.format( "#%d stair @ (%s)", id, location ), true );
                LevelNode typeNode = new LevelNode( "Type", "door", true );
                LevelNode idNode = new LevelNode( "ID", id, false );
                LevelNode locNode = new LevelNode( "Location", location, false );
                door.add( typeNode );
                door.add( idNode );
                door.add( locNode );

                entities.add( door );
            }
        }

        level.add( triggers );
        level.add( panels );
        level.add( entities );
        rootNode.add( level );

        treeModel.setRoot( rootNode );
        tree.setEditable( true );
    }
}
