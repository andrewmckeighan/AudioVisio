package audiovisio.entities.particles;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.ColorRGBA;

/**
 * @author Matt Gerst
 */
public class ButtonParticle extends Particle {
    @Override
    public void init( AssetManager assetManager ){
        this.color = ColorRGBA.Blue;

        super.init(assetManager);
    }
}
