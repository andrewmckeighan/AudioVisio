package audiovisio;


import audiovisio.states.ClientAppState;
import audiovisio.states.GuiAppState;
import audiovisio.states.ServerAppState;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.system.JmeContext;

import java.util.logging.Level;

public class AudioVisio extends SimpleApplication {

    public static AudioVisio serverInstance;
    public static String level = "demo_level.json";
    static JmeContext.Type appType = JmeContext.Type.Display;
    static boolean        startServer;
    public ClientAppState client;
    GuiAppState gui;
    ServerAppState server;

    public AudioVisio(){

    }

    public static void main( String[] args ){
        AudioVisio AV = new AudioVisio();

        NetworkUtils.setPort(11550);
        LogHelper.init("rsle2Log.log");
        LogHelper.setLevel(Level.INFO);

        NetworkUtils.initializeSerializables();

        if (args.length == 1){
            if (args[0].equals("-server")){
                AudioVisio.startServer = true;
                AudioVisio.appType = JmeContext.Type.Headless;
                AudioVisio.serverInstance = AV;
            }
        }

        //Client Start
        AV.setPauseOnLostFocus(false);
        AV.start(AudioVisio.appType);
    }

    public static void stopServer(){
        if (AudioVisio.serverInstance != null){ AudioVisio.serverInstance.stop(); }
    }

    @Override
    public void simpleInitApp(){
        if (AudioVisio.startServer){
            this.serverStart();
        } else {
            this.gui = new GuiAppState();
            this.stateManager.attach(this.gui);
        }
    }

    public void serverStart(){
        this.server = new ServerAppState();
        this.stateManager.attach(this.server);
    }

    public int getWidth(){
        return this.settings.getWidth();
    }

    public int getHeight(){
        return this.settings.getHeight();
    }

    public void setFPSText( CharSequence text ){
        this.fpsText.setText(text);
    }

    public void stopGui(){
        this.stateManager.detach(this.gui);
    }

    public void clientStart(){
        this.client = new ClientAppState();
        this.stateManager.attach(this.client);
    }
}