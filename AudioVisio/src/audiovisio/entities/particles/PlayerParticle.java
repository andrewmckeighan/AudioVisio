package audiovisio.entities.particles;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;

//TODO why do particles reset?
public class PlayerParticle extends Particle {
    @Override
    public void init( AssetManager assetManager ){
        this.color = ColorRGBA.Yellow;

        super.init(assetManager);
    }
}