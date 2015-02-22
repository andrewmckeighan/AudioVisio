package audiovisio.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

public class LobbyState extends AbstractAppState{
	
	private SimpleApplication app;

	 @Override
	 public void initialize(AppStateManager stateManager, Application app){
		 super.initialize(stateManager, app);
		 this.app = (SimpleApplication)app;
	 }
	 
	 @Override
	 public void cleanup(){
		 //TODO
	 }
	
	 @Override
	 public void setEnabled(boolean enabled){
		 super.setEnabled(enabled);
		 if(enabled){
			 //TODO
		 }
		 else{
			 //TODO
		 }
	 }
	 
	 @Override
	 public void update(float tpf){
		 //TODO
	 }
}
