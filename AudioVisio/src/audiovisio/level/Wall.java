package audiovisio.level;

import audiovisio.rsle.editor.LevelNode;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import org.json.simple.JSONObject;

/**
 */
public class Wall extends Panel {
    public static final  String     KEY_EDGE = "edge";
    private static final Box        SHAPE    = new Box(0.01F * Level.SCALE.getX(),
            0.5F * Level.SCALE.getY(),
            0.5F * Level.SCALE.getZ());
    private static final Quaternion ROTATION = new Quaternion().fromAngles(0, (float) Math.PI / 2, 0);
    private static final Vector3f   OFFSET   = new Vector3f(0.0F, Level.SCALE.getY() / 2.0F, 0.0F);
    private static final ColorRGBA  COLOR    = ColorRGBA.LightGray;
    private Direction direction;

    public Wall(){}

    public Wall( Vector3f location, Direction direction ){
        super(location);
        this.direction = direction;
    }

    @Override
    public void load( JSONObject loadObj ){
        super.load(loadObj);

        this.direction = Direction.valueOf((String) loadObj.get(Wall.KEY_EDGE));
    }

    @Override
    public void init( AssetManager assetManager ){
        Box shape = Wall.SHAPE;

        this.geometry = new Geometry("Wall" + this.ID, shape);

        if (this.direction == Direction.NORTH){
            this.location = this.location.add(0.5F, 0.0F, 0.0F);
        } else if (this.direction == Direction.SOUTH){
            this.location = this.location.add(-0.5F, 0.0F, 0.0F);
        } else if (this.direction == Direction.EAST){
            this.location = this.location.add(0.0F, 0.0F, -0.5F);
            this.geometry.setLocalRotation(Wall.ROTATION);//Probably in Radians
        } else if (this.direction == Direction.WEST){
            this.location = this.location.add(0.0F, 0.0F, -0.5F);
            this.geometry.setLocalRotation(Wall.ROTATION);//Probably in Radians
        }
        this.location = this.location.mult(Level.SCALE);
        this.location = this.location.add(Wall.OFFSET);
        this.geometry.setLocalTranslation(this.location);

        Material randomMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        randomMaterial.setColor("Color", Wall.COLOR);
//        this.material = randomMaterial;
        this.geometry.setMaterial(randomMaterial);

        this.physics = new RigidBodyControl(0);
        this.geometry.addControl(this.physics);
    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);

        codeObj.put(JSONHelper.KEY_TYPE, "wall");
        codeObj.put(Wall.KEY_EDGE, this.direction.toString());
    }

    @Override
    public LevelNode getLevelNode(){
        LevelNode root = new LevelNode(String.format("#%d wall @ %s", this.ID, this.location), true);
        LevelNode typeNode = new LevelNode("Type", "wall", true);
        LevelNode idNode = new LevelNode("ID", this.ID, false);
        LevelNode dirNode = new LevelNode("Direction", this.direction, false);
        LevelNode location = LevelUtils.vector2node(this.location);

        root.add(typeNode);
        root.add(idNode);
        root.add(dirNode);
        root.add(location);

        return root;
    }

}
