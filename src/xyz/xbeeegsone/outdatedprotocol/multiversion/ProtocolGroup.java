package xyz.xbeeegsone.outdatedprotocol.multiversion;

import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.v361.Bedrock_v361;
import com.nukkitx.protocol.bedrock.v388.Bedrock_v388;
import com.nukkitx.protocol.bedrock.v389.Bedrock_v389;

public enum ProtocolGroup {
    UNKNOWN(0, 0, Bedrock_v388.V388_CODEC),
    PROTOCOL_1_12(361, 369, Bedrock_v361.V361_CODEC),
    PROTOCOL_1_13(388, 388, Bedrock_v388.V388_CODEC),
    PROTOCOL_1_14(389, 389, Bedrock_v389.V389_CODEC);


    private final int minProtocol;
    private final int maxProtocol;
    private final BedrockPacketCodec codec;


    ProtocolGroup(int minProtocol, int maxProtocol, BedrockPacketCodec codec) {
        this.minProtocol = minProtocol;
        this.maxProtocol = maxProtocol;
        this.codec = codec;
    }

    public static ProtocolGroup from(int protocol) {
        for (ProtocolGroup group : values()) {
            if (protocol >= group.minProtocol && protocol <= group.maxProtocol) {
                return group;
            }
        }

        return UNKNOWN;
    }

    public BedrockPacketCodec getCodec() {
        return codec;
    }
}
