package Server;
//imports for jme3 server apps
import com.jme3.app.SimpleApplication;
import com.jme3.network.Server;
import com.jme3.network.Network;
import com.jme3.system.JmeContext;
//import for catching port exceptions
import java.io.IOException;
//import for server logs
import java.util.logging.level;
import java.util.logging.logger;
//import for creating geometric objects
import com.jme3.scene.Geometry

//may throw an error if the port is being used

public class ServerMain extends SimpleApplication{
	//server instance variable
	private Server myServer;
	
	public static void main (String[] args){
		//intializes serializable variables from networkutils class
		NetworkUtils.initializeSerializables();
		//initiates a new server app with a headless type (no server messaging?)
		ServerMain app = new ServerMain();
		app.start(JmeContext.Type.Headless);
	}
	
	//initializes the server, throws exceptions (for ?)
	@Override
	public void simpleInitApp(){
		try{
			myServer = Network.createServer(NetworkUtils.PORT);
			myServer.start();
		}
		catch(IOException e){
			//submits the catch to server log
			Logger.getLogger(ServerMain.class.getName().log(Level.SEVERE, null, e));
		}
		//server creates a geometric object (a box)
		Geometry geometric = new CreateGeometrics(this).createBox();
		rootNode.attachChild(geometric);
	}
	
	@Override
	public void simpleUpdate(float tpf){
		server.broadcast(new NetworkMessage("Hello World! server time is: " + tpf));
	}
	
	// must override destroy method from super
	@override
	public void destroy(){
		myServer.close();
		super.destroy();
	}
		
}
