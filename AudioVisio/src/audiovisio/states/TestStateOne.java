package audiovisio.states;

import audiovisio.StateExperiment;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class TestStateOne extends AbstractAppState implements ScreenController {
	
	private Nifty nifty;
	StateExperiment experiment;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		experiment = (StateExperiment) app;
		System.out.println("Test State One");
		
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(),
				app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
		nifty = niftyDisplay.getNifty();
		nifty.fromXml("Interface/baselayer.xml", "start", this);
		
		app.getGuiViewPort().addProcessor(niftyDisplay);
		app.getInputManager().setCursorVisible(true);
	}

	@Override
	public void cleanup() {
		super.cleanup();
		System.out.println("Test State One Cleanup");
	}
	
	public void initHost() {
		experiment.switchState();
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