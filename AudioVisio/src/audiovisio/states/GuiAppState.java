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

public class GuiAppState extends AbstractAppState implements ScreenController {

    AudioVisio      app;
    AppStateManager stateManager;
    NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;

    public GuiAppState(){

    }

	/**
	 * Initializes variables to create a GuiAppState
	 * @param stateManager State manager passed by the AudioVisio SimpleApplication.
	 * @param app A SimpleApplication to implement the AppState in (AudioVisio).
	 */
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

	/**
	 * Sets variables to run the GuiAppState to false, closes the GuiAppState in the SimpleApplication.
	 */
	@Override
	public void cleanup(){
		this.app.getGuiViewPort().removeProcessor(this.niftyDisplay);
		this.app.getInputManager().setCursorVisible(false);
	}

	/**
	 * Starts the GUI from the GuiAppState, sets necessary member variables.
	 */
	public void GUIStart(){

		AppSettings settings = new AppSettings(true);
		settings.setAudioRenderer("LWJGL");

		this.app.setSettings(settings);
		this.app.start();

	}

	public void quitAudioVisio(){
		this.app.stop();
	}

	/**
	 * Switches the current screen to the "Host" screen.
	 */
	public void initHost(){
		this.nifty.gotoScreen("host");
	}

	/**
	 * Switches the current screen to the "Join" screen.
	 */
	public void initJoin(){
		this.nifty.gotoScreen("join");
	}

	/**
	 * Switches the current screen to the settings menu.
 	 */
	public void initSettings(){
		this.nifty.gotoScreen("settings");
	}

	/**
	 * Switches the settings screen to the "Keybindings" screen.
	 */
	public void initKeybind(){
		this.nifty.gotoScreen("keybindings");
	}

	/**
	 * Returns from one of the secondary menu screens to the "start screen".
	 */
	public void goBack(){
		this.nifty.fromXml("Interface/baselayer.xml", "start");
	}

	/**
	 * @return Returns the java.net IPv4 IP address of the current user's computer.
	 */
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

	public void setKeyBinding( String EventId ){

	}

	/**
	 * Switches to the ClientAppState from the current GuiAppState.
	 */
	public void clientInit(){
		this.app.stopGui();
		this.setEnabled(false);
		this.app.clientStart();
		NetworkUtils.attemptConnection(this.app.client.myClient);
	}

	/**
	 * Unimplemented method stub.
	 * @param arg0
	 * @param arg1
	 */
	@Override
	public void bind( Nifty arg0, Screen arg1 ){
		// TODO Auto-generated method stub

	}

	/**
	 * Unimplemented method stub.
	 */
	@Override
	public void onStartScreen(){
		// TODO Auto-generated method stub

	}

	/**
	 * Unimplemented method stub.
	 */
	@Override
	public void onEndScreen(){
		// TODO Auto-generated method stub

	}

	/**
	 * Switches to the ClientAppState and ServerAppState from the current GuiAppState.
	 */
	public void clientAndServerInit(){
		this.app.stopGui();
		this.setEnabled(false);
		AudioVisio.main(new String[]{ "-server" });
		this.app.clientStart();
		NetworkUtils.attemptConnection(this.app.client.myClient);
	}

}