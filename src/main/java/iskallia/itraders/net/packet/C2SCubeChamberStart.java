package iskallia.itraders.net.packet;

import io.netty.buffer.ByteBuf;
import iskallia.itraders.block.entity.TileEntityCubeChamber;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class C2SCubeChamberStart implements IMessage {

    private int x, y, z;

    public C2SCubeChamberStart() {}

    public C2SCubeChamberStart(TileEntityCubeChamber cubeChamber) {
        BlockPos pos = cubeChamber.getPos();

        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    /* -------------------------------------- */

    public static class C2SCubeChamberStartHandler implements IMessageHandler<C2SCubeChamberStart, IMessage> {

        @Override
        public IMessage onMessage(C2SCubeChamberStart message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            WorldServer world = player.getServerWorld();

            TileEntity tileEntity = world.getTileEntity(new BlockPos(message.x, message.y, message.z));

            if (tileEntity instanceof TileEntityCubeChamber) {
                TileEntityCubeChamber cubeChamber = (TileEntityCubeChamber) tileEntity;
                cubeChamber.startPressed();
            }

            return null;
        }

    }

}
