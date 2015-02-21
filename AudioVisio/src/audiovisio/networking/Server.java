package audiovisio.networking;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import audiovisio.networking.utilities.GeneralUtilities;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;

public class Server extends SimpleApplication{
	
	private com.jme3.network.Server myServer;
	
	@Override
	public void simpleInitApp(){
		try{
			myServer = Network.createServer(GeneralUtilities.getPort());
			myServer.start();
		}
		catch(IOException e){
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@Override
	public void simpleUpdate(float tpf){
		
	}
	
	@Override
	public void destroy(){
		myServer.close();
		super.destroy();
	}
	
}