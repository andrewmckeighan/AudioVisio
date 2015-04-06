package audiovisio.networking.messages;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;

@Serializable
public class SyncRigidBodyMessage extends PhysicsSyncMessage {

    public Vector3f location;
    public Matrix3f rotation;
    public Vector3f linearVelocity;
    public Vector3f angularVelocity;

    public SyncRigidBodyMessage(){
    }

    public SyncRigidBodyMessage( long id, PhysicsRigidBody body ){
        this.syncId = id;

        this.location = body.getPhysicsLocation(new Vector3f());
        this.rotation = body.getPhysicsRotationMatrix(new Matrix3f());
        this.linearVelocity = new Vector3f();
        body.getLinearVelocity(this.linearVelocity);
        this.angularVelocity = new Vector3f();
        body.getAngularVelocity(this.angularVelocity);
    }

    public void readData( PhysicsRigidBody body ){
        this.location = body.getPhysicsLocation(new Vector3f());
        this.rotation = body.getPhysicsRotationMatrix(new Matrix3f());
        this.linearVelocity = new Vector3f();
        body.getLinearVelocity(this.linearVelocity);
        this.angularVelocity = new Vector3f();
        body.getAngularVelocity(this.angularVelocity);
    }

    public void applyData( Object body ){
        if (body == null){
            return;
        }
        PhysicsRigidBody rigidBody = ((Spatial) body).getControl(RigidBodyControl.class);
        rigidBody.setPhysicsLocation(this.location);
        rigidBody.setPhysicsRotation(this.rotation);
        rigidBody.setLinearVelocity(this.linearVelocity);
        rigidBody.setAngularVelocity(this.angularVelocity);
    }
}
