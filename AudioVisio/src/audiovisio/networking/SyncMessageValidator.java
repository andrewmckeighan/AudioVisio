package audiovisio.networking;

import audiovisio.networking.messages.PhysicsSyncMessage;

public interface SyncMessageValidator {
    public boolean checkMessage(PhysicsSyncMessage message);
}
