package audiovisio;


import audiovisio.networking.utilities.GeneralUtilities;
import audiovisio.states.ClientAppState;
import audiovisio.states.MenuAppState;
import audiovisio.states.ServerAppState;
import audiovisio.utils.LogHelper;

import audiovisio.networking.Client;
import audiovisio.networking.utilities.GeneralUtilities;
import audiovisio.utils.LogHelper;



import java.util.logging.Level;

import com.jme3.app.Application;

public class AudioVisio {

	static Client clientApp = new Client();
	
	static MenuAppState gui;
	static ClientAppState client;
	static ServerAppState server;

    public static void main(String[] args) {
    	
    	GeneralUtilities.setPort(11550);
    	LogHelper.init();
        LogHelper.setLevel(Level.INFO);
	
		//Client Start
		clientApp.setPauseOnLostFocus(false);
    	clientApp.start();
    	
    	//gui = new MenuAppState();
    	//gui.setEnabled(true);
    	
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
}