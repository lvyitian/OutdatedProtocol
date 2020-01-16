package xyz.xbeeegsone.outdatedprotocol.player;

import org.itxtech.nemisys.Client;
import org.itxtech.nemisys.Player;
import org.itxtech.nemisys.Server;
import org.itxtech.nemisys.event.player.PlayerTransferEvent;
import org.itxtech.nemisys.network.SourceInterface;
import org.itxtech.nemisys.network.protocol.spp.PlayerLoginPacket;
import org.itxtech.nemisys.utils.Skin;
import org.itxtech.nemisys.utils.TextFormat;

import java.util.Collection;
import java.util.UUID;

public class OutdatedPlayer extends Player {

    private String name;
    private Skin skin;
    private UUID uuid;

    private byte[] cachedLoginPacket;
    private Client client;
    private boolean isFirstTimeLogin = true;

    public OutdatedPlayer(SourceInterface interfaz, long clientId, String ip, int port) {
        super(interfaz, clientId, ip, port);
    }

    public void init(String username, Skin skin, UUID uuid, byte[] cachedLoginPacket) {
        this.name = username;
        this.skin = skin;
        this.uuid = uuid;

        this.cachedLoginPacket = cachedLoginPacket;
        Server.getInstance().getLogger().info(Server.getInstance().getLanguage().translateString("nemisys.player.logIn", new String[]{TextFormat.AQUA + username + TextFormat.WHITE, this.getIp(), String.valueOf(this.getPort()), "" + TextFormat.GREEN + this.getClientId() + TextFormat.WHITE}));
        this.connect();
    }

    private void connect() {
        Collection<Client> clients = Server.getInstance().getMainClients().values();
        if(clients.isEmpty()) {
            this.close("Synapse Server: " + TextFormat.RED + "Target servers is not online!");
        } else {
            for (Client client : clients) {
                this.transfer(client);
                return;
            }
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public void transfer(Client client) {
        PlayerTransferEvent ev;
        this.getServer().getPluginManager().callEvent(ev = new PlayerTransferEvent(this, client));
        if (!ev.isCancelled()) {
            if (this.getClient() != null) {
                this.getClient().removePlayer(this, "Player has been transferred");
                this.removeAllPlayers();
                this.despawnEntities();
                this.removeScoreboards();
            }
            this.client = ev.getTargetClient();
            this.client.addPlayer(this);

            PlayerLoginPacket pk = new PlayerLoginPacket();
            pk.uuid = this.uuid;
            pk.address = this.getIp();
            pk.port = this.getPort();
            pk.isFirstTime = this.isFirstTimeLogin;
            pk.cachedLoginPacket = this.cachedLoginPacket;

            this.client.sendDataPacket(pk);

            this.isFirstTimeLogin = false;

            this.getServer().getLogger().info(this.name + " has been transferred to " + this.client.getDescription());
            this.getServer().updateClientData();
        }
    }

    @Override
    public Client getClient() {
        return this.client;
    }
}
