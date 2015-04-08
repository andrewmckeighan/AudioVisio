package audiovisio.entities;

import audiovisio.level.Level;
import audiovisio.networking.messages.SyncCharacterMessage;
import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class AudioPlayer extends Player {

    @Override
    public void load( Level level ){
        super.load(level);

        this.spawn = level.getAudioSpawn();
    }

    @Override
    public void init( AssetManager assetManager ){
        super.init(assetManager);

        this.model = (Node) assetManager.loadModel(Player.DEFAULT_MODEL);
        this.model.setLocalScale(0.5F);
        this.attachChild(this.model);
    }

    @Override
    public void update( Vector3f direction, Quaternion rotation ){
        super.update(direction, rotation);
        if (this.model != null){
            this.model.setLocalTranslation(this.getLocalTranslation().add(Player.MODEL_OFFSET));
            //rotation.set(rotation.getX(), 0, rotation.getZ(), 0);//TODO fix Y rotaion
            this.model.setLocalRotation(rotation);
        }
    }

    @Override
    public SyncCharacterMessage getSyncCharacterMessage(){
        SyncCharacterMessage syncCharacterMessage = super.getSyncCharacterMessage();
        if (this.model != null){
            syncCharacterMessage.rotation = this.model.getLocalRotation();
        }
        return syncCharacterMessage;
    }
}