package audiovisio.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Main extends SimpleApplication implements ScreenController{

	private Nifty nifty;
	static Main app = new Main();
	public static void main(String args[]) {
//		Gui gui = new Gui();
//		gui.start();
//		niftyTest test = new niftyTest();
//		test.start();
		AppSettings settings = new AppSettings(true);
		settings.setAudioRenderer("LWJGL");
		
		app.setSettings(settings);
		app.start();
		
	}

	public void simpleInitApp() {
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
				inputManager, audioRenderer, guiViewPort);
		nifty = niftyDisplay.getNifty();
		nifty.fromXml("audiovisio/gui/baselayer.xml", "start");
		guiViewPort.addProcessor(niftyDisplay);
		flyCam.setEnabled(false);
		inputManager.setCursorVisible(true);
		
	}
	
	public void quitGame(){
		app.stop();
	}
	
	public void initSettings(){
		//nifty.fromXml("audiovisio/gui/baselayer.xml", "settings");
		nifty.gotoScreen("settings");
	}
	
	public void goBack(){
		nifty.fromXml("audiovisio/gui/baselayer.xml", "start");
	}
	
	@Override
	public void bind(Nifty arg0, Screen arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub
		
	}
}