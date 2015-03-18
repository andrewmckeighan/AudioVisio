package audiovisio.networking;

import audiovisio.WorldManager;
import audiovisio.entities.Player;
import audiovisio.networking.messages.PhysicsSyncMessage;
import audiovisio.networking.messages.SyncCharacterMessage;
import audiovisio.networking.messages.SyncRigidBodyMessage;
import audiovisio.utils.LogHelper;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.network.Client;
import com.jme3.network.*;
import com.jme3.network.Server;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

/**
 * This class manages the messages system to ensure syncing between client and server works properly.
 *
 * Messages to the server are received are handled and rebroadcast to the clients.
 * Messages to the client are added to a queue and the queue is emptied.
 *
 * @author Taylor Premo
 * @author Matt Gerst
 */

public class SyncManager extends AbstractAppState implements MessageListener {

    //References
    private Application app                             = null;
    private Server server                               = null;
    private Client client                               = null;

    //Lists
    private LinkedList<PhysicsSyncMessage> messageQueue = new LinkedList<PhysicsSyncMessage>();
    private LinkedList<SyncMessageValidator> validators = new LinkedList<SyncMessageValidator>();
    private HashMap<Long, Object> objectMap             = new HashMap<Long, Object>();

    //Timers
    private double time                                 = 0;
    private float timeSinceLastSync                     = 0f;
    private float serverSyncFrequency                   = 0.1f;
    private double clientSyncOffset                     = Double.MIN_VALUE;
    private double maxDelay                             = 0.4;

    public SyncManager(Application app, Server server) {
        this.app = app;
        this.server = server;
    }

    public SyncManager(Application app, Client client) {
        this.app = app;
        this.client = client;
    }

    /**
     * updates the manager, sends messages to the client in order.
     * or syncs the info on the server periodically.
     *
     * @param tpf time per frame
     */

    @Override
    public void update(float tpf) {
        time += tpf;

        if (time < 0) {
            time = 0;//prevent overflow;
        }

        if (client != null) {
            for (Iterator<PhysicsSyncMessage> iter = messageQueue.iterator(); iter.hasNext(); ) {
                PhysicsSyncMessage message = iter.next();
                if (message.time >= time + clientSyncOffset) {
                    handleMessage(message);
                    iter.remove();
                }
            }
        } else if (server != null) {
            timeSinceLastSync += tpf;
            if (timeSinceLastSync >= serverSyncFrequency) {
                sendSyncData();
                timeSinceLastSync = 0;
            }
        }
    }

    /**
     * Applies the transformation stored inside the message to the object in objectMap that has the same ID as the message.
     *
     * @param message The message that was received.
     */

    protected void handleMessage(PhysicsSyncMessage message) {
        Object obj = objectMap.get(message.syncId);

        if (obj != null) {
            message.applyData(obj);
        } else {
            if (client != null) {
                WorldManager wm = (WorldManager) objectMap.get(-1L);
                assert message instanceof SyncCharacterMessage;
                SyncCharacterMessage msg = (SyncCharacterMessage) message;
                wm.addPlayer(msg.syncId);
            }
            LogHelper.warn("Cannot find obj in message: " + message + " with ID: " + message.syncId);
        }
    }

    /**
     * Adds the message received to the queue of messages that the client will handle.
     *
     * @param message The message that was received.
     */
    protected void enqueueMessage(PhysicsSyncMessage message) {
        if (clientSyncOffset == Double.MIN_VALUE) {
            clientSyncOffset = this.time - message.time;
        }
        double delay = (message.time + clientSyncOffset) - time;
        if (delay < maxDelay) {
            clientSyncOffset -= delay - maxDelay;
        } else if (delay < 0) {
            clientSyncOffset -= delay;
        }
        messageQueue.add(message);
    }

    /**
     * Creates a SyncCharacterMessage and broadcasts it to the clients.
     * May be modified to handle more messages in the future.
     */

