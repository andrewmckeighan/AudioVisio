package audiovisio.utils;

import audiovisio.networking.messages.*;

import com.jme3.network.NetworkClient;
import com.jme3.network.serializing.Serializer;
import com.jme3.math.Vector3f;

import java.io.IOException;
import java.net.*;
import java.text.DecimalFormat;
import java.util.Enumeration;


public class NetworkUtils {
    
    public static int PORT;

    public static final float NETWORK_SYNC_FREQUENCY = 0.25f;
    
    public static synchronized void initializeSerializables(){
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

    /**
     * Get the LAN IP address of the current host.
     *
     * @return The address of the host if found, null otherwise.
     */
    public static String getIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                Enumeration<InetAddress> addrs = iface.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();

                    if (addr instanceof Inet4Address) {
                        Inet4Address ipv4 = (Inet4Address) addr;

                        if (ipv4.isLoopbackAddress())
                            continue;
                        if (ipv4.isLinkLocalAddress())
                            continue;
                        if (ipv4.isMulticastAddress())
                            continue;
                        if (ipv4.isSiteLocalAddress())
                            return ipv4.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Boolean attemptConnection(NetworkClient client){
        LogHelper.info("attemptConnection");
        for (int i = 0; i < 500; i++) {
            try{
                client.connectToServer("127.0.0.1", PORT, PORT);
                return true;
            } catch (IOException e){
                LogHelper.info(String.format("Attempt %d to connect to server", i));
            }
            try {
                Thread.sleep(50l);
            } catch (InterruptedException ex) {}
        }
        return false;
    };
}
