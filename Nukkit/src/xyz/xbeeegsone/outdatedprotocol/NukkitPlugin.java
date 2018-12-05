package xyz.xbeeegsone.outdatedprotocol;

import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import xyz.xbeeegsone.outdatedprotocol.listeners.PacketHandler;
import xyz.xbeeegsone.outdatedprotocol.network.ExtraLoginPacket;

public class NukkitPlugin extends PluginBase {

    private static NukkitPlugin plugin;

    @Override
    public void onLoad() {
        this.getServer().getLogger().warning("Enabling outdated protocol support...");
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new PacketHandler(), this);
        this.getServer().getNetwork().registerPacket(ProtocolInfo.LOGIN_PACKET, ExtraLoginPacket.class);
        this.getServer().setPropertyBoolean("xbox-auth", false);
        this.getServer().getLogger().notice("Outdated protocol support enabled!");
        this.getServer().getLogger().info(TextFormat.colorize("&fServer Protocol: &e" + ProtocolInfo.CURRENT_PROTOCOL));
        this.getServer().getLogger().info(TextFormat.colorize("&fAdded support for: &c[1.0 - 1.1.7]"));
    }

    public static NukkitPlugin getPlugin(){
        return plugin;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
