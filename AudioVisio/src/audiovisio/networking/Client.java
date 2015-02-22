package audiovisio.networking;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import audiovisio.networking.utilities.GeneralUtilities;
import audiovisio.utils.LogHelper;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Network;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

import audiovisio.entities.*;
import audiovisio.level.*;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

public class Client extends SimpleApplication {

	private com.jme3.network.Client myClient;

	public ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();

	public Client() {
		// super(new StatsAppState(), new FlyCamAppState(), new
		// DebugKeysAppState());
	}

	private Node shootables;
	private Geometry mark;

	public void simpleInitApp(String IP) {

		try {
			myClient = Network.connectToServer(IP, GeneralUtilities.getPort());
			myClient.start();
		} catch (IOException e) {
			LogHelper.severe("Error on client start", e);
			System.exit(1);
		}

		// /////////////////////////////////////////

		BulletAppState bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
		flyCam.setMoveSpeed(100);
		audiovisio.level.Level level = new audiovisio.level.Level("testLevel",
				"author", "0.0.0");

		Panel testPanel = new Panel();
		Button testButton = new Button();
		Player testPlayer = new Player();

		Material pondMat = new Material(assetManager,
				"Common/MatDefs/Light/Lighting.j3md"); // load the material &
														// color
		pondMat.setTexture("DiffuseMap",
				assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));// located
																			// in
																			// jME3-testdata.jar
		pondMat.setTexture("NormalMap", assetManager
				.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
		pondMat.setBoolean("UseMaterialColors", true);
		pondMat.setColor("Diffuse", ColorRGBA.White); // minimum material color
		pondMat.setColor("Specular", ColorRGBA.White); // for shininess
		pondMat.setFloat("Shininess", 64f); // [1,128] for shininess

		testButton.geometry.setMaterial(pondMat);

		testPlayer.initKeys(inputManager);
		testPlayer.makeCharacter(assetManager);

		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(al);

		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
		rootNode.addLight(dl);

		rootNode.attachChild(testPlayer.model);
		rootNode.attachChild(testButton.geometry);

		level.getEntities().add(testButton);
		level.getPanels().add(testPanel);

	}

	public void simpleInitApp() {
		simpleInitApp("127.0.0.1");
	}

	@Override
	public void simpleUpdate(float tpf) {

		String message = messageQueue.poll();
		if (message != null) {
			fpsText.setText(message);
		} else {
			fpsText.setText("No message in queue.");
		}

	}

	@Override
	public void destroy() {
		myClient.close();
		super.destroy();
	}

}