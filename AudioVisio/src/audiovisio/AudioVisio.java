package audiovisio;

import audiovisio.networking.Server;
import audiovisio.networking.Client;
import audiovisio.networking.utilities.GeneralUtilities;

import com.jme3.system.JmeContext;

public class AudioVisio {

	public static final String CONNECTIP = "127.0.0.1";
	private static audiovisio.networking.Server serverApp;
	private static audiovisio.networking.Client clientApp;
	
    public static void main(String[] args) {
    	Gui mainScreen = new Gui();
		mainScreen.start();
    	
    	GeneralUtilities.setPort(6000);
    	//Server start- put in condition
    	serverApp = new Server(/*fill this out audiovisio.networking.Server serverApp null, null)*/);
    	serverApp.start();
    	//Client Start
    	clientApp = new Client();
		clientApp.start();
    }

}