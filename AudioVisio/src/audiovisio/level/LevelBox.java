package audiovisio.level;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * @author Matt Gerst
 */
public class LevelBox {
    private Box box;
    private Material wireMaterial;
    private Geometry geometry;

    public LevelBox(AssetManager assetManager){
        wireMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wireMaterial.setColor("Color", ColorRGBA.White);
        wireMaterial.getAdditionalRenderState().setWireframe(true);
        wireMaterial.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);

        box = new Box();
        geometry = new Geometry("levelbox", box);
        geometry.setMaterial(wireMaterial);
    }

    public void setSize(Vector3f min, Vector3f max) {
        min.mult(Level.SCALE);
        box.updateGeometry(min, max);
    }

    public void start( Node rootNode ){
        rootNode.attachChild(geometry);
    }
}
