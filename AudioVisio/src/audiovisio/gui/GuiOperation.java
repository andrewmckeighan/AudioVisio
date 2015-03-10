package audiovisio.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;

import audiovisio.networking.Client;
import audiovisio.networking.Server;
import audiovisio.networking.utilities.GeneralUtilities;
import audiovisio.utils.LogHelper;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class GuiOperation extends SimpleApplication implements ScreenController{

	private Nifty nifty;
	static GuiOperation app = new GuiOperation();
	public static void GUIStart() {

		AppSettings settings = new AppSettings(true);
		settings.setAudioRenderer("LWJGL");
		
		app.setSettings(settings);
		app.start();
		
	}

	public void simpleInitApp() {
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
				inputManager, audioRenderer, guiViewPort);
		nifty = niftyDisplay.getNifty();
		nifty.fromXml("audiovisio/gui/baselayer.xml", "start", this);
		guiViewPort.addProcessor(niftyDisplay);
		flyCam.setEnabled(false);
		inputManager.setCursorVisible(true);
		
	}
	
	public void quitGame(){
		app.stop();
	}
	
	public void initHost(){
		nifty.gotoScreen("host");
	}
	
	public void initJoin(){
		nifty.gotoScreen("join");
	}
	
	public void initSettings(){
		nifty.gotoScreen("settings");
	}
	
	public void goBack(){
		nifty.fromXml("audiovisio/gui/baselayer.xml", "start");
	}
	
	public String getIp() {
		String temp = "";
		try {
			System.setProperty("java.net.preferIPv4Stack" , "true");
			temp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			LogHelper.warn("UnkownHostException" , e);
		}
		return temp;
	}
	
	public static void startServerAndClient() {
		Server serverApp;
		Client clientApp;
		
    	GeneralUtilities.setPort(11550);
    	
		serverApp = new Server();
		serverApp.start(JmeContext.Type.Headless);
    	clientApp = new Client();
		clientApp.setPauseOnLostFocus(false);
    	clientApp.start();
   
    }
	
	public static void startClient() {
		app.stop();
		Client clientApp;
    	GeneralUtilities.setPort(11550);
    	clientApp = new Client();
		clientApp.setPauseOnLostFocus(false);
    	clientApp.start();
   
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