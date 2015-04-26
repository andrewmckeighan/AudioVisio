/*
 * Entity
 *
 * Version information
 *
 * 02/18/15
 *
 * Copyright notice
 */

package audiovisio.entities;

import audiovisio.entities.particles.PlayerParticle;
import audiovisio.level.ILevelItem;
import audiovisio.rsle.editor.LevelNode;
import audiovisio.rsle.editor.RSLESetter;
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
import com.jme3.scene.Spatial;
import org.json.simple.JSONObject;

public class Entity extends Node implements ILevelItem {
    public static final String KEY_NAME = "name";
    public Vector3f location;
    //    public Node         rootNode;
//    public PhysicsSpace physicsSpace;
    public Vector3f position;
    public Spatial  model;
    //Read from JSON
    protected long ID = Long.MIN_VALUE;
    protected Geometry         geometry;
    protected RigidBodyControl physics;
    //read from files
    protected String modelFile      = "";
    protected String materialString = "Common/MatDefs/Misc/Unshaded.j3md";
    protected Material       material;
    protected ColorRGBA      color;
    protected Node           rootNode;
    protected PhysicsSpace   physicsSpace;
    protected PlayerParticle particle;

    public Entity() {
    }

    /**
     * Create and instance of Entity class,
     *
     * @param loadObj the JSON object that is read.
     */
    @Override
    public void load(JSONObject loadObj) {
        this.ID = (Long) loadObj.get(JSONHelper.KEY_ID);
        this.name = (String) loadObj.get(Entity.KEY_NAME);

        JSONObject location = (JSONObject) loadObj.get(JSONHelper.KEY_LOCATION);
        this.location = JSONHelper.readVector3f(location);

        this.color = JSONHelper.readColor((String) loadObj.get(JSONHelper.KEY_COLOR));
    }

    /**
     * @param assetManager A reference to the assetManager to allow
     */
    @Override
    public void init(AssetManager assetManager) {
        if (!this.modelFile.isEmpty()) {
            assetManager.loadModel(this.modelFile);
        }
        if (!this.materialString.isEmpty()) {
            this.material = new Material(assetManager, this.materialString);
            this.material.setColor("Color", this.color);
        }
    }

    @Override
    public void start(Node rootNode, PhysicsSpace physics) {
        try {
            LogHelper.finest(this + "has started");
        } catch (NullPointerException nullException) {
            LogHelper.severe("Entity " + this.name + "tried to start without being fully initialized!\n", nullException);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save(JSONObject codeObj) {
        JSONObject location = JSONHelper.saveVector3f(this.location);
        codeObj.put(JSONHelper.KEY_ID, this.getID());
        codeObj.put(JSONHelper.KEY_LOCATION, location);
        codeObj.put(Entity.KEY_NAME, this.name);
    }

    @Override
    public LevelNode getLevelNode() {
        LevelNode root = new LevelNode(String.format("#%d @ %s - Entity does not define LevelNode structure", this.getID(), this.location), true);
        LevelNode typeNode = new LevelNode("Type", "entity", true);
        LevelNode idNode = new LevelNode("ID", this.getID(), true);
        LevelNode location = LevelUtils.vector2node(this.location);

        root.add(typeNode);
        root.add(idNode);
        root.add(location);

        return root;
    }

    /**
     * A shallow toString Method.
     *
     * @return this.name Only return the name of the object on shallowToString
     */
    public String toStringShort() {
        return this.name;
    }

    /**
     * TODO: checks if the two collisions are meaningful.
     *
     * @param entityB the entity that this is colliding with.
     */
    public void collisionTrigger(Entity entityB) {
    }

    public void collisionEndTrigger(Entity collisionEntityB) {
    }

    @Override
    public String toString() {
        return "Entity[" + this.ID + "]";
    }

    @RSLESetter("Name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the iD
     */
    @Override
    public long getID() {
        return this.ID;
    }

    /**
     * @param id the iD to set
     */
    @RSLESetter("ID")
    @Override
    public void setID(long id) {
        this.ID = id;
    }

    public Vector3f getLocation() {
        return this.location;
    }
}