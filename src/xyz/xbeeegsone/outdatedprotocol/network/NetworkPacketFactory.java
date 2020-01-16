package xyz.xbeeegsone.outdatedprotocol.network;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import org.itxtech.nemisys.network.protocol.mcpe.*;

import java.util.Arrays;

public class NetworkPacketFactory {

    public BedrockPacket translate(DataPacket packet) {
        switch (packet.pid()) {
            case ProtocolInfo.PLAY_STATUS_PACKET:
                return this.build((PlayStatusPacket) packet);
            case ProtocolInfo.DISCONNECT_PACKET:
                return this.build((DisconnectPacket) packet);
            case ProtocolInfo.RESOURCE_PACKS_INFO_PACKET:
                return this.build((ResourcePacksInfoPacket) packet);
            case ProtocolInfo.RESOURCE_PACK_STACK_PACKET:
                return this.build((ResourcePackStackPacket) packet);
            case ProtocolInfo.TEXT_PACKET:
                return this.build((TextPacket) packet);
            default:
                return null;
        }
    }

    private com.nukkitx.protocol.bedrock.packet.PlayStatusPacket build(PlayStatusPacket packet) {
        com.nukkitx.protocol.bedrock.packet.PlayStatusPacket pk = new com.nukkitx.protocol.bedrock.packet.PlayStatusPacket();
        pk.setStatus(com.nukkitx.protocol.bedrock.packet.PlayStatusPacket.Status.values()[packet.status]);

        return pk;
    }

    private com.nukkitx.protocol.bedrock.packet.DisconnectPacket build(DisconnectPacket packet) {
        com.nukkitx.protocol.bedrock.packet.DisconnectPacket pk = new com.nukkitx.protocol.bedrock.packet.DisconnectPacket();
        pk.setKickMessage(packet.message);
        pk.setDisconnectScreenHidden(packet.hideDisconnectionScreen);

        return pk;
    }

    private com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket build(ResourcePacksInfoPacket packet) {
        com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket pk = new com.nukkitx.protocol.bedrock.packet.ResourcePacksInfoPacket();
        pk.setForcedToAccept(packet.mustAccept);
        pk.setScriptingEnabled(false); // ??? not released in Nemisys

        return pk;
    }

    private com.nukkitx.protocol.bedrock.packet.ResourcePackStackPacket build(ResourcePackStackPacket packet) {
        com.nukkitx.protocol.bedrock.packet.ResourcePackStackPacket pk = new com.nukkitx.protocol.bedrock.packet.ResourcePackStackPacket();
        pk.setExperimental(false); // need decoding???
        pk.setForcedToAccept(packet.mustAccept);
        pk.setGameVersion(""); // empty for test

        return pk;
    }

    private com.nukkitx.protocol.bedrock.packet.TextPacket build(TextPacket packet) {
        com.nukkitx.protocol.bedrock.packet.TextPacket pk = new com.nukkitx.protocol.bedrock.packet.TextPacket();
        pk.setType(com.nukkitx.protocol.bedrock.packet.TextPacket.Type.values()[packet.type]);
        pk.setXuid(""); // empty ?
        pk.setSourceName(packet.source);
        pk.setPlatformChatId(""); // ???
        pk.setNeedsTranslation(false); // false for test
        pk.setMessage(packet.message);
        pk.setParameters(Arrays.asList(packet.parameters));

        return pk;
    }

}
