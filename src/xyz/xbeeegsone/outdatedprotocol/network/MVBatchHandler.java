package xyz.xbeeegsone.outdatedprotocol.network;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.handler.BatchHandler;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import io.netty.buffer.ByteBuf;
import org.itxtech.nemisys.Server;

import java.util.Collection;

public class MVBatchHandler implements BatchHandler {
    @Override
    public void handle(BedrockSession session, ByteBuf compressed, Collection<BedrockPacket> packets) {
        for (BedrockPacket packet : packets) {
            if (session.isLogging()) {
                Server.getInstance().getLogger().debug("Inbound " + session.getAddress() + ":" + packet);
            }

            BedrockPacketHandler handler = session.getPacketHandler();
            if (handler == null || !packet.handle(handler)) {
                Server.getInstance().getLogger().debug("Unhandled packet for " + session.getAddress() + ":" + packet);
            }
        }
    }
}
