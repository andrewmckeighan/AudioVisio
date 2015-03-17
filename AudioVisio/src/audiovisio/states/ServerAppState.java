package audiovisio.states;

import audiovisio.networking.Server;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

public class ServerAppState extends AbstractAppState{

	public Server app = new Server();

	 @Override
	 public void initialize(AppStateManager stateManager, Application app){
		 super.initialize(stateManager, app);
		 this.app = (Server)app;
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
			 app.destroy();
		 }
	 }
	 
	 @Override
	 public void update(float tpf){
		 //TODO
	 }
}