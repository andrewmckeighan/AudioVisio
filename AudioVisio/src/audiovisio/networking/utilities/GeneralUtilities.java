package audiovisio.networking.utilities;

import audiovisio.networking.messages.*;

import com.jme3.network.serializing.Serializer;

public class GeneralUtilities {
	
	public static int PORT;

	public static final float NETWORK_SYNC_FREQUENCY = 0.25f;
	
	public static void initializeSerializables(){
		// General Messages
		Serializer.registerClass(NetworkMessage.class);
		
		// Client Messages
		Serializer.registerClass(PlayerSendMovementMessage.class);
		
		// Server Messages
		Serializer.registerClass(PlayerLocationMessage.class);
		Serializer.registerClass(PlayerJoinMessage.class);
		Serializer.registerClass(PlayerLeaveMessage.class);
		Serializer.registerClass(PlayerListMessage.class);

		// Sync Messages
		Serializer.registerClass(PhysicsSyncMessage.class);
		Serializer.registerClass(SyncCharacterMessage.class);
		Serializer.registerClass(SyncRigidBodyMessage.class);
	}
	
	public static void setPort(int portNumber){
		
		PORT = portNumber;
		
	}
	
	public static int getPort(){
		
		return PORT;
		
	}

}
