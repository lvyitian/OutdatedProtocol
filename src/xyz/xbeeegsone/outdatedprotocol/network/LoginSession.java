package xyz.xbeeegsone.outdatedprotocol.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.packet.TickSyncPacket;
import com.nukkitx.protocol.bedrock.v389.Bedrock_v389;
import org.itxtech.nemisys.Player;
import org.itxtech.nemisys.Server;
import org.itxtech.nemisys.event.player.PlayerCreationEvent;
import org.itxtech.nemisys.scheduler.AsyncTask;
import org.itxtech.nemisys.utils.SerializedImage;
import org.itxtech.nemisys.utils.Skin;
import org.itxtech.nemisys.utils.SkinAnimation;
import xyz.xbeeegsone.outdatedprotocol.multiversion.ProtocolGroup;
import xyz.xbeeegsone.outdatedprotocol.player.MVInterfaz;
import xyz.xbeeegsone.outdatedprotocol.player.OutdatedPlayer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LoginSession implements BedrockPacketHandler {

    private final BedrockServerSession session;

    private String username;
    private long clientId;
    private Skin skin;

    private AsyncTask loginTask;
    private byte[] cachedLoginPacket = new byte[]{};
    private UUID clientUUID;
    private ProtocolGroup protocolGroup;

    public LoginSession(BedrockServerSession session) {
        this.session = session;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        Server.getInstance().getLogger().info("protocol: " + packet.getProtocolVersion());
        this.protocolGroup = ProtocolGroup.from(packet.getProtocolVersion());
        this.session.setPacketCodec(this.protocolGroup.getCodec());
        this.decodeChainData(packet);
        this.decodeSkinData(packet);

        //this.cachedLoginPacket = Bedrock_v389.V389_CODEC.tryEncode(packet).array();
        //this.createPlayer(this.session);
        return true;
    }

    private void decodeChainData(LoginPacket packet) {
        Map<String, List<String>> map = new Gson().fromJson(String.valueOf(packet.getChainData()), new TypeToken<Map<String, List<String>>>() {}.getType());
        if (map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty()) return;
        List<String> chains = map.get("chain");
        for (String c : chains) {
            JsonObject chainMap = decodeToken(c);
            if (chainMap == null) continue;
            if (chainMap.has("extraData")) {
                JsonObject extra = chainMap.get("extraData").getAsJsonObject();
                if (extra.has("displayName")) this.username = extra.get("displayName").getAsString();
                if (extra.has("identity")) this.clientUUID = UUID.fromString(extra.get("identity").getAsString());
            }
        }
    }

    private void decodeSkinData(LoginPacket packet) {
        JsonObject skinToken = decodeToken(String.valueOf(packet.getSkinData()));
        if (skinToken.has("ClientRandomId")) this.clientId = skinToken.get("ClientRandomId").getAsLong();
        skin = new Skin();
        if (skinToken.has("SkinId")) {
            skin.setSkinId(skinToken.get("SkinId").getAsString());
        }
        if (skinToken.has("CapeId")) {
            skin.setSkinId(skinToken.get("CapeId").getAsString());
        }

        skin.setSkinData(getImage(skinToken, "Skin"));
        skin.setCapeData(getImage(skinToken, "Cape"));
        if (skinToken.has("PremiumSkin")) {
            skin.setPremium(skinToken.get("PremiumSkin").getAsBoolean());
        }
        if (skinToken.has("PersonaSkin")) {
            skin.setPersona(skinToken.get("PersonaSkin").getAsBoolean());
        }
        if (skinToken.has("CapeOnClassicSkin")) {
            skin.setCapeOnClassic(skinToken.get("CapeOnClassicSkin").getAsBoolean());
        }

        if (skinToken.has("SkinResourcePatch")) {
            skin.setSkinResourcePatch(new String(Base64.getDecoder().decode(skinToken.get("SkinResourcePatch").getAsString()), StandardCharsets.UTF_8));
        }

        if (skinToken.has("SkinGeometryData")) {
            skin.setGeometryData(new String(Base64.getDecoder().decode(skinToken.get("SkinGeometryData").getAsString()), StandardCharsets.UTF_8));
        }

        if (skinToken.has("AnimationData")) {
            skin.setGeometryData(new String(Base64.getDecoder().decode(skinToken.get("AnimationData").getAsString()), StandardCharsets.UTF_8));
        }

        if (skinToken.has("AnimatedImageData")) {
            JsonArray array = skinToken.get("AnimatedImageData").getAsJsonArray();
            for (JsonElement element : array) {
                skin.getAnimations().add(getAnimation(element.getAsJsonObject()));
            }
        }
    }

    private static SkinAnimation getAnimation(JsonObject element) {
        float frames = element.get("Frames").getAsFloat();
        int type = element.get("Type").getAsInt();
        byte[] data = Base64.getDecoder().decode(element.get("Image").getAsString());
        int width = element.get("ImageWidth").getAsInt();
        int height = element.get("ImageHeight").getAsInt();
        return new SkinAnimation(new SerializedImage(width, height, data), type, frames);
    }

    private static SerializedImage getImage(JsonObject token, String name) {
        if (token.has(name + "Data")) {
            byte[] skinImage = Base64.getDecoder().decode(token.get(name + "Data").getAsString());
            if (token.has(name + "ImageHeight") && token.has(name + "ImageWidth")) {
                int width = token.get(name + "ImageWidth").getAsInt();
                int height = token.get(name + "ImageHeight").getAsInt();
                return new SerializedImage(width, height, skinImage);
            } else {
                return SerializedImage.fromLegacy(skinImage);
            }
        }
        return SerializedImage.EMPTY;
    }

    private JsonObject decodeToken(String token) {
        String[] base = token.split("\\.");
        if (base.length < 2) return null;


        return new Gson().fromJson(new String(Base64.getDecoder().decode(base[1].replaceAll("-", "+").replaceAll("_", "/")), StandardCharsets.UTF_8), JsonObject.class);
    }

    public Skin getSkin() {
        return this.skin;
    }

    private void createPlayer(BedrockServerSession session) {
        MVInterfaz interfaz = new MVInterfaz(session);
        PlayerCreationEvent event = new PlayerCreationEvent(interfaz, OutdatedPlayer.class, OutdatedPlayer.class, this.clientId, session.getAddress().getHostString(), session.getAddress().getPort());
        Server.getInstance().getPluginManager().callEvent(event);

        Class<? extends Player> clazz = event.getPlayerClass();
        try {
            Constructor constructor = clazz.getConstructor(MVInterfaz.class, Long.class, String.class, int.class, String.class, UUID.class, Skin.class, byte[].class, ProtocolGroup.class);
            OutdatedPlayer player = (OutdatedPlayer) constructor.newInstance(interfaz, this.clientId, event.getAddress(), event.getPort(), this.username, this.clientUUID, this.skin, this.cachedLoginPacket, this.protocolGroup);

            Server.getInstance().addPlayer(UUID.randomUUID().toString(), player);

            session.setPacketHandler(new GameSessionPacketHandler());
            //session.setBatchedHandler(new MVBatchHandler());
            session.addDisconnectHandler((reason) -> {
                Server.getInstance().removePlayer(player);
                Server.getInstance().getLogger().info("Disconnected " + session.getAddress().getHostString() + ":" + session.getAddress().getPort());
            });
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            Server.getInstance().getLogger().logException(e);
            session.setPacketCodec(Bedrock_v389.V389_CODEC);
            session.disconnect(e.getMessage());
        }
    }
}
