package audiovisio;

import com.jme3.system.JmeContext;

import audiovisio.networking.Server;
import audiovisio.networking.utilities.GeneralUtilities;

public class AudioVisioServer {
	private static Server serverApp;
	
	public static void main(String[] args) {
		GeneralUtilities.setPort(11550);
		
		serverApp = new Server();
		serverApp.start(JmeContext.Type.Headless);
	}
}
