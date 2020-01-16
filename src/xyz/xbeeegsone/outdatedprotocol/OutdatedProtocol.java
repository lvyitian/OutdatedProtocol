package xyz.xbeeegsone.outdatedprotocol;

import com.nukkitx.protocol.bedrock.BedrockServer;
import org.itxtech.nemisys.network.RakNetInterface;
import org.itxtech.nemisys.network.SourceInterface;
import org.itxtech.nemisys.plugin.PluginBase;
import org.itxtech.nemisys.utils.TextFormat;

import java.net.InetSocketAddress;

public class OutdatedProtocol extends PluginBase {

    private BedrockServer server;

    @Override
    public void onLoad() {
        this.getServer().getLogger().warning("Disabling default RakNet interface...");
        for (SourceInterface sourceInterface : this.getServer().getNetwork().getInterfaces()) {
            if (sourceInterface instanceof RakNetInterface) {
                sourceInterface.shutdown();
            }
        }
        this.getServer().getLogger().notice("Default RakNet disabled. Starting MVServer...");
    }

    @Override
    public void onEnable() {
        int port = this.getServer().getPort();
        server = new BedrockServer(new InetSocketAddress("0.0.0.0", port));
        server.setHandler(new MVEventListener(port));

        server.bind().whenComplete((aVoid, throwable) -> {
            if (throwable == null) {
                this.getLogger().info("RakNet server started on " + port);
            } else {
                this.getLogger().error("RakNet server failed to bind to " + throwable.getMessage());
                this.getServer().getPluginManager().disablePlugin(this);
            }
        }).join();

        this.getLogger().info(TextFormat.colorize(
                "\n\n &eOutdated Protocol &asupport enabled!\n &6Supporting &f[&a1.12 &f- &a1.14&f]\n"
        ));
    }

    @Override
    public void onDisable() {
        server.close();
    }

}
