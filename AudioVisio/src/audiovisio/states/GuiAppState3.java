package audiovisio.states;

import java.net.InetAddress;
import java.net.UnknownHostException;

import audiovisio.AudioVisio;
import audiovisio.StateExperiment;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class GuiAppState3 extends AbstractAppState implements ScreenController{

	private Nifty nifty;
	
	StateExperiment app;
    AppStateManager stateManager;
	
	public GuiAppState3(){
		
	}
	
	 @Override
	 public void initialize(AppStateManager stateManager, Application app){
		 super.initialize(stateManager, app);
		 this.app = (StateExperiment)app;
         this.stateManager = stateManager;

         NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(),
                 app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
         nifty = niftyDisplay.getNifty();
         nifty.fromXml("Interface/baselayer.xml", "start", this);
         this.app.getGuiViewPort().addProcessor(niftyDisplay);
         this.app.getFlyByCamera().setEnabled(false);
         this.app.getInputManager().setCursorVisible(true);
	 }
	 
	 
	public void GUIStart() {

		AppSettings settings = new AppSettings(true);
		settings.setAudioRenderer("LWJGL");
		
		app.setSettings(settings);
		app.start();
		
	}
	
	public void quitGui(){
		app.stop();
	}
	
	public void initHost(){
		app.switchState();
	}
	
	public void initJoin(){
		nifty.gotoScreen("join");
	}
	
	public void initSettings(){
		nifty.gotoScreen("settings");
	}
	
	public void initKeybind(){
		nifty.gotoScreen("keybindings");
	}
	
	public void goBack(){
		nifty.fromXml("Interface/baselayer.xml", "start");
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
//
//    public void clientAndServerInit() {
//        app.stopGui();
//		setEnabled(false);
//        AudioVisio.main(new String[]{"-server"});
//        NetworkUtils.attemptConnection(app.client.myClient);
//        app.clientStart();
//    }

}