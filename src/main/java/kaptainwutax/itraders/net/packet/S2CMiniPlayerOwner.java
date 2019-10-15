package kaptainwutax.itraders.net.packet;

import io.netty.buffer.ByteBuf;
import kaptainwutax.itraders.entity.EntityMiniPlayer;
import kaptainwutax.itraders.item.ItemSkullNeck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class S2CMiniPlayerOwner implements IMessage {

    private int entityId;
    private UUID ownerUUID;

    public S2CMiniPlayerOwner() {}

    public S2CMiniPlayerOwner(EntityLivingBase owner, int entityId) {
        this.ownerUUID = owner.getUniqueID();
        this.entityId = entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String uuid = (String) buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8);
        this.ownerUUID = UUID.fromString(uuid);
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String uuid = ownerUUID.toString();
        buf.writeInt(uuid.length());
        buf.writeCharSequence(uuid, StandardCharsets.UTF_8);
        buf.writeInt(entityId);
    }

    public static class S2CMiniPlayerOwnerHandler implements IMessageHandler<S2CMiniPlayerOwner, IMessage> {

        @Override
        public IMessage onMessage(S2CMiniPlayerOwner message, MessageContext ctx) {
            System.out.println("RECEIVED");

            Minecraft minecraft = Minecraft.getMinecraft();

            minecraft.addScheduledTask(() -> {
                EntityPlayerSP player = minecraft.player;
                World world = player.world;
                Entity entity = world.getEntityByID(message.entityId);

                if(entity == null || entity.isDead || !(entity instanceof EntityMiniPlayer))
                    return;

                EntityMiniPlayer miniPlayer = (EntityMiniPlayer) entity;
                miniPlayer.setOwner(message.ownerUUID);
            });

            return null;
        }

    }

}
