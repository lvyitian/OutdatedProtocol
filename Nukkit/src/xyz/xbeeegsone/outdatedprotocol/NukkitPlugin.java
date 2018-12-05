package xyz.xbeeegsone.outdatedprotocol;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.plugin.PluginBase;
import xyz.xbeeegsone.outdatedprotocol.listeners.PacketHandler;
import xyz.xbeeegsone.outdatedprotocol.network.ExtraLoginPacket;

public class NukkitPlugin extends PluginBase {

    private static NukkitPlugin plugin;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new PacketHandler(), this);
        this.getServer().getNetwork().registerPacket(ProtocolInfo.LOGIN_PACKET, ExtraLoginPacket.class);
    }

    public static NukkitPlugin getPlugin(){
        return plugin;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
