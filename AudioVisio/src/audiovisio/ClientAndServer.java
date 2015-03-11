package audiovisio;

import com.jme3.system.JmeContext;

import audiovisio.networking.Client;
import audiovisio.networking.Server;
import audiovisio.networking.utilities.GeneralUtilities;

public class ClientAndServer {
	private static Server serverApp;
	private static Client clientApp;

    public static void startServerAndClient() {
    	GeneralUtilities.setPort(11550);

    	//Server Start
		serverApp = new Server();
		serverApp.start(JmeContext.Type.Headless);
		//Client Start
    	clientApp = new Client();
		clientApp.setPauseOnLostFocus(false);
    	clientApp.start();
   
    }
}
