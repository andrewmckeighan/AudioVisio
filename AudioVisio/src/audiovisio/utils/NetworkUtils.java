package audiovisio.utils;

import audiovisio.networking.messages.*;
import com.jme3.network.NetworkClient;
import com.jme3.network.serializing.Serializer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class NetworkUtils {

    public static final float NETWORK_SYNC_FREQUENCY = 0.25f;
    public static int PORT;
    public static String ipAddress = "127.0.0.1";

    public static synchronized void initializeSerializables(){
        // Client Messages
        Serializer.registerClass(PlayerSendMovementMessage.class);

        // Server Messages
        Serializer.registerClass(PlayerLocationMessage.class);
        Serializer.registerClass(PlayerJoinMessage.class);
        Serializer.registerClass(PlayerLeaveMessage.class);

        // Sync Messages
        Serializer.registerClass(PhysicsSyncMessage.class);
        Serializer.registerClass(SyncCharacterMessage.class);
        Serializer.registerClass(SyncRigidBodyMessage.class);
        Serializer.registerClass(TriggerActionMessage.class);

    }

    public static int getPort(){
        return NetworkUtils.PORT;
    }

    public static void setPort( int portNumber ){
        NetworkUtils.PORT = portNumber;
    }

    /**
     * Get the LAN IP address of the current host.
     *
     * Based on: http://stackoverflow.com/a/14364233
     *
     * @return The address of the host if found, null otherwise.
     */
    public static String getIP(){
        try{
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface iface = interfaces.nextElement();

                Enumeration<InetAddress> addrs = iface.getInetAddresses();
                while (addrs.hasMoreElements()){
                    InetAddress addr = addrs.nextElement();

                    if (addr instanceof Inet4Address){
                        Inet4Address ipv4 = (Inet4Address) addr;

                        if (ipv4.isLoopbackAddress()){ continue; }
                        if (ipv4.isLinkLocalAddress()){ continue; }
                        if (ipv4.isMulticastAddress()){ continue; }
                        return ipv4.getHostAddress();
                    }
                }
            }
        } catch (SocketException e){
            e.printStackTrace();
        }

        return null;
    }

    public static void setIp( String ip ){
        ipAddress = ip;
    }

    public static Boolean attemptConnection( NetworkClient client ){
        LogHelper.info("attemptConnection");
        LogHelper.info("Connecting to " + ipAddress);
        for (int i = 0; i < 500; i++){
            try{
                if (client.isConnected()){
                    LogHelper.info("Actually connected");
                    return true;
                }
                client.connectToServer(ipAddress, NetworkUtils.PORT, NetworkUtils.PORT);
                client.start();
                return true;
            } catch (IOException e){
                LogHelper.info(String.format("Attempt %d to connect to server", i));
            }
            try{
                Thread.sleep(50l);
            } catch (InterruptedException ex){}
        }
        LogHelper.info("Unable to connect to server.");
        return false;
    }
}
