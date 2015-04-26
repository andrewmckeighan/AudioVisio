package audiovisio.entities;

import audiovisio.level.ITriggerable;
import audiovisio.networking.messages.TriggerActionMessage;
import audiovisio.utils.LogHelper;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InteractableEntity extends Entity implements ITriggerable {

    public static final String    KEY_LINKED = "interactionList";
    protected static    ColorRGBA COLOR      = ColorRGBA.Blue;
    public boolean          stuck; //if the entity keeps it state regardless of triggerEvents
    public Geometry         geometry;
    //    public Material         material;
    public RigidBodyControl physics;
    public boolean          wasUpdated;
    protected Set<Long> linkedIds = new HashSet<Long>();
    protected Map<Long, ITriggerable> interactionMap;
    protected Boolean state = false;
    private InteractableEntity linkedEntity;

    public InteractableEntity(){}

    public InteractableEntity( boolean stuck ){
        this.stuck = stuck;
    }

    @Override
    public void load( JSONObject loadObj ){
        super.load(loadObj);

        JSONArray interactionList = (JSONArray) loadObj.get("interactionList");
        for (Object oItem : interactionList){
            this.linkedIds.add((Long) oItem);
        }
    }

    @Override
    public void save( JSONObject codeObj ){
        super.save(codeObj);

        JSONArray linked = new JSONArray();
        for (Long id : this.linkedIds){
            linked.add(id);
        }

        codeObj.put(InteractableEntity.KEY_LINKED, linked);
    }

    public void update( Set<Long> idList, Boolean state ){
        this.update(state);
        for (Long id : idList){
            LogHelper.info("updating: " + id);
            this.interactionMap.get(id).update(state);
        }
    }

    @Override
    public void update( Boolean state ){
        this.state = state;
    }

    public void update( TriggerActionMessage message ){
        this.update(message.getState());
        for (Long id : this.linkedIds){
            this.interactionMap.get(id).update(this.state);
        }
        this.wasUpdated = false;
    }

    @Override
    public Set<Long> getLinked(){
        if (this.linkedIds == null){ throw new IllegalStateException("Link Ids are null! Did you load the object?"); }

        return this.linkedIds;
    }

    @Override
    public void setLinks( Set<Long> ids ){
        this.linkedIds = ids;
    }

    @Override
    public void resolveLinks( Map<Long, ITriggerable> links ){
        if (this.interactionMap != null){ throw new IllegalStateException("Links have already been resolved!"); }
        this.interactionMap = links;
        this.linkedIds = links.keySet();
    }

    @Override
    public void link( Long id ){
        if (this.linkedIds != null){ this.linkedIds.add(id); }
    }

    public Entity getLinkedEntity(){
        return this.linkedEntity;
    }

    public void setLinkedEntity( InteractableEntity entity ){
        this.linkedEntity = entity;
    }

    public TriggerActionMessage getTriggerActionMessage(){
        return new TriggerActionMessage(this.getID(), this.state, this.location);
    }

    public Set<Long> getIDList( Boolean state ){
        return this.linkedIds;
    }
}