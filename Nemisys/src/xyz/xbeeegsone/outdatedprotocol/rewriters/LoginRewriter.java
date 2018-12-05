package xyz.xbeeegsone.outdatedprotocol.rewriters;

import org.itxtech.nemisys.utils.Skin;

import java.util.UUID;

public class LoginRewriter {

    private String username;
    private int protocol;
    private UUID clientUUID;
    private long clientId;
    private Skin skin;

    public LoginRewriter(String username, int protocol, UUID clientUUID, long clientId, Skin skin) {
        this.username = username;
        this.protocol = protocol;
        this.clientUUID = clientUUID;
        this.clientId = clientId;
        this.skin = skin;
    }

    public byte[] createCacheBuffer() {
        return new byte[]{0};
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public UUID getClientUUID() {
        return clientUUID;
    }

    public void setClientUUID(UUID clientUUID) {
        this.clientUUID = clientUUID;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
