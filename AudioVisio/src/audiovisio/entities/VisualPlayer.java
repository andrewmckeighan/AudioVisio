package audiovisio.entities;

import audiovisio.level.Level;

public class VisualPlayer extends Player {
    public VisualPlayer( Level level ){
        super(null, level.getVisualSpawn());
        this.level = level;
    }
}