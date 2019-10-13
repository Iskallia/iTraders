package kaptainwutax.itraders.item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import kaptainwutax.itraders.Traders;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpawnEggTrader extends ItemSpawnEgg {

	public ItemSpawnEggTrader(String name) {
		super(name, Traders.getResource("trader"));
	}

	@Override
	protected void doSpawning(World world, BlockPos pos, ItemStack stack) {
		Entity entity = spawnCreature(world, getNamedIdFrom(stack), pos.getX() + 0.5D, pos.getY() + 0.5D,
				pos.getZ() + 0.5D);
		if (stack.hasDisplayName())
			entity.setCustomNameTag(stack.getDisplayName());
		ItemMonsterPlacer.applyItemEntityDataToEntity(world, (EntityPlayer) null, stack, entity);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag isAdvanced) {
		if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			tooltip.add("Press shift for more...");
			return;
		}

		if (!stack.hasTagCompound())
			return;
		NBTTagCompound stackTag = stack.getTagCompound();

		if (!stackTag.hasKey("EntityTag"))
			return;
		NBTTagCompound entityTag = stackTag.getCompoundTag("EntityTag");

		if (!entityTag.hasKey("SubData"))
			return;
		NBTTagCompound subTag = entityTag.getCompoundTag("SubData");

		if (subTag.hasKey("Amount")) {
			float amount = subTag.getFloat("Amount");
			StringBuilder sb = new StringBuilder();
			sb.append(TextFormatting.GRAY + "Amount: ");
			sb.append(TextFormatting.YELLOW + String.valueOf(amount) + "$");
			tooltip.add(sb.toString());
		}

		if (subTag.hasKey("Time")) {
			long time = subTag.getLong("Time") * 1000;
			SimpleDateFormat date1 = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat date2 = new SimpleDateFormat("yyyy-MM-dd");
			date1.setTimeZone(TimeZone.getTimeZone("UTC"));
			date2.setTimeZone(TimeZone.getTimeZone("UTC"));

			StringBuilder sb = new StringBuilder();
			sb.append(TextFormatting.GRAY + "Time: ");
			sb.append(TextFormatting.YELLOW + date1.format(new Date(time)));
			sb.append(" ");
			sb.append(TextFormatting.YELLOW + date2.format(new Date(time)));
			tooltip.add(sb.toString());
		}
	}

}
