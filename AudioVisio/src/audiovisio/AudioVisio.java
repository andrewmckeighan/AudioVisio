package audiovisio;

import audiovisio.networking.Server;
import audiovisio.networking.Client;
import com.jme3.system.JmeContext;

public class AudioVisio {

    public static void main(String[] args) {
<<<<<<< HEAD
		Gui mainScreen = new Gui();
		mainScreen.start();
		
		
=======
    	//Server Start
    	audiovisio.networking.Server serverApp = new Server(null, null);
    	clientApp.start(JmeContext.Type.Headless);
    	//
    	Client clientApp = new Client();
		clientApp.start();
>>>>>>> 277bcfc81eb6b0baf323136798f7e2f6e4d20d4c
    }

}