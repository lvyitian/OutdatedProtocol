package xyz.xbeeegsone.outdatedprotocol;

import org.itxtech.nemisys.network.protocol.mcpe.ProtocolInfo;
import org.itxtech.nemisys.plugin.PluginBase;
import xyz.xbeeegsone.outdatedprotocol.network.ExtraLoginPacket;

public class NemisysPlugin extends PluginBase {

    private static NemisysPlugin plugin;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        this.getServer().getNetwork().registerPacket(ProtocolInfo.LOGIN_PACKET, ExtraLoginPacket.class);
    }

    public static NemisysPlugin getPlugin(){
        return plugin;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
