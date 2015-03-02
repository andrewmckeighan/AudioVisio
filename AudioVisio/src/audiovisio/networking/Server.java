package audiovisio.networking;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import audiovisio.entities.Button;
import audiovisio.entities.Entity;
import audiovisio.entities.Lever;
import audiovisio.entities.Player;
import audiovisio.networking.listeners.ServerMessageListener;
import audiovisio.networking.messages.PlayerJoinMessage;
import audiovisio.networking.messages.PlayerLeaveMessage;
import audiovisio.networking.messages.PlayerListMessage;
import audiovisio.networking.messages.PlayerLocationMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;
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
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Server extends SimpleApplication implements PhysicsCollisionListener, ActionListener{
	private com.jme3.network.Server myServer;

	private Map<Integer, Player> players = new HashMap<Integer, Player>();
	private Spatial sceneModel;
	private BulletAppState bulletAppState;
	private RigidBodyControl landscape;
	//private Player audioPlayer;
	//private Player visualPlayer;
	private ServerMessageListener messageListener = new ServerMessageListener(this);

	/**
	 * Default constructor
	 */
	public Server(){

	}

	/**
	 * Initializes game App on server startup
	 */
	@Override
	public void simpleInitApp(){
		GeneralUtilities.initializeSerializables();

		try{
			myServer = Network.createServer(GeneralUtilities.getPort());
			myServer.start();
			
			
		}
		catch(IOException e){
			LogHelper.severe("Error on server start", e);
			System.exit(1);
		}

		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		final PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();


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

		///////////////////////////
		//Add entities to Scene //
		///////////////////////////

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
		
		myServer.addConnectionListener(new ConnectionListener() {

			/**
			 * Handles adding connections to Server
			 */
			@Override
			public void connectionAdded(com.jme3.network.Server server,
					HostedConnection conn) {
				// DON'T REMOVE THIS LOG MESSAGE. IT BREAKS STUFF
				LogHelper.info("connectionAdded() for connection: " + conn.getId());
				if (players.size() < 2) {
					server.broadcast(new PlayerJoinMessage(conn.getId()));
					LogHelper.info("Sent PlayerJoinMessage");
					Player p = new Player();
					p.addToScene(rootNode, physicsSpace);
					players.put(conn.getId(), p);

					Integer[] list = players.keySet().toArray(new Integer[players.keySet().size()]);
					conn.send(new PlayerListMessage(list));
					LogHelper.info("Sent PlayerListMessage");
				} else {
					conn.close("Too many clients connect to server");
					LogHelper.severe("More than 2 players attempted to join");
				}
			}

			/**
			 * Handles Removing connections from server
			 */
			@Override
			public void connectionRemoved(com.jme3.network.Server server,
					HostedConnection conn) {
				if (players.containsKey(conn.getId()))
					players.remove(conn.getId());
				server.broadcast(Filters.notEqualTo(conn), new PlayerLeaveMessage(conn.getId()));
			}
		});
		myServer.addMessageListener(messageListener);

	}

	/**
	 * Handles App updates on server to client
	 */
	@Override
	public void simpleUpdate(float tpf){
		for (Entry<Integer, Player> entry : this.players.entrySet()) {
			PlayerLocationMessage message = entry.getValue().getLocationMessage(entry.getKey());
			
			myServer.broadcast(message);
			LogHelper.info("Server is sending message: [" + message + "] to, client [" + entry.getKey() + "]");
		}
	}
	


	/**
	 * Override default server destruction method
	 */
	@Override
	public void destroy(){
		myServer.close();
		super.destroy();
	}

	/**
	 * Get a player by the connection ID
	 * @param id The connection ID
	 */
	public Player getPlayer(int id) {
		return players.get(id);
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		if (event.getNodeA().getParent() instanceof Entity && event.getNodeB().getParent() instanceof Entity) {
			Entity entityA = (Entity) event.getNodeA().getParent();
			Entity entityB = (Entity) event.getNodeB().getParent();
			entityA.collisionTrigger(entityB);
			entityB.collisionTrigger(entityA);
			if ("button".equals(event.getNodeA().getName())) {

				if ("Oto-ogremesh".equals(event.getNodeB().getName())) {
					Button b = (Button) event.getNodeA().getParent();
					b.startPress();
				}
			}
			if ("button".equals(event.getNodeB().getName())) {

				if ("Oto-ogremesh".equals(event.getNodeA().getName())) {
					Button b = (Button) event.getNodeB().getParent();
					b.startPress();
				}
			}
		}
	}

	@Override
	public void onAction(String arg0, boolean arg1, float arg2) {
		// TODO Auto-generated method stub

	}

}

