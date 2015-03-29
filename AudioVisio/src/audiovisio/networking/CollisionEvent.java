package audiovisio.networking;

import audiovisio.entities.Entity;
import audiovisio.level.ITriggerable;
import audiovisio.utils.LogHelper;

/**
 * Created by Taylor Premo on 3/26/2015.
 */
public class CollisionEvent {
    private static float   timeToWait = 0.40F;
    public         boolean wasUpdated = true;
    private Entity collisionEntityA;
    private Entity collisionEntityB;
    private float  timer;

    public CollisionEvent( Entity entityA, Entity entityB ){
        this.collisionEntityA = entityA;
        this.collisionEntityB = entityB;
        if (this.collisionEntityA.equals(this.collisionEntityB)){
            LogHelper.warn("Object colliding with self!");
            this.wasUpdated = false;
            this.timer = 100;
        }
    }



    public Boolean check( float tpf ){
        Boolean collisionEnded = false;

        if (this.wasUpdated){
            this.timer = 0;
        } else {
            this.timer += tpf;
            if (this.timer > CollisionEvent.timeToWait){
                collisionEnded = true;
            }
        }

        this.wasUpdated = false;
        return collisionEnded;
    }

    public boolean hasSameEntities( Entity entityA, Entity entityB ){
        Boolean sameEntitiesBool = false;
        if (this.collisionEntityA.equals(entityA) && this.collisionEntityB.equals(entityB)){
            sameEntitiesBool = true;
        }
        if (this.collisionEntityA.equals(entityB) && this.collisionEntityB.equals(entityA)){
            sameEntitiesBool = true;
        }
        return sameEntitiesBool;
    }

    public void collisionEndTrigger(){
        if (this.collisionEntityA instanceof ITriggerable){
            this.collisionEntityA.collisionEndTrigger(this.collisionEntityB);
        }
        if (this.collisionEntityB instanceof ITriggerable){
            this.collisionEntityB.collisionEndTrigger(this.collisionEntityA);
        }
    }
}
