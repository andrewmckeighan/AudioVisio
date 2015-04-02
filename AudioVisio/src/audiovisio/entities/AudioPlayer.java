package audiovisio.entities;

import audiovisio.level.Level;

public class AudioPlayer extends Player {
    public AudioPlayer( Level level ){
        super(null, level.getAudioSpawn());
        this.level = level;
    }
}