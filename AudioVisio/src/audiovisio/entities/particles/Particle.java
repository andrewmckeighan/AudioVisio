package audiovisio.entities.particles;

import audiovisio.AudioVisio;
import audiovisio.entities.Entity;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * @author Matt Gerst
 */
public class Particle extends Entity {
    private static final float PARTICLES_PER_SECOND = 5;
    public ParticleEmitter emitter;
    protected String    material = "Common/MatDefs/Misc/Particle.j3md";
    protected String    texture  = "Effects/Explosion/flame.png";
    protected ColorRGBA color    = ColorRGBA.Red;
    protected Vector3f  velocity = new Vector3f(0, 2, 0);
    protected Float     lowLife  = 2.0F;
    protected Float     highLife = 3.0F;
    private ParticleEmitter fire;

    public Particle(){}


    @Override
    public void init( AssetManager assetManager ){
        fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat_red = new Material(assetManager,
                this.material);
        mat_red.setTexture("Texture", assetManager.loadTexture(
                this.texture));

        fire.setMaterial(mat_red);
        fire.setImagesX(2);
        fire.setImagesY(2);
        if(fire.getStartColor() == null || fire.getEndColor() == null){
            fire.setEndColor(this.color);
            fire.setStartColor(this.color);
        }
        fire.getParticleInfluencer().setInitialVelocity(this.velocity);
        fire.setStartSize(1.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(this.lowLife);
        fire.setHighLife(this.highLife);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        fire.setInWorldSpace(true);

        this.emitter = fire;
        this.attachChild(this.emitter);
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        super.start(rootNode, physics);

        if (AudioVisio.difficulty == 0){
            this.emitter.setParticlesPerSec(PARTICLES_PER_SECOND);
            this.emitter.setEnabled(false);
        }
    }
    public void setParticleColor(ColorRGBA value){
        fire.setStartColor(value);
        fire.setEndColor(value);
    }

}
