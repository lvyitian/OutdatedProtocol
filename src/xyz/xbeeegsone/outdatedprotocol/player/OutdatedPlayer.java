package xyz.xbeeegsone.outdatedprotocol.player;

import org.itxtech.nemisys.Player;
import org.itxtech.nemisys.network.SourceInterface;
import org.itxtech.nemisys.network.protocol.mcpe.LoginPacket;
import org.itxtech.nemisys.utils.Skin;
import xyz.xbeeegsone.outdatedprotocol.multiversion.ProtocolGroup;

import java.util.UUID;

public class OutdatedPlayer extends Player {

    private String username;
    private UUID uuid;
    private Skin skin;

    private LoginPacket cachedLoginPacket;
    private ProtocolGroup protocolGroup;

    public OutdatedPlayer(SourceInterface interfaz, long clientId, String ip, int port, String username, UUID uuid, Skin skin, LoginPacket cachedLoginPacket, ProtocolGroup protocolGroup) {
        super(interfaz, clientId, ip, port);

        this.username = username;
        this.uuid = uuid;
        this.skin = skin;

        this.cachedLoginPacket = cachedLoginPacket;
        this.protocolGroup = protocolGroup;
    }

    @Override
    public String getName() {
        return this.username;
    }

    @Override
    public Skin getSkin() {
        return this.skin;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    public LoginPacket getCachedLoginPacket() {
        return this.cachedLoginPacket;
    }
}
