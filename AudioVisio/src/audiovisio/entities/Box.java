package audiovisio.entities;

import audiovisio.entities.particles.PlayerParticle;
import audiovisio.level.IShootable;
import audiovisio.level.Level;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.states.ClientAppState;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import audiovisio.utils.LogHelper;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import org.json.simple.JSONObject;

/**
 * Created by Tain on 4/6/2015.
 */
public class Box extends InteractableEntity implements IShootable {
    protected static ColorRGBA COLOR = ColorRGBA.Yellow;

    public Box(){}

    @Override
    public void init( AssetManager assetManager ){
        com.jme3.scene.shape.Box shape = Box.SHAPE;

        this.geometry = new Geometry(this.name + "testButtonName", shape);
//        this.geometry.setLocalRotation(Button.ROTATION);
        this.location = this.location.mult(Level.SCALE);
        this.location = this.location.add(Box.PLACE_OFFSET);
        this.geometry.setLocalTranslation(this.location);

        Material randomMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        randomMaterial.setColor("Color", Box.COLOR);
        this.geometry.setMaterial(randomMaterial);

        this.physics = new RigidBodyControl(Box.MASS);

        this.particle = new PlayerParticle();

        this.particle.init(assetManager);

        this.attachChild(this.geometry);
        this.attachChild(this.particle);
        this.addControl(this.physics);

        if (this.particle != null && this.particle.emitter != null){
//          this.footSteps.emitter.setLocalTranslation(this.getLocalTranslation());
            this.particle.emitter.setLocalTranslation(this.location);
            this.particle.emitter.setNumParticles(35);
            this.particle.emitter.setEnabled(true);
        }
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        this.rootNode = rootNode;
        this.physicsSpace = physics;
        if (ClientAppState.isAudio){
            this.rootNode.attachChild(this.particle);
            this.particle.start(rootNode, physics);

        } else {
            this.particle.removeFromParent();
            this.particle = null;
            this.rootNode.attachChild(this);
        }
        physics.add(this);

    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);
        codeObj.put(JSONHelper.KEY_TYPE, "box");
    }

    @Override
    public LevelNode getLevelNode(){
        LevelNode root = new LevelNode(String.format("#%d box @ %s", this.ID, this.location), true);
        LevelNode typeNode = new LevelNode("Type", "box", true);
        LevelNode idNode = new LevelNode("ID", this.ID, false);
        LevelNode locationNode = LevelUtils.vector2node(this.location);
        LevelNode nameNode = new LevelNode("Name", this.name, false);

        root.add(typeNode);
        root.add(idNode);
        root.add(nameNode);
        root.add(locationNode);

        if (!this.linkedIds.isEmpty()){
            LevelNode linkNode = LevelUtils.linksNode(this.linkedIds);
            root.add(linkNode);
        }

        return root;
    }

    @Override
    public void update( Boolean state ){
        if (!this.state){
            if (state){
                this.pickUp();
            }
        } else {
            if (!state){
                this.putDown(this.location);
            }
        }

        this.state = state;
        this.wasUpdated = false;
    }

    public void putDown( Vector3f location ){
        this.rootNode.attachChild(this);
        this.physicsSpace.add(this);

        this.location = location.add(Box.PLACE_OFFSET);

        this.setLocalTranslation(this.location);
    }

    public Box pickUp(){
        this.removeFromParent();
        this.physicsSpace.remove(this);
        return this;
    }

    @Override
    public void update(){
        LogHelper.fine("Box was shot");
    }

    @Override
    public Boolean getWasUpdated(){
        return this.wasUpdated;
    }

    @Override
    public void setWasUpdated( boolean wasUpdated ){
        this.wasUpdated = wasUpdated;
    }

    @Override
    public Geometry getGeometry(){
        return this.geometry;
    }

    private static final Vector3f                 PLACE_OFFSET = new Vector3f(0.0F, 1.0F, 0.0F);
    private static final com.jme3.scene.shape.Box SHAPE        = new com.jme3.scene.shape.Box(0.4F * Level.SCALE.getX(),
            0.4F * Level.SCALE.getX(),
            0.4F * Level.SCALE.getX());
    private static final float                    MASS         = 8.0F;
}
