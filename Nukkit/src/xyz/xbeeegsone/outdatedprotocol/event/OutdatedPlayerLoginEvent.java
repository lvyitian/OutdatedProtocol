package xyz.xbeeegsone.outdatedprotocol.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.player.PlayerEvent;

public class OutdatedPlayerLoginEvent extends PlayerEvent implements Cancellable {

    private Player player;

    public OutdatedPlayerLoginEvent(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
