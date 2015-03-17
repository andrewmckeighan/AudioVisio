package audiovisio;

import audiovisio.utils.LogHelper;
import com.jme3.system.JmeContext;

import audiovisio.networking.Client;
import audiovisio.networking.Server;
import audiovisio.utils.NetworkUtils;

public class ClientAndServer {
	private static Server serverApp;
	private static Client clientApp;

    public static void startServerAndClient() {
    	NetworkUtils.setPort(11550);
        LogHelper.init();

    	//Server Start
		serverApp = new Server();
		serverApp.start(JmeContext.Type.Headless);
		//Client Start
    	clientApp = new Client();
		clientApp.setPauseOnLostFocus(false);
    	clientApp.start();
   
    }
}
