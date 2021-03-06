package audiovisio;


import audiovisio.level.LevelLoader;
import audiovisio.rsle.RSLE;
import audiovisio.states.ClientAppState;
import audiovisio.states.GuiAppState;
import audiovisio.states.ServerAppState;
import audiovisio.utils.Config;
import audiovisio.utils.FileUtils;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;

public class AudioVisio extends SimpleApplication {

    public static final int     difficulty = 0;
    public static final int     FPS        = 100;
    public static AudioVisio serverInstance;
    public static String level = "color_puzzle.json";
    static        JmeContext.Type appType = JmeContext.Type.Display;
    static boolean        startServer;
    public ClientAppState client;
    GuiAppState    gui;
    ServerAppState server;

    public AudioVisio(){

    }

    public static void main( String[] args ){
        FileUtils.dataDirectorySanityCheck();

        Config.load();
        LevelLoader.initLevelList();

        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
        LogHelper.init();
//        LogHelper.load("Log_" + sdf.format(cal.getTime()) + ".log");
        LogHelper.setLevel(Level.WARNING);
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(AudioVisio.FPS);

        AudioVisio AV = new AudioVisio();
        AV.setSettings(settings);

        NetworkUtils.setPort(11550);


        NetworkUtils.initializeSerializables();

        if (args.length == 1){
            if (args[0].equals("-server")){
                AudioVisio.startServer = true;
                AudioVisio.appType = JmeContext.Type.Headless;
                AudioVisio.serverInstance = AV;
            }
            else if(args[0].equals("-rsle")){
                RSLE.main(args);
                return;
            }
        }

        //Client Start
        AV.setPauseOnLostFocus(false);
        AV.start(AudioVisio.appType);
    }

    public static void stopServer(){
        if (AudioVisio.serverInstance != null){ AudioVisio.serverInstance.stop(); }
    }

    /**
     * Starts the AudioVisio SimpleApplication.
     */
    @Override
    public void simpleInitApp(){
        if (AudioVisio.startServer){
            this.serverStart();
        } else {
            this.gui = new GuiAppState();
            this.stateManager.attach(this.gui);
        }
    }

    /**
     * Creates an instance of the ServerAppState, and switches the state manager to run the server.
     */
    public void serverStart(){
        this.server = new ServerAppState();
        this.stateManager.attach(this.server);
    }

    /**
     * Sets a sequence of characters to display from the server.
     *
     * @param text A char sequence to display from the server.
     */
    public void setFPSText( CharSequence text ){
        this.fpsText.setText(text);
    }

    /**
     * Detaches the GuiAppState from the Audio Visio simple Application.
     */
    public void stopGui(){
        this.stateManager.detach(this.gui);
    }

    /**
     * Creates an instance of the ClientAppState, and switches the state manager to run the client.
     */
    public void clientStart(){
        this.client = new ClientAppState();
        this.stateManager.attach(this.client);
    }

    public void stopGame() {
        this.stateManager.detach(this.client);
        this.gui = new GuiAppState();
        this.gui.setEnabled(true);
        this.stateManager.attach(this.gui);
    }

    /**
     * @return returns the width of for
     */
    public int getWidth() {
        return this.settings.getWidth();
    }

    /**
     * @return returns the height of for
     */
    public int getHeight() {
        return this.settings.getHeight();
    }
}