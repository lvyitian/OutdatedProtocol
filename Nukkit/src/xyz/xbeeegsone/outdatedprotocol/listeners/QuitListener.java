package xyz.xbeeegsone.outdatedprotocol.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import xyz.xbeeegsone.outdatedprotocol.NukkitPlugin;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(NukkitPlugin.getPlugin().outdatedPlayers.contains(player)) {
            NukkitPlugin.getPlugin().outdatedPlayers.remove(player);
        }
    }

}
