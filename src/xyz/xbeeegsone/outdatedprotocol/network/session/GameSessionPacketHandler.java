package xyz.xbeeegsone.outdatedprotocol.network.session;

import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.TickSyncPacket;

public class GameSessionPacketHandler implements BedrockPacketHandler {

    @Override
    public boolean handle(TickSyncPacket packet) {
        return true;
    }
}
