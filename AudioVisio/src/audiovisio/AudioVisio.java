package audiovisio;


import audiovisio.utils.NetworkUtils;
import audiovisio.states.ClientAppState;
import audiovisio.states.ClientAppState;
import audiovisio.states.MenuAppState;
import audiovisio.states.ServerAppState;
import audiovisio.utils.LogHelper;



import java.util.logging.Level;

import com.jme3.app.SimpleApplication;

public class AudioVisio extends SimpleApplication{

	static MenuAppState gui;
	static ClientAppState client;
	static ServerAppState server;

    public static void main(String[] args) {
    	
    	NetworkUtils.setPort(11550);
    	LogHelper.init();
        LogHelper.setLevel(Level.INFO);
	
		//Client Start
		clientApp.setPauseOnLostFocus(false);
    	clientApp.start();
    	
    	
//    	gui = new MenuAppState();
//		gui.setEnabled(true);

    	
    }

	public void clientInit(){
		gui.setEnabled(false);
		client.setEnabled(true);
	}

	public void serverAndClientInit(){
		gui.setEnabled(false);
		client.setEnabled(true);
		client.app.startClient();
		server.setEnabled(true);
		server.app.startServer();
	}

	@Override
	public void simpleInitApp() {
		stateManager.attach(gui);
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
}