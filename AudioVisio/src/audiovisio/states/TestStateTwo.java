package audiovisio.states;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

public class TestStateTwo extends AbstractAppState {

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		System.out.println("Test State Two");
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		System.out.println("Test State Two Cleanup");
	}
}