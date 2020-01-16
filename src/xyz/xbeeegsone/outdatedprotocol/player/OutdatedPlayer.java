package xyz.xbeeegsone.outdatedprotocol.player;

import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import org.itxtech.nemisys.Player;
import org.itxtech.nemisys.network.SourceInterface;
import org.itxtech.nemisys.utils.Skin;

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
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }
}
