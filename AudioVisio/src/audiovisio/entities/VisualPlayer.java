package audiovisio.entities;

import audiovisio.entities.particles.PlayerParticle;
import audiovisio.level.Level;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class VisualPlayer extends Player {
    private PlayerParticle particle;
    private AudioNode      audioSteps;

    @Override
    public void load( Level level ){
        super.load(level);

        this.spawn = level.getVisualSpawn();
    }

    @Override
    public void init( AssetManager assetManager ){
        super.init(assetManager);

        this.audioSteps = new AudioNode(assetManager, "Sound/Effects/Foot steps.ogg", false);
        this.particle = new PlayerParticle();
        this.particle.init(assetManager);

        this.attachChild(this.audioSteps);
        this.attachChild(this.particle);
//        this.particle.emitter.setEnabled(false);
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        super.start(rootNode, physics);

        this.particle.start(rootNode, physics);
    }

    @Override
    public void update( Vector3f direction, Quaternion rotation ){
        super.update(direction, rotation);

        assert this.particle != null && this.particle.emitter != null;
        assert this.model == null;

//        this.particle.emitter.setEnabled(true);
//    this.particle.emitter.setLocalTranslation(this.getLocalTranslation());
        this.particle.emitter.setNumParticles((int) direction.length() * 3 + 1);


        if (!this.isServer()){
            if (direction.length() != 0){
                this.audioSteps.setLooping(false);
                this.audioSteps.setPositional(false);
                this.audioSteps.setVolume(3);
                if (this.audioSteps.getStatus() != AudioSource.Status.Playing){
                    this.audioSteps.play();
                }
                if(!this.particle.emitter.isEnabled()){
                    this.particle.emitter.setEnabled(true);
                }
            } else {
                this.audioSteps.pause();
                this.particle.emitter.setEnabled(false);
            }
        }

    }
}