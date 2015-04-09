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
 * This is the box that renders around the entire level for the
 * visual player. The size of the box is dynamically calculated
 * by the level. It uses a special material that allows it to
 * render as a wireframe, rather than as a shaded surface. NOTE:
 * the player will be inside this box, so it should not be added
 * to any node that collision will be calculated on.
 *
 * @author Matt Gerst
 */
public class LevelBox {
    private Box box;
    private Material wireMaterial;
    private Geometry geometry;

    public LevelBox( AssetManager assetManager ){
        this.wireMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        this.wireMaterial.setColor("Color", ColorRGBA.White);
        this.wireMaterial.getAdditionalRenderState().setWireframe(true);
        this.wireMaterial.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);

        this.box = new Box();
        this.geometry = new Geometry("levelbox", this.box);
        this.geometry.setMaterial(this.wireMaterial);
    }

    /**
     * Set the size of the box by specifying two opposite corners
     *
     * @param min The min point
     * @param max The max point
     */
    public void setSize( Vector3f min, Vector3f max ){
        min.mult(Level.SCALE);
        this.box.updateGeometry(min, max);
    }

    /**
     * Attach the box to the specified root node.
     *
     * @param rootNode The node to attach the box to
     */
    public void start( Node rootNode ){
        rootNode.attachChild(this.geometry);
    }
}