    protected void sendSyncData() {
        for (Entry<Long, Object> entry : objectMap.entrySet()) {
            if (entry.getValue() instanceof Spatial) {
                Spatial spat = (Spatial) entry.getValue();
                PhysicsRigidBody body = spat.getControl(RigidBodyControl.class);
                if (body == null) {
                    body = spat.getControl(VehicleControl.class);
                }
                if (body != null && body.isActive()) {
                    SyncRigidBodyMessage msg = new SyncRigidBodyMessage(entry.getKey(), body);
                    broadcast(msg);
                    continue;
                }
                BetterCharacterControl control = spat.getControl(BetterCharacterControl.class);
                if (control != null) {
                    assert entry.getValue() instanceof Player;
                    Player p = (Player) entry.getValue();
                    SyncCharacterMessage msg = p.getSyncCharacterMessage();
                    LogHelper.info("SyncManager.sendSyncData " + msg);
                    broadcast(msg);
                }
            }
        }
    }

    /**
     * Sends the message to all clients listening to the server.
     *
     * @param message The message to be broadcast.
     */
    public void broadcast(PhysicsSyncMessage message) {
        if (server == null) {
            LogHelper.severe("broadcasting from client: " + message);
            return;
        }

        message.time = time;
        server.broadcast(message);
    }

    /**
     * Sends the message from the client to the server.
     *
     * @param client  client sending the message
     * @param message message to be sent.
     */

    public void send(HostedConnection client, PhysicsSyncMessage message) {
        message.time = time;
        if (client == null) {
            LogHelper.severe("Client is null when sending: " + message);
            return;
        }

        client.send(message);
    }

    /**
     * Handles messages recieved from either the client or server.
     *
     * If client is receiving, adds to the queue.
     * If server is receiving, broadcast to all clients and then handle.
     *
     * @param source  Needed to implement MessageListener, currently not used.
     * @param message The message that was received.
     */

    public void messageReceived(Object source, final Message message) {
        assert (message instanceof PhysicsSyncMessage);
        if (client != null) {
            app.enqueue(new Callable<Void>() {
                public Void call() throws Exception {
                    enqueueMessage((PhysicsSyncMessage) message);
                    return null;
                }
            });
        } else if (server != null) {
            app.enqueue(new Callable<Void>() {
                public Void call() throws Exception {
                    for (SyncMessageValidator syncMessageValidator : validators) {
                        if (!syncMessageValidator.checkMessage((PhysicsSyncMessage) message)) {
                            return null;
                        }
                    }
                    broadcast((PhysicsSyncMessage) message);
                    handleMessage((PhysicsSyncMessage) message);
                    return null;
                }
            });

        }
    }

    /**
     * Sets what classes the SyncManager will listen for and handle.
     *
     * @param classes the message classes to be handled by this.
     */

    public void setMessageTypes(Class... classes) {
        if (server != null) {
            server.removeMessageListener(this);
            server.addMessageListener(this, classes);
        } else if (client != null) {
            client.removeMessageListener(this);
            client.addMessageListener(this, classes);
        }
    }

    public void addObject(long id, Object object) {
        objectMap.put(id, object);
    }

    public void removeObject(long id) {
        objectMap.remove(id);
    }

    public void removeObject(Object obj) {
        for (Iterator<Entry<Long, Object>> iter = objectMap.entrySet().iterator(); iter.hasNext(); ) {
            Entry<Long, Object> entry = iter.next();

            if (entry.getValue() == obj) {
                iter.remove();
                return;
            }
        }
    }

    public void clearObjects() {
        objectMap.clear();
    }

    public void addMessageValidator(SyncMessageValidator validator) {
        validators.add(validator);
    }

    public void removeMessageValidator(SyncMessageValidator validator) {
        validators.remove(validator);
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public double getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(double maxDelay) {
        this.maxDelay = maxDelay;
    }

    public float getServerSyncFrequency() {
        return serverSyncFrequency;
    }

    public void setServerSyncFrequency(float serverSyncFrequency) {
        this.serverSyncFrequency = serverSyncFrequency;
    }
}
