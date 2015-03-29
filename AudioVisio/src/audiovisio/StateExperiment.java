package audiovisio;

import audiovisio.states.GuiAppState3;
import audiovisio.states.GuiAppState2;
import audiovisio.states.TestStateOne;
import audiovisio.states.TestStateTwo;

import com.jme3.app.SimpleApplication;

public class StateExperiment extends SimpleApplication {
	GuiAppState2 testOne;
	TestStateTwo testTwo;
	boolean flasg = true;
	
	public static void main(String[] args) {
		StateExperiment experiment = new StateExperiment();
		experiment.setPauseOnLostFocus(false);
		experiment.start();
	}

	@Override
	public void simpleInitApp() {
		testOne = new GuiAppState2();
		testTwo = new TestStateTwo();
		
		stateManager.attach(testOne);
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
	}
	
	public void switchState() {
		if (flasg) {
			stateManager.detach(testOne);
			stateManager.attach(testTwo);
			flasg = false;
		} else {
			stateManager.detach(testTwo);
			stateManager.attach(testOne);
			flasg = true;
		}
	}
}
