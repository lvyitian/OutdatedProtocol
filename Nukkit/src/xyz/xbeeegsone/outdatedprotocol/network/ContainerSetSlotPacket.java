package xyz.xbeeegsone.outdatedprotocol.network;

import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.InventorySlotPacket;
import cn.nukkit.network.protocol.ProtocolInfo;

public class ContainerSetSlotPacket extends InventorySlotPacket {

    public static final byte NETWORK_ID = ProtocolInfo.INVENTORY_SLOT_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public int windowid;
    public int slot;
    public int hotbarSlot;
    public Item item;
    public int selectedSlot;

    @Override
    public void decode() {
        this.windowid = this.getByte();
        this.slot = this.getVarInt();
        this.hotbarSlot = this.getVarInt();
        this.item = this.getSlot();
        this.selectedSlot = this.getByte();

        super.inventoryId = this.windowid;
        super.slot = this.slot;
        super.item = this.item;
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte((byte) this.windowid);
        this.putVarInt(this.slot);
        this.putVarInt(this.hotbarSlot);
        this.putSlot(this.item);
        this.putByte((byte) this.selectedSlot);

        super.inventoryId = this.windowid;
        super.slot = this.slot;
        super.item = this.item;
    }

}
