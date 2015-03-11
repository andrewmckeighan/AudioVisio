package audiovisio;

import audiovisio.gui.Gui;
import audiovisio.networking.Server;
import audiovisio.networking.Client;
import audiovisio.networking.utilities.GeneralUtilities;

import audiovisio.utils.LogHelper;
import com.jme3.system.JmeContext;

import java.util.logging.Level;

public class AudioVisio {

	private static Client clientApp;

    public static void main(String[] args) {
    	GeneralUtilities.setPort(11550);
		LogHelper.setLevel(Level.FINE);

    	clientApp = new Client();
		clientApp.setPauseOnLostFocus(false);
    	clientApp.start();
   
    }

}