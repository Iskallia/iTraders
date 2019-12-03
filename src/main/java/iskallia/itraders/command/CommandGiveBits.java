package iskallia.itraders.command;

import iskallia.itraders.init.InitItem;
import iskallia.itraders.item.ItemBit;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class CommandGiveBits extends CommandBase {

    public static final List<ItemBit> BIT_ITEMS = new LinkedList<>();

    static {
        BIT_ITEMS.add(InitItem.BIT_10000);
        BIT_ITEMS.add(InitItem.BIT_5000);
        BIT_ITEMS.add(InitItem.BIT_1000);
        BIT_ITEMS.add(InitItem.BIT_100);
        BIT_ITEMS.sort(Comparator.comparingInt(b -> -b.value)); // Sort by decreasing bitItem::value
        // ^^^ Here to make this thing fail-safe
    }

    @Nonnull
    @Override
    public String getName() {
        return "givebits";
    }

    @Nonnull
    @Override
    public String getUsage(ICommandSender sender) {
        return getName() + "<nickname> <bit_amount>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 2)
            return; // Requires exactly 2 parameters: <nickname> <bit_amount>

        // Fetch arguments
        String nickname = args[0];
        int bitsInput = parseInt(args[1]);

        // Fetch player from player list
        EntityPlayerMP player = getPlayer(server, sender, nickname);
        System.out.println("For " + player);

        List<ItemStack> itemsToGive = new LinkedList<>();

        for (ItemBit bitItem : BIT_ITEMS) {
            if (bitsInput < bitItem.value) continue;

            int amount = bitsInput / bitItem.value;

            itemsToGive.add(new ItemStack(bitItem, amount));

            bitsInput %= bitItem.value;
        }

        for (ItemStack itemStack : itemsToGive) {
            boolean added = player.inventory.addItemStackToInventory(itemStack);

            if (!added) {
                player.dropItem(itemStack, false, false);
            }
        }

    }

}
