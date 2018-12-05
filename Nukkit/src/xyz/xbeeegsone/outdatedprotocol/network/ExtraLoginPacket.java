package xyz.xbeeegsone.outdatedprotocol.network;

import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ExtraLoginPacket extends LoginPacket {

    private String username;
    public int protocol;
    private UUID clientUUID;
    private long clientId;
    private byte gameEdition = 0;

    private Skin skin;

    private byte[] cacheBuffer;

    @Override
    public byte pid() {
        return ProtocolInfo.LOGIN_PACKET;
    }

    @Override
    public void decode() {
        this.cacheBuffer = this.getBuffer();

        this.protocol = this.getInt();
        super.protocol = this.protocol;

        if(this.protocol <= 113) {
            this.gameEdition = (byte) this.getByte();
        } else if (this.protocol >= 0xffff) {
                this.offset -= 6;
                this.protocol = this.getInt();
                this.offset += 1;
        }
        this.setBuffer(this.getByteArray(), 0);

        decodeChainData();
        decodeSkinData();

        Server.getInstance().getLogger().info("Logged " + this.username + " [Protocol: " + this.protocol + ", ClientID: " + this.clientId + ", ClientUUID: " + this.clientUUID + "]");
        super.protocol = ProtocolInfo.CURRENT_PROTOCOL;
    }

    private void decodeChainData() {
        Map<String, List<String>> map = new Gson().fromJson(new String(this.get(this.getLInt()), StandardCharsets.UTF_8),
                new TypeToken<Map<String, List<String>>>() {
                }.getType());
        if (map.isEmpty() || !map.containsKey("chain") || map.get("chain").isEmpty()) return;
        List<String> chains = map.get("chain");
        for (String c : chains) {
            JsonObject chainMap = decodeToken(c);
            if (chainMap == null) continue;
            if (chainMap.has("extraData")) {
                JsonObject extra = chainMap.get("extraData").getAsJsonObject();
                if (extra.has("displayName")) {
                    this.username = extra.get("displayName").getAsString();
                    super.username = this.username;
                }
                if (extra.has("identity")) {
                    this.clientUUID = UUID.fromString(extra.get("identity").getAsString());
                    super.clientUUID = this.clientUUID;
                }
            }
        }
    }

    private void decodeSkinData() {
        JsonObject skinToken = decodeToken(new String(this.get(this.getLInt())));
        if (skinToken.has("ClientRandomId")) {
            this.clientId = skinToken.get("ClientRandomId").getAsLong();
            super.clientId = this.clientId;
        }
        skin = new Skin();
        if (skinToken.has("SkinId")) {
            skin.setSkinId(skinToken.get("SkinId").getAsString());
        }
        if (skinToken.has("SkinData")) {
            skin.setSkinData(Base64.getDecoder().decode(skinToken.get("SkinData").getAsString()));
        }

        if (skinToken.has("CapeData")) {
            this.skin.setCapeData(Base64.getDecoder().decode(skinToken.get("CapeData").getAsString()));
        }

        if (skinToken.has("SkinGeometryName")) {
            skin.setGeometryName(skinToken.get("SkinGeometryName").getAsString());
        }

        if (skinToken.has("SkinGeometry")) {
            skin.setGeometryData(new String(Base64.getDecoder().decode(skinToken.get("SkinGeometry").getAsString()), StandardCharsets.UTF_8));
        }

        super.skin = skin;
    }

    private JsonObject decodeToken(String token) {
        String[] base = token.split("\\.");
        if (base.length < 2) return null;


        return new Gson().fromJson(new String(Base64.getDecoder().decode(base[1].replaceAll("-", "+").replaceAll("_", "/")), StandardCharsets.UTF_8), JsonObject.class);
    }

    @Override
    public Skin getSkin() {
        return this.skin;
    }

}
