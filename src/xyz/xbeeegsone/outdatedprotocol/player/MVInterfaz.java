package xyz.xbeeegsone.outdatedprotocol.player;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import org.itxtech.nemisys.Player;
import org.itxtech.nemisys.Server;
import org.itxtech.nemisys.network.SourceInterface;
import org.itxtech.nemisys.network.protocol.mcpe.DataPacket;
import xyz.xbeeegsone.outdatedprotocol.network.NetworkPacketFactory;

public class MVInterfaz implements SourceInterface {

    private BedrockServerSession session;
    private NetworkPacketFactory factory = new NetworkPacketFactory();

    public MVInterfaz(BedrockServerSession session) {
        this.session = session;
    }

    @Override
    public Integer putPacket(Player player, DataPacket dataPacket) {
        return this.putPacket(player, dataPacket, true);
    }

    @Override
    public Integer putPacket(Player player, DataPacket dataPacket, boolean b) {
        return this.putPacket(player, dataPacket, true);
    }

    @Override
    public Integer putPacket(Player player, DataPacket dataPacket, boolean b, boolean b1) {
        BedrockPacket packet = this.factory.translate(dataPacket);
        if(packet != null) {
            this.session.sendPacket(packet);
        } else {
            Server.getInstance().getLogger().alert("unknown packet with pid <" + dataPacket.pid() + ">");
        }
        return 0;
    }

    @Override
    public int getNetworkLatency(Player player) {
        return 0;
    }

    @Override
    public void close(Player player) {
        session.disconnect();
    }

    @Override
    public void close(Player player, String s) {
        session.disconnect(s);
    }

    @Override
    public void setName(String s) {
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void emergencyShutdown() {

    }
}
