package audiovisio;


import audiovisio.states.ClientAppState;
import audiovisio.states.GuiAppState;
import audiovisio.states.ServerAppState;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;
import com.jme3.app.SimpleApplication;
import com.jme3.network.NetworkClient;
import com.jme3.system.JmeContext;

import java.util.logging.Level;

public class AudioVisio extends SimpleApplication{

	GuiAppState gui;
	public ClientAppState client;
	ServerAppState server;

    static JmeContext.Type appType = JmeContext.Type.Display;

    static boolean startServer = false;
	
	public AudioVisio(){
		
	}

    public static void main(String[] args) {
    	AudioVisio AV = new AudioVisio();

        NetworkUtils.setPort(11550);
        LogHelper.init();
        LogHelper.setLevel(Level.INFO);

        if (args.length == 1) {
            if (args[0].equals("-server")) {
                startServer = true;
                appType = JmeContext.Type.Headless;
            }
        }

		//Client Start
		AV.setPauseOnLostFocus(false);
		AV.start(appType);
    }

	@Override
	public void simpleInitApp() {
        if (startServer) {
            serverStart();
        } else {
            gui = new GuiAppState();
            stateManager.attach(gui);
        }
	}
	
	public int getWidth(){
		return settings.getWidth();
	}
	
	public int getHeight(){
		return settings.getHeight();
	}
	
	public void setFPSText(CharSequence text){
		fpsText.setText(text);
	}

    public void stopGui() {
        stateManager.detach(gui);
    }

    public void serverStart() {
        server = new ServerAppState();
        stateManager.attach(server);
    }

    public void clientStart() {
        client = new ClientAppState();
        stateManager.attach(client);
    }

}