package audiovisio.networking;

import audiovisio.networking.messages.PhysicsSyncMessage;

public interface SyncMessageValidator {
    boolean checkMessage( PhysicsSyncMessage message );
}
