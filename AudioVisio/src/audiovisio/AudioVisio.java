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
    	GeneralUtilities.setPort(6000);
    	//Server start- put in condition
    	serverApp = new Server(//fill this out audiovisio.networking.Server serverApp);

		Gui mainScreen = new Gui();
		mainScreen.start();

    	//Server Start
    	audiovisio.networking.Server serverApp = new Server(null, null);
    	clientApp.start(JmeContext.Type.Headless);
    	//Client Start
    	Client clientApp = new Client();
		clientApp.start();
    }

}