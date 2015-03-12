package audiovisio.networking;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import audiovisio.WorldManager;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Vector3f;
import com.jme3.network.MessageListener;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.network.Server;
import com.jme3.network.Client;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Spatial;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;







import audiovisio.entities.Player;
import audiovisio.networking.messages.*;
import audiovisio.utils.LogHelper;



public class SyncManager extends AbstractAppState implements MessageListener {

    double time = 0;
    double offset = Double.MIN_VALUE;
//    double offset =  0.1f;
    LinkedList<SyncMessageValidator> validators = new LinkedList<SyncMessageValidator>();
    float timer = 0f;
    LinkedList<PhysicsSyncMessage> messageQueue = new LinkedList<PhysicsSyncMessage>();
    HashMap<Long, Object> objectMap = new HashMap<Long, Object>();
    Application app;
    private Server server;
    private Client client;
    private float syncFrequency = 0.1f;
    private double maxDelay = 0.4;

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
                if (message.time >= time + offset) {
                    handleMessage(message);
                    iter.remove();
                }
            }

        } else if (server != null) {
            timer += tpf;
            if (timer >= syncFrequency) {
                sendSyncData();
                timer = 0;
            }
        }
    }

    public void addObject(long id, Object object) {
        objectMap.put(id, object);
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

    public void removeObject(long id) {
        objectMap.remove(id);
    }

    public void clearObjects() {
        objectMap.clear();
    }

    protected void handleMessage(PhysicsSyncMessage message) {
        Object obj = objectMap.get(message.syncId);

        if (obj != null) {
            message.applyData(obj);
        } else {
            if (client != null) {
                WorldManager wm = (WorldManager) objectMap.get(-1L);
                SyncCharacterMessage msg = (SyncCharacterMessage) message;
                wm.addPlayer(msg.syncId);
            }
            LogHelper.warn("Cannot find obj in message: " + message + " with ID: " + message.syncId);
        }
    }

    protected void enqueueMessage(PhysicsSyncMessage message) {
        if (offset == Double.MIN_VALUE) {
            offset = this.time - message.time;
        }
        double delay = (message.time + offset) - time;
        if (delay < maxDelay) {
            offset -= delay - maxDelay;
        } else if (delay < 0) {
            offset -= delay;
        }
        messageQueue.add(message);
    }

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
                    LogHelper.info("SyncManager.sendSyncData Player is Sending obj (" + msg.syncId + ") sync to " + msg.location + " walking " + msg.walkDirection + " looking " + msg.viewDirection);
                    LogHelper.warn("");
                    broadcast(msg);
                }
            }
        }
    }

    public void broadcast(PhysicsSyncMessage message) {
        if (server == null) {
            LogHelper.severe("broadcasting from client: " + message);
            return;
        }

        message.time = time;
        server.broadcast(message);
    }

    public void send(HostedConnection client, PhysicsSyncMessage message) {
        message.time = time;
        if (client == null) {
            LogHelper.severe("Client is null when sending: " + message);
            return;
        }

        client.send(message);
    }

    public void setMessageTypes(Class... classes) {
        if (server != null) {
            server.removeMessageListener(this);
            server.addMessageListener(this, classes);
        } else if (client != null) {
            client.removeMessageListener(this);
            client.addMessageListener(this, classes);
        }
    }

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

    public float getSyncFrequency() {
        return syncFrequency;
    }

    public void setSyncFrequency(float syncFrequency) {
        this.syncFrequency = syncFrequency;
    }
}
