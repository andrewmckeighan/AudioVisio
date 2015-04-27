package audiovisio.entities;

import audiovisio.entities.particles.ButtonParticle;
import audiovisio.entities.particles.Particle;
import audiovisio.level.IShootable;
import audiovisio.level.Level;
import audiovisio.networking.messages.TriggerActionMessage;
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
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import org.json.simple.JSONObject;

/**
 * //TODO this needs to be easy to walk over! (maybe no physical collision?)
 */
public class Button extends InteractableEntity implements IShootable {

    private static final Cylinder   SHAPE    = new Cylinder(8, 8, 0.4F * Level.SCALE.getX(), 0.03F * Level.SCALE.getY(), true);
    private static final Quaternion ROTATION = new Quaternion().fromAngles((float) Math.PI / 2, 0, 0);
    private static final float      MASS     = 0.0f;
    public  Particle  particle;
    private Node      rootNode;
    private AudioNode button_sound;

    public Button() {
    }

    @Override
    public void init(AssetManager assetManager) {
        Cylinder shape = Button.SHAPE;

        this.geometry = new Geometry(this.name + "testButtonName", shape);
        this.geometry.setLocalRotation(Button.ROTATION);
        this.location = this.location.mult(Level.SCALE);
        this.geometry.setLocalTranslation(this.location);

        Material material = new Material(assetManager,
                this.materialString);
        material.setColor("Color", Button.COLOR);
        this.geometry.setMaterial(material);

        this.physics = new RigidBodyControl(Button.MASS);//TODO: this might not be needed if we don't want collision detection

        this.particle = new ButtonParticle();
        this.particle.init(assetManager);
        this.particle.setParticleColor(this.color);

        this.attachChild(this.geometry);
        this.attachChild(this.particle);
        this.addControl(this.physics);

        String wavString = "Sounds/Effects/Click.wav";
        LogHelper.info(wavString);

        this.button_sound = new AudioNode(assetManager, wavString, false);
        this.attachChild(this.button_sound);
        this.button_sound.setLooping(false);
        this.button_sound.setPositional(true);
        this.button_sound.setVolume(.1f);

        if (this.particle != null && this.particle.emitter != null) {
//          this.footSteps.emitter.setLocalTranslation(this.getLocalTranslation());
            this.particle.emitter.setLocalTranslation(this.location);
            this.particle.emitter.setNumParticles(35);
            this.particle.emitter.setEnabled(true);
        }
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        this.rootNode = rootNode;
        if (ClientAppState.isAudio){
            this.rootNode.attachChild(this.particle);
            this.particle.start(rootNode, physics);

        } else {
            this.particle.removeFromParent();
            this.particle = null;
//            this.rootNode.attachChild(this);
        }
        physics.add(this);

    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);
        codeObj.put(JSONHelper.KEY_TYPE, "button");
    }

    @Override
    public LevelNode getLevelNode(){
        LevelNode root = new LevelNode(String.format("#%d button @ %s", this.ID, this.location), true);
        LevelNode typeNode = new LevelNode("Type", "button", true);
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

        root.setSourceItem(this);

        return root;
    }

    @Override
    public void update( Boolean state ){
        //this.button_sound.play();

        if (!this.state){
            if (state){
                this.startPress();
            }
        } else {
            if (!state){
                this.stopPress();
            }
        }

        this.state = state;
        this.wasUpdated = false;
    }

    @Override
    public void update(){
        LogHelper.fine("button was shot");
    }

    /**
     * Toggle the particle effects for the button on and off.
     * Expects to only be run on the Audio client.
     */
    private void toggleParticle(){
        if (this.particle.emitter.isEnabled()){
            this.particle.emitter.setEnabled(false);
            this.rootNode.detachChild(this.particle);
        } else {
            this.rootNode.attachChild(this.particle);
            this.particle.emitter.setEnabled(true);
        }
    }

    private void playSound(){
        if( ClientAppState.isAudio){
            this.button_sound.play();}
    }

    private void updateVisuals(){
        try{
            if (this.geometry.getMaterial() != null){
                this.geometry.getMaterial().setColor("updatedColor", ColorRGBA.randomColor());
            }
        } catch (IllegalArgumentException argumentException){
            // LogHelper.warn("Material not defined", argumentException);
        }
    }

    /**
     * Checks the entity against all classes that are valid ways to collide with this entity.
     *
     * @param entity entity to be checked
     *
     * @return if entity is something that can cause an update via collision
     */
    private Boolean isTriggeredBy( Entity entity ){
        Boolean rBoolean = false;
        if (entity instanceof Player){
            rBoolean = true;
        }
        if (entity instanceof MovingEntity){
            rBoolean = true;
        }
        if (entity instanceof Box){
            rBoolean = true;
        }
        //add other entities here (eg ball/box/etc..)
        return rBoolean;
    }

    public void startPress(){
        //particles for startPress
        //change color of button to something
        assert this.state;
        if (ClientAppState.isAudio){
            this.playSound();
            this.toggleParticle();
        } else {
            this.updateVisuals();
        }
    }

    public void stopPress(){
        //particles for stopPress
        //change the color again.
        assert !this.state;

        if (ClientAppState.isAudio){
            this.playSound();
            this.toggleParticle();
        } else {
            this.updateVisuals();
        }
    }

    /**
     * method called from client onCollision even handler.
     * handles actions needed if these two entity types collide.
     *
     * Note: this method will be called twice (once per entity), so only handle the receiving end of the collision.
     * (e.g. bot hits button, box does nothing, button updates being hit.)
     *
     * @param entity The other entity in the collision.
     */
    @Override
    public void collisionTrigger( Entity entity ){
        if (this.isTriggeredBy(entity)){
            if (!this.state){
                LogHelper.info("button was triggered!");
                this.state = true;
                this.wasUpdated = true;
            }
        }
    }

    @Override
    public void collisionEndTrigger( Entity entity ){
        if (this.isTriggeredBy(entity)){
            if (this.state){
                LogHelper.info("button trigger ended!");
                this.state = false;
                this.wasUpdated = true;
            }
        }
    }

    @Override
    public String toString(){
        String s = "Button: " + this.name + "{\n" +
                "   Pos: " + this.location + "\n" +
                "   State: " + this.state + "\n" +
                "   Interactables: {\n";
        for (Long id : this.linkedIds){
            s = s + "      " + id + "/n";
        }
        s = s + "   }\n + " +
                "}\n";

        return s;
    }

    @Override
    public Boolean getWasUpdated() {
        return this.wasUpdated;
    }

    @Override
    public void setWasUpdated(boolean wasUpdated) {
        this.wasUpdated = wasUpdated;
    }

    @Override
    public Geometry getGeometry() {
        return this.geometry;
    }

    public TriggerActionMessage getTriggerActionMessage() {
        return new TriggerActionMessage(this.getID(), this.state, this.location);
    }
}