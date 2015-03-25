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
    private Application app;
    private Server      server;
    private Client      client;

    //Lists
    private LinkedList<PhysicsSyncMessage>   messageQueue = new LinkedList<PhysicsSyncMessage>();
    private LinkedList<SyncMessageValidator> validators   = new LinkedList<SyncMessageValidator>();
    private HashMap<Long, Object>            objectMap    = new HashMap<Long, Object>();

    //Timers
    private double time;
    private float  timeSinceLastSync;
    private float  serverSyncFrequency = 0.1f;
    private double clientSyncOffset    = Double.MIN_VALUE;
    private double maxDelay            = 0.4;

    public SyncManager( Application app, Server server ){
        this.app = app;
        this.server = server;
    }

    public SyncManager( Application app, Client client ){
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
    public void update( float tpf ){
        this.time += tpf;

        if (this.time < 0){
            this.time = 0;//prevent overflow;
        }

        if (this.client != null){
            for (Iterator<PhysicsSyncMessage> iter = this.messageQueue.iterator(); iter.hasNext(); ){
                PhysicsSyncMessage message = iter.next();
                if (message.time >= this.time + this.clientSyncOffset){
                    this.handleMessage(message);
                    iter.remove();
                }
            }
        } else if (this.server != null){
            this.timeSinceLastSync += tpf;
            if (this.timeSinceLastSync >= this.serverSyncFrequency){
                this.sendSyncData();
                this.timeSinceLastSync = 0;
            }
        }
    }

    /**
     * Applies the transformation stored inside the message to the object in objectMap that has the same ID as the message.
     *
     * @param message The message that was received.
     */

    protected void handleMessage(PhysicsSyncMessage message) {
        Object obj = this.objectMap.get(message.syncId);

        if (obj != null) {
            message.applyData(obj);
        } else {
            if (this.client != null) {
                WorldManager wm = (WorldManager) this.objectMap.get(-1L);
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
        if (this.clientSyncOffset == Double.MIN_VALUE) {
            this.clientSyncOffset = this.time - message.time;
        }
        double delay = (message.time + this.clientSyncOffset) - this.time;
        if (delay < this.maxDelay) {
            this.clientSyncOffset -= delay - this.maxDelay;
        } else if (delay < 0) {
            this.clientSyncOffset -= delay;
        }
        this.messageQueue.add(message);
    }

    /**
     * Creates a SyncCharacterMessage and broadcasts it to the clients.
     * May be modified to handle more messages in the future.
     */

    protected void sendSyncData() {
        for (Entry<Long, Object> entry : this.objectMap.entrySet()) {
            if (entry.getValue() instanceof Spatial) {
                Spatial spat = (Spatial) entry.getValue();
                PhysicsRigidBody body = spat.getControl(RigidBodyControl.class);
                if (body == null) {
                    body = spat.getControl(VehicleControl.class);
                }
                if (body != null && body.isActive()) {
                    SyncRigidBodyMessage msg = new SyncRigidBodyMessage(entry.getKey(), body);
                    this.broadcast(msg);
                    continue;
                }
                BetterCharacterControl control = spat.getControl(BetterCharacterControl.class);
                if (control != null) {
                    assert entry.getValue() instanceof Player;
                    Player p = (Player) entry.getValue();
                    SyncCharacterMessage msg = p.getSyncCharacterMessage();
                    LogHelper.info("SyncManager.sendSyncData " + msg);
                    this.broadcast(msg);
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
        if (this.server == null) {
            LogHelper.severe("broadcasting from client: " + message);
            return;
        }

        message.time = this.time;
        this.server.broadcast(message);
    }

    /**
     * Sends the message from the client to the server.
     *
     * @param client  client sending the message
     * @param message message to be sent.
     */

    public void send(HostedConnection client, PhysicsSyncMessage message) {
        message.time = this.time;
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
        if (this.client != null) {
            this.app.enqueue(new Callable<Void>() {
                public Void call() throws Exception{
                    SyncManager.this.enqueueMessage((PhysicsSyncMessage) message);
                    return null;
                }
            });
        } else if (this.server != null) {
            this.app.enqueue(new Callable<Void>() {
                public Void call() throws Exception{
                    for (SyncMessageValidator syncMessageValidator : SyncManager.this.validators){
                        if (!syncMessageValidator.checkMessage((PhysicsSyncMessage) message)){
                            return null;
                        }
                    }
                    SyncManager.this.broadcast((PhysicsSyncMessage) message);
                    SyncManager.this.handleMessage((PhysicsSyncMessage) message);
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
        if (this.server != null) {
            this.server.removeMessageListener(this);
            this.server.addMessageListener(this, classes);
        } else if (this.client != null) {
            this.client.removeMessageListener(this);
            this.client.addMessageListener(this, classes);
        }
    }

    public void addObject(long id, Object object) {
        this.objectMap.put(id, object);
    }

    public void removeObject(long id) {
        this.objectMap.remove(id);
    }

    public void removeObject(Object obj) {
        for (Iterator<Entry<Long, Object>> iter = this.objectMap.entrySet().iterator(); iter.hasNext(); ) {
            Entry<Long, Object> entry = iter.next();

            if (entry.getValue() == obj) {
                iter.remove();
                return;
            }
        }
    }

    public void clearObjects() {
        this.objectMap.clear();
    }

    public void addMessageValidator(SyncMessageValidator validator) {
        this.validators.add(validator);
    }

    public void removeMessageValidator(SyncMessageValidator validator) {
        this.validators.remove(validator);
    }

    public Server getServer() {
        return this.server;
    }

    public Client getClient() {
        return this.client;
    }

    public double getMaxDelay() {
        return this.maxDelay;
    }

    public void setMaxDelay(double maxDelay) {
        this.maxDelay = maxDelay;
    }

    public float getServerSyncFrequency() {
        return this.serverSyncFrequency;
    }

    public void setServerSyncFrequency(float serverSyncFrequency) {
        this.serverSyncFrequency = serverSyncFrequency;
    }
}
