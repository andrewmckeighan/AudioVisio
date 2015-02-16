/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package audio;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * Sample 11 - playing 3D audio.
 */
public class AudioTest extends SimpleApplication {
    
    private AudioNode audio_gun;
    private AudioNode audio_nature;
    private Geometry player;
    
    public static void main(String[] args) {
        AudioTest app = new AudioTest();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(40);
        
        /** blue box floating in space */
        Box box1 = new Box(1, 1, 1);
        player = new Geometry("Player", box1);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        player.setMaterial(mat1);
        rootNode.attachChild(player);
        
        initKeys();
        initAudio();
    }
    
    private void initAudio() {
        audio_gun = new AudioNode(assetManager, "Sounds/Effects/Gun.wav", false);
        audio_gun.setPositional(false);
        audio_gun.setLooping(false);
        audio_gun.setVolume(2);
        rootNode.attachChild(audio_gun);
        
        /* nature sound */
        audio_nature = new AudioNode(assetManager, "Sounds/Environment/Ocean Waves.ogg", true);
        audio_nature.setLooping(true);
        audio_nature.setPositional(true);
        audio_nature.setLocalTranslation(Vector3f.ZERO.clone());
        audio_nature.setVolume(3);
        rootNode.attachChild(audio_nature);
        audio_nature.play();
    }
    
    private void initKeys() {
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "Shoot");
    }
    
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Shoot") && !keyPressed) {
                audio_gun.playInstance();
            }
        }
    };
    
    @Override
    public void simpleUpdate(float tpf) {
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
    }
}
