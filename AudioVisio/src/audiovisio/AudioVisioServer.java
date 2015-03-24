package audiovisio;

import audiovisio.states.ServerAppState;
import audiovisio.utils.LogHelper;

import com.jme3.system.JmeContext;

import audiovisio.utils.NetworkUtils;

import java.util.logging.Level;

public class AudioVisioServer {
	private static ServerAppState serverApp;
	
	public static void main(String[] args) {
		NetworkUtils.setPort(11550);
        LogHelper.init();
        LogHelper.setLevel(Level.INFO);
		
		serverApp = new ServerAppState();
		serverApp.start(JmeContext.Type.Headless);
	}
}
