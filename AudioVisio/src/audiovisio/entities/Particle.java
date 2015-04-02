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

    @Override
    public void init( AssetManager assetManager ){
        ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(assetManager,
                "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture(
                "Effects/Explosion/flame.png"));
        fire.setMaterial(mat_red);
        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(ColorRGBA.Blue);
        fire.setStartColor(ColorRGBA.Blue);
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(1.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(20f);
        fire.setHighLife(30f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        fire.setInWorldSpace(true);//TODO this should prevent particles from moving when emitter moves.

        this.emitter = fire;
    }

    @Override
    public void start( Node rootNode, PhysicsSpace physics ){
        super.start(rootNode, physics);
        rootNode.attachChild(this.emitter);
    }
}