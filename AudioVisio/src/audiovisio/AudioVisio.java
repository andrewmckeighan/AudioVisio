package audiovisio;


import audiovisio.states.ClientAppState;
import audiovisio.states.GuiAppState;
import audiovisio.states.ServerAppState;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;
import com.jme3.app.SimpleApplication;

import java.util.logging.Level;

public class AudioVisio extends SimpleApplication{

	static GuiAppState gui;
	static ClientAppState client;
	static ServerAppState server;
	
	public AudioVisio(){
		
	}

    public static void main(String[] args) {
    	AudioVisio AV = new AudioVisio();
    	
    	NetworkUtils.setPort(11550);
    	LogHelper.init();
        LogHelper.setLevel(Level.INFO);
	
		//Client Start
		AV.setPauseOnLostFocus(false);
		AV.start();
    }

	@Override
	public void simpleInitApp() {
		gui = new GuiAppState();
		stateManager.attach(gui);
	}
	
	public int getWidth(){
		return settings.getWidth();
	}
	
	public int getHeight(){
		return settings.getHeight();
	}
	
	public void setFPSText(CharSequence text){
		fpsText.setText(text);
	}
}