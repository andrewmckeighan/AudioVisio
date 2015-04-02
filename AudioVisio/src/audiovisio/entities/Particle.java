package audiovisio.entities;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

//TODO why do particles reset?
public class Particle extends Entity {
    public ParticleEmitter emitter;
    private String    material  = "Common/MatDefs/Misc/Particle.j3md";
    private String    texture   = "Effects/Explosion/flame.png";
    private ColorRGBA color     = ColorRGBA.Blue;
    private Vector3f  velocity  = new Vector3f(0, 2, 0);
    private Float     startSize = 20.0F;
    private Float     endSize   = 30.0F;

    public Particle(){}

    public void load(){}

    @Override
    public void init( AssetManager assetManager ){
        ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(assetManager,
                this.material);
        mat_red.setTexture("Texture", assetManager.loadTexture(
                this.texture));
        fire.setMaterial(mat_red);
        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(this.color);
        fire.setStartColor(this.color);
        fire.getParticleInfluencer().setInitialVelocity(this.velocity);
        fire.setStartSize(1.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(this.startSize);
        fire.setHighLife(this.endSize);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        fire.setInWorldSpace(true);//TODO this should prevent particles from moving when emitter moves.

        this.emitter = fire;
        this.attachChild(this.emitter);
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        super.start(rootNode, physics);
    }
}