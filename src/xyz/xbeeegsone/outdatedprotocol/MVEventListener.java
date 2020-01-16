package xyz.xbeeegsone.outdatedprotocol;

import com.nukkitx.protocol.bedrock.BedrockPong;
import com.nukkitx.protocol.bedrock.BedrockServerEventHandler;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.v361.Bedrock_v361;
import org.itxtech.nemisys.Server;
import xyz.xbeeegsone.outdatedprotocol.network.LoginSession;

import java.net.InetSocketAddress;

public class MVEventListener implements BedrockServerEventHandler {

    private BedrockPong pong = new BedrockPong();

    MVEventListener(int port) {
        pong.setEdition("MCPE");
        pong.setGameType("Default");
        pong.setNintendoLimited(false);
        pong.setProtocolVersion(Bedrock_v361.V361_CODEC.getProtocolVersion());
        pong.setIpv4Port(port);

        pong.setPlayerCount(0);
        pong.setMaximumPlayerCount(Server.getInstance().getMaxPlayers());
        pong.setMotd(Server.getInstance().getMotd());
        pong.setSubMotd("OutdatedProtocol [1.12 - 1.14]");
    }

    @Override
    public boolean onConnectionRequest(InetSocketAddress address) {
        return true;
    }

    @Override
    public BedrockPong onQuery(InetSocketAddress address) {
        return pong;
    }

    @Override
    public void onSessionCreation(BedrockServerSession session) {
        session.setPacketHandler(new LoginSession(session));
    }
}
