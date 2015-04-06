package audiovisio.networking.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable()
public abstract class PhysicsSyncMessage extends AbstractMessage {

    public long syncId = -1;
    public double time;

    public PhysicsSyncMessage(){
        super(true);
    }

    public PhysicsSyncMessage( long id ){
        super(true);
        this.syncId = id;
    }

    public abstract void applyData( Object object );
}
