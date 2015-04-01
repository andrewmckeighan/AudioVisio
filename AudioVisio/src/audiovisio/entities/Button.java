package audiovisio.entities;

import audiovisio.level.Level;
import audiovisio.networking.messages.TriggerActionMessage;
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
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import org.json.simple.JSONObject;

/**
 * //TODO this needs to be easy to walk over! (maybe no physical collision?)
 */
public class Button extends InteractableEntity {

    private static final Cylinder   SHAPE    = new Cylinder(8, 8, 0.5F * Level.SCALE.getX(), 0.03F * Level.SCALE.getY(), true);
    private static final Quaternion ROTATION = new Quaternion().fromAngles((float) Math.PI / 2, 0, 0);
    private static final float      MASS     = 0.0f;

    public Particle particle;

    public Button(){}

    @Override
    public void init( AssetManager assetManager ){
        Cylinder shape = Button.SHAPE;

        this.geometry = new Geometry(this.name + "testButtonName", shape);
        this.geometry.setLocalRotation(Button.ROTATION);
        this.geometry.setLocalTranslation(this.location.mult(Level.SCALE));

        Material randomMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        randomMaterial.setColor("Color", ColorRGBA.randomColor());
        this.geometry.setMaterial(randomMaterial);

        this.physics = new RigidBodyControl(Button.MASS);//TODO: this might not be needed if we don't want collision detection

        this.attachChild(this.geometry);
        this.addControl(this.physics);

        this.particle = new Particle();

        this.particle.init(assetManager);


        if (this.particle != null && this.particle.emitter != null){
//            this.footSteps.emitter.setLocalTranslation(this.getLocalTranslation());
            this.particle.emitter.setLocalTranslation(this.location);
            this.particle.emitter.setNumParticles(25);
        }

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
        LevelNode root = new LevelNode(String.format("#%d button @ %s", this.ID, this.location), true);
        LevelNode typeNode = new LevelNode("Type", "button", true);
        LevelNode idNode = new LevelNode("ID", this.ID, false);
        LevelNode nameNode = new LevelNode("Name", this.name, false);
        LevelNode locationNode = LevelUtils.vector2node(this.location);

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

    /**
     * method called from client onCollision even handler.
     * handles actions needed if these two entity types collide.
     *
     * Note: this method will be called twice (once per entity), so only handle the receiving end of the collision.
     * (e.g. bot hits button, box does nothing, button updates being hit.)
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

    /**
     * Checks the entity against all classes that are valid ways to collide with this entity.
     * @param entity entity to be checked
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
        //add other entities here (eg ball/box/etc..)
        return rBoolean;
    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);
        codeObj.put(JSONHelper.KEY_TYPE, "button");
    }

    @Override
    public void update( Boolean state ){
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

    public void startPress(){
        //particles for startPress
        //change color of button to something
        assert this.state;
        if (ClientAppState.isAudio){
            this.playSound();
            this.emitParticle();
        } else {
            this.updateVisuals();
        }
    }

    private void emitParticle(){}

    private void playSound(){
        //TODO
    }

    private void updateVisuals(){
        //TODO
        try{
            if (this.geometry.getMaterial() != null){
                this.geometry.getMaterial().setColor("updatedColor", ColorRGBA.randomColor());
            }
        } catch (IllegalArgumentException argumentException){
            // LogHelper.warn("Material not defined", argumentException);
        }
    }

    public void stopPress(){
        //particles for stopPress
        //change the color again.
        assert !this.state;

        if (ClientAppState.isAudio){
            this.playSound();
            this.emitParticle();
        } else {
            this.updateVisuals();
        }
    }

    public TriggerActionMessage getTriggerActionMessage(){
        //TODO
        return new TriggerActionMessage(this.getID(), this.state);
    }
}