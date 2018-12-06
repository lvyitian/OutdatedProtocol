package xyz.xbeeegsone.outdatedprotocol;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import xyz.xbeeegsone.outdatedprotocol.listeners.PacketHandler;
import xyz.xbeeegsone.outdatedprotocol.listeners.QuitListener;
import xyz.xbeeegsone.outdatedprotocol.network.ContainerSetSlotPacket;
import xyz.xbeeegsone.outdatedprotocol.network.ExtraLoginPacket;
import xyz.xbeeegsone.outdatedprotocol.network.TextPacket;

import java.util.ArrayList;
import java.util.List;

public class NukkitPlugin extends PluginBase {

    private static NukkitPlugin plugin;
    public List<Player> outdatedPlayers = new ArrayList<>();

    @Override
    public void onLoad() {
        this.getServer().getLogger().warning("Enabling outdated protocol support...");
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.registerPackets();
        this.getServer().getNetwork().setName(this.getServer().getNetwork().getName() + " v.1.0.x");
        setServerSettings();
        PluginInfo();
    }

    public void PluginInfo() {
        this.getServer().getLogger().notice("Outdated protocol support enabled!");
        this.getServer().getLogger().info(TextFormat.colorize("&fServer Protocol: &e" + ProtocolInfo.CURRENT_PROTOCOL));
        this.getServer().getLogger().info(TextFormat.colorize("&fAdded support for: &c[1.0 - 1.1.7]"));
    }

    public void registerPackets() {
        this.getServer().getNetwork().registerPacket(ProtocolInfo.LOGIN_PACKET, ExtraLoginPacket.class); //fixed 0x01 or LoginPacket
        this.getServer().getNetwork().registerPacket(ProtocolInfo.INVENTORY_SLOT_PACKET, ContainerSetSlotPacket.class);
        this.getServer().getNetwork().registerPacket(ProtocolInfo.TEXT_PACKET, TextPacket.class);
    }

    public void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PacketHandler(), this);
        this.getServer().getPluginManager().registerEvents(new QuitListener(), this);
    }

    public void setServerSettings() {
        this.getServer().setPropertyBoolean("xbox-auth", false);
        this.getServer().getLogger().setLogDebug(true);
    }

    public static NukkitPlugin getPlugin(){
        return plugin;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
