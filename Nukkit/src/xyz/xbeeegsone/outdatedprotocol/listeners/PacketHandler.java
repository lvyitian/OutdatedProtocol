package xyz.xbeeegsone.outdatedprotocol.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.network.protocol.*;
import cn.nukkit.utils.TextFormat;
import xyz.xbeeegsone.outdatedprotocol.NukkitPlugin;
import xyz.xbeeegsone.outdatedprotocol.event.OutdatedPlayerLoginEvent;
import xyz.xbeeegsone.outdatedprotocol.network.ContainerSetSlotPacket;
import xyz.xbeeegsone.outdatedprotocol.network.ExtraLoginPacket;

public class PacketHandler implements Listener {

    @EventHandler
    public void onReceive(DataPacketReceiveEvent event) {
        DataPacket dataPacket = event.getPacket();
        Player player = event.getPlayer();
        switch (dataPacket.pid()) {
            case ExtraLoginPacket.NETWORK_ID:
                ExtraLoginPacket extraLoginPacket = (ExtraLoginPacket) dataPacket;
                if(extraLoginPacket.protocol < ProtocolInfo.CURRENT_PROTOCOL) {
                    OutdatedPlayerLoginEvent outdatedPlayerLoginEvent = new OutdatedPlayerLoginEvent(player);
                    String info = extraLoginPacket.username + " [Protocol: " + extraLoginPacket.protocol + ", ClientID: " + extraLoginPacket.clientId + ", ClientUUID: " + extraLoginPacket.clientUUID + "]";
                    Server.getInstance().getPluginManager().callEvent(outdatedPlayerLoginEvent);
                    if(outdatedPlayerLoginEvent.isCancelled()) {
                        Server.getInstance().getLogger().error("Something is going wrong... " + TextFormat.WHITE + info);
                    } else {
                        NukkitPlugin.getPlugin().outdatedPlayers.add(player);
                        Server.getInstance().getLogger().debug("New outdated client " + info);
                    }
                }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSend(DataPacketSendEvent event) {
        DataPacket dataPacket = event.getPacket();
        Player player = event.getPlayer();
        if(!NukkitPlugin.getPlugin().outdatedPlayers.contains(player)) return;
        switch (dataPacket.pid()) {
            case ProtocolInfo.INVENTORY_SLOT_PACKET:
                InventorySlotPacket inventorySlotPacket = (InventorySlotPacket) dataPacket;
                ContainerSetSlotPacket containerSetSlotPacket = new ContainerSetSlotPacket();
                containerSetSlotPacket.item = inventorySlotPacket.item;
                containerSetSlotPacket.slot = inventorySlotPacket.slot;
                containerSetSlotPacket.windowid = inventorySlotPacket.inventoryId;
                containerSetSlotPacket.hotbarSlot = 0;
                containerSetSlotPacket.selectedSlot = player.getInventory().getHeldItemSlot();
                break;
        }
    }

}
