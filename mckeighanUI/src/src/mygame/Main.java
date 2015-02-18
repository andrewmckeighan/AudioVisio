package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
* test
* @author normenhansen
*/
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    protected Geometry player;
    Boolean isRunning = true;

    @Override
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1);
        player = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        player.setMaterial(mat);

        rootNode.attachChild(player);
        initKeys();
    }

    private void initKeys() {
        inputManager.addMapping("Pause", new KeyTrigger(keyInput.KEY_P));

        inputManager.addListener(actionListener, "Pause");
    }

    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if(name.equals("Pause") && !keyPressed){
                isRunning = !isRunning;
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        if(isRunning){
            player.rotate(3*tpf,2*tpf, 1*tpf);
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
//TODO: add render code
    }
}
