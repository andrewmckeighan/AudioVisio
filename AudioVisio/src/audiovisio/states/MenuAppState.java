package audiovisio.states;

import audiovisio.gui.GuiOperation;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

public class MenuAppState extends AbstractAppState{

	public GuiOperation app = new GuiOperation();

	 @Override
	 public void initialize(AppStateManager stateManager, Application app){
		 super.initialize(stateManager, app);
		 this.app = (GuiOperation)app;
	 }
	 
	 @Override
	 public void cleanup(){
		 //TODO
	 }
	
	 @Override
	 public void setEnabled(boolean enabled){
		 super.setEnabled(enabled);
		 if(enabled){
			 app.start();
		 }
		 else{
			 app.quitGui();
		 }
	 }
	 
	 @Override
	 public void update(float tpf){
		 //TODO
	 }
}