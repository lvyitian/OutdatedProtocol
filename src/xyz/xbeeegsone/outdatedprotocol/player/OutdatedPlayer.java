package xyz.xbeeegsone.outdatedprotocol.player;

import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import org.itxtech.nemisys.Client;
import org.itxtech.nemisys.Player;
import org.itxtech.nemisys.Server;
import org.itxtech.nemisys.network.SourceInterface;
import org.itxtech.nemisys.utils.Skin;
import org.itxtech.nemisys.utils.TextFormat;

import java.util.Collection;
import java.util.UUID;

public class OutdatedPlayer extends Player {

    private String name;
    private Skin skin;
    private UUID uuid;

    private LoginPacket cachedLoginPacket;

    public OutdatedPlayer(SourceInterface interfaz, long clientId, String ip, int port) {
        super(interfaz, clientId, ip, port);
    }

    public void init(String username, Skin skin, UUID uuid, LoginPacket loginPacket) {
        this.name = username;
        this.skin = skin;
        this.uuid = uuid;

        this.cachedLoginPacket = loginPacket;
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
        super.transfer(client);
    }
}
