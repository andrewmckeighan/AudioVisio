package audiovisio.states;

import audiovisio.AudioVisio;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GuiAppState extends AbstractAppState implements ScreenController{

	AudioVisio app;
	AppStateManager stateManager;
    NiftyJmeDisplay niftyDisplay;
	private Nifty nifty;

	public GuiAppState(){

	}

	@Override
	public void initialize( AppStateManager stateManager, Application app ){
		super.initialize(stateManager, app);
		this.app = (AudioVisio) app;
		this.stateManager = stateManager;

		this.niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(),
				app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
		this.nifty = this.niftyDisplay.getNifty();
		this.nifty.fromXml("Interface/baselayer.xml", "start", this);
		this.app.getGuiViewPort().addProcessor(this.niftyDisplay);
		this.app.getFlyByCamera().setEnabled(false);
         this.app.getInputManager().setCursorVisible(true);
	}


	public void GUIStart(){

		AppSettings settings = new AppSettings(true);
		settings.setAudioRenderer("LWJGL");

		this.app.setSettings(settings);
		this.app.start();

	}

	public void quitGui(){
		this.app.stop();
	}

	public void initHost(){
		this.nifty.gotoScreen("host");
	}

	public void initJoin(){
		this.nifty.gotoScreen("join");
	}

	public void initSettings(){
		this.nifty.gotoScreen("settings");
	}

	public void initKeybind(){
		this.nifty.gotoScreen("keybindings");
	}

	public void goBack(){
		this.nifty.fromXml("Interface/baselayer.xml", "start");
	}

	public String getIp(){
		String temp = "";
		try{
			System.setProperty("java.net.preferIPv4Stack", "true");
			temp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e){
			LogHelper.warn("UnkownHostException", e);
		}
		return temp;
	}


	@Override
	public void bind( Nifty arg0, Screen arg1 ){
		// TODO Auto-generated method stub

	}


	@Override
	public void onEndScreen(){
		// TODO Auto-generated method stub

	}



	@Override
	public void onStartScreen(){
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanup(){
		this.app.getGuiViewPort().removeProcessor(this.niftyDisplay);
		this.app.getInputManager().setCursorVisible(false);
	}

	public void clientAndServerInit(){
		this.app.stopGui();
		this.setEnabled(false);
		AudioVisio.main(new String[]{ "-server" });
		this.app.clientStart();
		NetworkUtils.attemptConnection(this.app.client.myClient);
	}

}