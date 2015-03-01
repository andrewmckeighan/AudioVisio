package audiovisio.networking;

import java.io.IOException;
import java.util.Map;
import audiovisio.entities.Button;
import audiovisio.entities.Lever;
import audiovisio.entities.Player;
import audiovisio.networking.listeners.ServerPlayerDirectionMessageListener;
import audiovisio.networking.utilities.GeneralUtilities;
import audiovisio.utils.LogHelper;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Server extends SimpleApplication implements PhysicsCollisionListener, ActionListener{
	private com.jme3.network.Server myServer;

	public Server(){

	}

	private Map<Integer, Player> players;
	private Node shootables;
	private Geometry mark;
	private Spatial sceneModel;
	private BulletAppState bulletAppState;
	private RigidBodyControl landscape;
	private RigidBodyControl button;
	private Player audioPlayer;
	private Player visualPlayer;
	private Vector3f oldAudioLocation;
	private Vector3f oldVisualLocation;
	private Vector3f newAudioLocation = new Vector3f();
	private Vector3f newVisualLocation = new Vector3f();
	private long oldTime;
	private long newTime;
	private long time;
	private int counter = 0;
	private	float audioVelocity = 0;
	private	float visualVelocity = 0;
	private	float audioDistance = 0;
	private	float visualDistance = 0;
	private	Vector3f audioPosition = new Vector3f();
	private	Vector3f visualPosition = new Vector3f();
	private ServerPlayerDirectionMessageListener pDML= new ServerPlayerDirectionMessageListener(this);

	@Override
	public void simpleInitApp(){
		try{
			myServer = Network.createServer(GeneralUtilities.getPort());
			myServer.start();
			myServer.addConnectionListener(new ConnectionListener() {

				@Override
				public void connectionAdded(com.jme3.network.Server server,
						HostedConnection conn) {
					if (players.size() < 2) {
						players.put(conn.getId(), new Player());
					} else {
						// TODO: Throw an error of some kind
						LogHelper.severe("More than 2 players attempted to join");
					}
				}

				@Override
				public void connectionRemoved(com.jme3.network.Server server,
						HostedConnection conn) {
					players.remove(conn.getId());
				}
			});
		}
		catch(IOException e){
			LogHelper.severe("Error on server start", e);
			System.exit(1);
		}

		GeneralUtilities.initializeSerializables();

		PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();

		// /////////////////////
		// Load Scene (map) //
		// /////////////////////
		assetManager.registerLocator("town.zip", ZipLocator.class);
		sceneModel = assetManager.loadModel("main.scene");
		sceneModel.setLocalScale(2f);

		// /////////////
		// Physics //
		// /////////////
		CollisionShape sceneShape = CollisionShapeFactory
				.createMeshShape((Node) sceneModel);
		landscape = new RigidBodyControl(sceneShape, 0);
		sceneModel.setLocalScale(2f);

		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		/////////////////////////
		// Generate entities //
		/////////////////////////
		Button testButton = new Button(0f, 1f, 0f);

		Lever testLever = new Lever(3f, 5f, 3f);
		shootables.attachChild(testLever.geometry);

		setAudioPlayer(new Player(audioPlayer));
		setVisualPlayer(new Player(visualPlayer));

		///////////////////////////
		//Add entities to Scene //
		///////////////////////////
		getAudioPlayer().addToScene(rootNode, physicsSpace);
		getVisualPlayer().addToScene(rootNode, physicsSpace);

		testButton.addToScene(rootNode, physicsSpace);
		testLever.addToScene(rootNode, physicsSpace);

		// ////////////////////////////
		// Add objects to rootNode //
		// ////////////////////////////
		rootNode.attachChild(sceneModel);

		// /////////////////////////////////
		// Add objects to physicsSpace //
		// /////////////////////////////////
		physicsSpace.addCollisionListener(this);
		physicsSpace.add(landscape);

	}

	@Override
	public void simpleUpdate(float tpf){
		
	}

	@Override
	public void destroy(){
		myServer.close();
		super.destroy();
	}

	public Player getAudioPlayer() {
		return audioPlayer;
	}

	public Player getVisualPlayer() {
		return visualPlayer;
	}

	public void setAudioPlayer(Player audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	public void setVisualPlayer(Player visualPlayer) {
		this.visualPlayer = visualPlayer;
	}

	@Override
	public void onAction(String arg0, boolean arg1, float arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void collision(PhysicsCollisionEvent arg0) {
		// TODO Auto-generated method stub

	}

}

