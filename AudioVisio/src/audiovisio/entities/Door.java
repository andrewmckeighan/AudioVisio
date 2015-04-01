package audiovisio.entities;

import audiovisio.level.Level;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.states.ClientAppState;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import org.json.simple.JSONObject;

public class Door extends InteractableEntity {
    public static final  String     KEY_EDGE  = "edge";
    public static final  String     KEY_STATE = "state";
    private static final Quaternion ROTATION  = new Quaternion().fromAngles(0, (float) Math.PI / 2, 0);
    private static final Box        SHAPE     = new Box(0.05F * Level.SCALE.getX(),
            0.5F * Level.SCALE.getY(),
            0.5F * Level.SCALE.getZ());
    private static final float      MASS      = 0.0F;
    private static final Vector3f   OFFSET    = new Vector3f(0.0F, Level.SCALE.getY() / 2.0F, 0.0F);
    public Particle particle;
    private Direction direction = Direction.NORTH;

    public Door(){
        this.state = false;
    }

    public Door( Boolean open ){
        this.state = open;
    }

    public Door( Boolean open, Boolean stuck ){
        this.state = open;
        this.stuck = stuck;
    }

    @Override
    public void load( JSONObject loadObj ){
        super.load(loadObj);

        this.state = (Boolean) loadObj.get(Door.KEY_STATE);
        this.direction = Direction.valueOf((String) loadObj.get(Door.KEY_EDGE));
    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);
        codeObj.put(JSONHelper.KEY_TYPE, "door");

        codeObj.put(Door.KEY_STATE, this.state);
        codeObj.put(Door.KEY_EDGE, this.direction.toString());
    }

    @Override
    public void update( Boolean state ){
        if (!this.state){
            if (state){
                this.open();
            }
        } else {
            if (!state){
                this.close();
            }
        }

        this.state = state;
    }

    private void open(){
        assert this.state;
        if (ClientAppState.isAudio){
            this.playSound();
            this.emitParticle();
        } else {
            this.updateVisuals();
        }
    }

    private void updateVisuals(){
        if (this.state){
            this.rootNode.detachChild(this);
            this.physicsSpace.remove(this);
        } else {
            this.rootNode.attachChild(this);
            this.physicsSpace.add(this);
        }
    }

    private void emitParticle(){
        //TODO
    }

    private void playSound(){
        //TODO
    }

    private void close(){
        //TODO
        assert !this.state;
        if (ClientAppState.isAudio){
            this.playSound();
            this.emitParticle();
        } else {
            this.updateVisuals();
        }
    }

    @Override
    public void init( AssetManager assetManager ){
        Box shape = Door.SHAPE;

        this.geometry = new Geometry(this.name, shape);

        if (this.direction == Direction.NORTH){
            this.location = this.location.add(0.5F, 0.0F, 0.0F);
        } else if (this.direction == Direction.SOUTH){
            this.location = this.location.add(-0.5F, 0.0F, 0.0F);
        } else if (this.direction == Direction.EAST){
            this.location = this.location.add(0.0F, 0.0F, 0.5F);
            this.geometry.setLocalRotation(Door.ROTATION);//Probably in Radians
        } else if (this.direction == Direction.WEST){
            this.location = this.location.add(0.0F, 0.0F, -0.5F);
            this.geometry.setLocalRotation(Door.ROTATION);//Probably in Radians
        }
        this.location = this.location.mult(Level.SCALE);
        this.location = this.location.add(Door.OFFSET);
        this.geometry.setLocalTranslation(this.location);

        this.particle = new Particle();

        this.particle.init(assetManager);

        if (this.particle != null && this.particle.emitter != null){
//            this.footSteps.emitter.setLocalTranslation(this.getLocalTranslation());
            this.particle.emitter.setLocalTranslation(this.location);
            this.particle.emitter.setNumParticles(25);
        }

        com.jme3.material.Material randomMaterial = new com.jme3.material.Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        randomMaterial.setColor("Color", ColorRGBA.randomColor());
        this.material = randomMaterial;
        this.geometry.setMaterial(randomMaterial);

        this.physics = new RigidBodyControl(Door.MASS);//TODO: this might not be needed if we don't want collision detection
        this.attachChild(this.geometry);
        this.addControl(this.physics);
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        this.rootNode = rootNode;
        this.physicsSpace = physics;
        if (!ClientAppState.isAudio){
            rootNode.attachChild(this);
        }
        physics.add(this);

        this.particle.start(rootNode, physics);
    }

    @Override
    public LevelNode getLevelNode(){
        LevelNode root = new LevelNode(String.format("#%d door @ %s", this.ID, this.location), true);
        LevelNode typeNode = new LevelNode("Type", "door", true);
        LevelNode idNode = new LevelNode("ID", this.ID, false);
        LevelNode nameNode = new LevelNode("Name", this.name, false);
        LevelNode stateNode = new LevelNode("State", this.state, false);
        LevelNode locationNode = LevelUtils.vector2node(this.location);

        root.add(typeNode);
        root.add(idNode);
        root.add(nameNode);
        root.add(stateNode);
        root.add(locationNode);

        return root;
    }


}