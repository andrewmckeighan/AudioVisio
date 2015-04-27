package audiovisio.entities;

import audiovisio.entities.particles.Particle;
import audiovisio.entities.particles.PlayerParticle;
import audiovisio.level.IShootable;
import audiovisio.level.Level;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.states.ClientAppState;
import audiovisio.utils.JSONHelper;
import audiovisio.utils.LevelUtils;
import audiovisio.utils.LogHelper;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
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
    private static final Vector3f                 PLACE_OFFSET = new Vector3f(0.0F, 0.2F * Level.SCALE.getX(), 0.0F);
    private static final com.jme3.scene.shape.Box SHAPE        = new com.jme3.scene.shape.Box(0.2F * Level.SCALE.getX(),
            0.2F * Level.SCALE.getX(),
            0.2F * Level.SCALE.getX());
    private static final float                    MASS         = 500.0F;
    protected static ColorRGBA COLOR = ColorRGBA.Yellow;
    private Node shootables;
    private AudioNode audio_place;
    private AudioNode audio_pick;
    public Box(){}
    public Particle particle;

    @Override
    public void init(AssetManager assetManager) {
        com.jme3.scene.shape.Box shape = Box.SHAPE;

        this.geometry = new Geometry(this.name + "testButtonName", shape);

        this.location = this.location.mult(Level.SCALE);
        this.location = this.location.add(Box.PLACE_OFFSET);
        this.setLocalTranslation(this.location);

        Material randomMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        randomMaterial.setColor("Color", this.color);
        this.geometry.setMaterial(randomMaterial);

        this.physics = new RigidBodyControl(Box.MASS);

        this.particle = new Particle();
        this.particle.init(assetManager);
        this.particle.setParticleColor(this.color);

        this.particle.init(assetManager);


        this.attachChild(this.geometry);
        this.attachChild(this.particle);
        this.addControl(this.physics);
//        this.geometry.addControl(this.physics);


        if (this.particle != null && this.particle.emitter != null) {
//          this.footSteps.emitter.setLocalTranslation(this.getLocalTranslation());
            this.particle.emitter.setLocalTranslation(this.location);
            this.particle.emitter.setNumParticles(35);
            this.particle.emitter.setEnabled(true);
        }
        this.audio_pick = new AudioNode(assetManager, "Sounds/Effects/pickupBox.wav", false);
        this.attachChild(this.audio_pick);
        this.audio_pick.setLooping(false);
        this.audio_pick.setPositional(true);

        this.audio_place = new AudioNode(assetManager, "Sounds/Effects/placebox.wav", false);
        this.attachChild(this.audio_place);
        this.audio_place.setLooping(false);
        this.audio_place.setPositional(true);


    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        this.physics.setLinearVelocity(new Vector3f(0, -900.81f, 0));
        this.physics.setKinematic(true);

        this.rootNode = rootNode;
        this.physicsSpace = physics;
        if (ClientAppState.isAudio) {
            this.rootNode.attachChild(this.particle);
            this.particle.start(rootNode, physics);


        } else {
            this.particle.removeFromParent();
            this.particle = null;
//            rootNode.attachChild(this);
        }
        physics.add(this.physics);

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
        LevelNode nameNode = new LevelNode("Name", this.name, false);
        LevelNode colorNode = new LevelNode("Color", JSONHelper.writeColor(this.color), false);
        LevelNode locationNode = LevelUtils.vector2node(this.location);

        root.add(typeNode);
        root.add(idNode);
        root.add(nameNode);
        root.add(colorNode);
        root.add(locationNode);

        if (!this.linkedIds.isEmpty()){
            LevelNode linkNode = LevelUtils.linksNode(this.linkedIds);
            root.add(linkNode);
        }

        root.setSourceItem(this);

        return root;
    }

    @Override
    public void update( Boolean state ){
        if (!this.state){
            if (state){

               if( ClientAppState.isAudio){
                this.audio_pick.play();
               }
               this.pickUp();
            }
        } else {
            if (!state){
                if (ClientAppState.isAudio) {
                    this.audio_place.play();
                }
                this.putDown(this.shootables, this.location);
            }
        }

        this.state = state;
        this.wasUpdated = false;
    }

    public void putDown( Node shootablesNode, Vector3f location ){
        LogHelper.info("putDown");
        this.shootables = shootablesNode;

//        shootablesNode.attachChild(this);
        this.attachChild(this.geometry);
        this.physicsSpace.add(this);

        this.location = location.subtract(0.0f, Level.SCALE.getY(), 0.0f);
        this.location = this.location.add(Box.PLACE_OFFSET);

//        this.physics.setPhysicsLocation(this.location);
        this.setLocalTranslation(this.location);

        toggleParticle();

    }

    public Box pickUp(){
        LogHelper.info("pickUP");
//        this.removeFromParent();
        this.geometry.removeFromParent();
        this.physicsSpace.remove(this);
        toggleParticle();
        return this;
    }

    @Override
    public void update(){
        LogHelper.fine("Box was shot");
        this.wasUpdated = false;
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

    private void toggleParticle(){
        if (this.particle.emitter.isEnabled()){
            this.particle.emitter.setEnabled(false);
            this.rootNode.detachChild(this.particle);
        } else {
            this.rootNode.attachChild(this.particle);
            this.particle.emitter.setEnabled(true);
        }
    }
}
