package audiovisio.states;

import audiovisio.AudioVisio;
import audiovisio.level.Level;
import audiovisio.level.LevelLoader;
import audiovisio.utils.LogHelper;
import audiovisio.utils.NetworkUtils;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class GuiAppState extends AbstractAppState implements ScreenController, RawInputListener {

    AudioVisio      app;
    AppStateManager stateManager;
    NiftyJmeDisplay niftyDisplay;
	Button btn;
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

		this.app.getInputManager().addRawInputListener(this);

    }

	/**
	 * Sets variables to run the GuiAppState to false, closes the GuiAppState in the SimpleApplication.
	 */
	@Override
	public void cleanup(){
		this.app.getGuiViewPort().removeProcessor(this.niftyDisplay);
		this.app.getInputManager().setCursorVisible(false);
		this.app.getInputManager().removeRawInputListener(this);
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
		AudioVisio.main(new String[]{ "-server" });

		ListBox listBox = this.nifty.getScreen("host").findNiftyControl("levelList", ListBox.class);
		Collection<Level> levels = LevelLoader.getLevelList().values();
		listBox.addAllItems(Arrays.asList(levels.toArray()));

		Element continueButton = this.nifty.getScreen("host").findElementByName("ContButton");
		continueButton.setVisible(false);
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

	public void initRSLE(){
		AudioVisio.main(new String[]{"-rsle"});
		app.stop();
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
	boolean kFlag;

	public void setKeyBinding(String butt){
		kFlag = true;
		btn = this.nifty.getScreen("keybindings").findNiftyControl(butt, Button.class);
		kFlag = false;
	}

	/**
	 * Switches to the ClientAppState from the current GuiAppState.
	 */
	public void clientInit(){
		TextField ipField = this.nifty.getScreen("join").findNiftyControl("input", TextField.class);
		NetworkUtils.setIp(ipField.getRealText());

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
	public void bind( Nifty arg0, Screen arg1 ){}

	/**
	 * Unimplemented method stub.
	 */
	@Override
	public void onStartScreen(){}

	/**
	 * Unimplemented method stub.
	 */
	@Override
	public void onEndScreen(){}

	/**
	 * Switches to the ClientAppState and ServerAppState from the current GuiAppState.
	 */
	public void clientAndServerInit(){
		this.app.stopGui();
		this.setEnabled(false);
		this.app.clientStart();
		NetworkUtils.attemptConnection(this.app.client.myClient);
	}




	@Override
	public void beginInput(){

	}

	@Override
	public void endInput(){

	}

	@Override
	public void onJoyAxisEvent( JoyAxisEvent joyAxisEvent ){

	}

	@Override
	public void onJoyButtonEvent( JoyButtonEvent joyButtonEvent ){

	}

	@Override
	public void onMouseMotionEvent( MouseMotionEvent mouseMotionEvent ){

	}

	@Override
	public void onMouseButtonEvent( MouseButtonEvent mouseButtonEvent ){

	}

	char keyChar;
	int keyCode;
	@Override
	public void onKeyEvent( KeyInputEvent keyInputEvent ){
		if(kFlag != true){
			keyChar = keyInputEvent.getKeyChar();
			keyCode = keyInputEvent.getKeyCode();
			if (keyInputEvent.isPressed()){
				btn.setText(Character.toString(keyChar));
				System.out.println(keyChar);
				return;
			}
			kFlag = true;
		}
	}

	@Override
	public void onTouchEvent( TouchEvent touchEvent ){

	}

	@NiftyEventSubscriber(id="levelList")
	public void onLevelListSelectionChanged(final String id, final ListBoxSelectionChangedEvent<Level> event){
		Level lvl = event.getSelection().get(0);

		LogHelper.info("Selected level: " + lvl.toString());

		Element continueButton = this.nifty.getScreen("host").findElementByName("ContButton");
		continueButton.setVisible(true);
	}
}
