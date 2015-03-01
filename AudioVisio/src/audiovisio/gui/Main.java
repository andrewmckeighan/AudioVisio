package audiovisio.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;

public class Main extends SimpleApplication {

	private Nifty nifty;

	public static void main(String args[]) {
		// Gui gui = new Gui();
		// gui.start();
//		niftyTest test = new niftyTest();
//		test.start();
		AppSettings settings = new AppSettings(true);
		settings.setAudioRenderer("LWJGL");
		Main app = new Main();
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

}