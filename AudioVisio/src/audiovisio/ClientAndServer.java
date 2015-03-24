package audiovisio;

import audiovisio.states.ClientAppState;
import audiovisio.states.ServerAppState;
import audiovisio.utils.LogHelper;

import com.jme3.system.JmeContext;

import audiovisio.utils.NetworkUtils;

public class ClientAndServer {
	private static ServerAppState serverApp;
	private static ClientAppState clientApp;

    public static void startServerAndClient() {
    	NetworkUtils.setPort(11550);
        LogHelper.init();

    	//Server Start
		serverApp = new ServerAppState();
		serverApp.start(JmeContext.Type.Headless);
		//Client Start
    	clientApp = new ClientAppState();
		clientApp.setPauseOnLostFocus(false);
    	clientApp.start();
   
    }
}
