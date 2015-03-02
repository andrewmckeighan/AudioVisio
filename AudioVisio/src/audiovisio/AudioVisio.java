package audiovisio;

import audiovisio.gui.Gui;
import audiovisio.networking.Server;
import audiovisio.networking.Client;
import audiovisio.networking.utilities.GeneralUtilities;

import com.jme3.system.JmeContext;

public class AudioVisio {
	
//	private static Server serverApp;
	private static Client clientApp;

    public static void main(String[] args) {
    	GeneralUtilities.setPort(11550);
    	//Server start- put in condition

//		Gui mainScreen = new Gui();
		//mainScreen.start();

    	//Server Start
//		serverApp = new Server();
//		serverApp.start(JmeContext.Type.Headless);
		//Client Start
    	clientApp = new Client();
		clientApp.setPauseOnLostFocus(false);
    	clientApp.start();
   
    }

}