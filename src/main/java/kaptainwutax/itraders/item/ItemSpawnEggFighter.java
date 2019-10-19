package kaptainwutax.itraders.item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.entity.EntityFighter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpawnEggFighter extends ItemSpawnEgg<EntityFighter> {

	public ItemSpawnEggFighter(String name) {
		super(name, Traders.getResource("fighter"), true);
	}

	@Override
	public EntityFighter onPlayerSpawn(World world, EntityPlayer player, BlockPos pos, ItemStack stack) {
		if (stack.hasDisplayName() && !shouldSpawnEntity(stack.getTagCompound())) {
			ItemStack headDrop = new ItemStack(Items.SKULL, 1, 3);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("SkullOwner", stack.getDisplayName());
			headDrop.setTagCompound(nbt);
			EntityItem itemItem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, headDrop);
			world.spawnEntity(itemItem);
		} else {
			EntityFighter fighter = this.spawnCreature(world, getNamedIdFrom(stack), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
			if (stack.hasDisplayName())fighter.setCustomNameTag(stack.getDisplayName());
			ItemMonsterPlacer.applyItemEntityDataToEntity(world, (EntityPlayer) null, stack, fighter);
			return fighter;
		}
		
		return null;
	}
	
	@Override
	protected EntityFighter onDispenserSpawn(World world, EnumFacing facing, BlockPos pos, ItemStack stack) {
		EntityFighter fighter = this.spawnCreature(world, getNamedIdFrom(stack), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
		if (stack.hasDisplayName())fighter.setCustomNameTag(stack.getDisplayName());
		ItemMonsterPlacer.applyItemEntityDataToEntity(world, (EntityPlayer) null, stack, fighter);
		return fighter;
	}

	public static boolean shouldSpawnEntity(NBTTagCompound compound) {
		if (compound == null)
			return true;

		if (!compound.hasKey("EntityTag", 10))
			return true;
		NBTTagCompound entityTagNBT = compound.getCompoundTag("EntityTag");

		if (!entityTagNBT.hasKey("SizeMultiplier"))
			return true;
		float sizeMultiplier = entityTagNBT.getFloat("SizeMultiplier");

		double percentage = MathHelper.clamp(sizeMultiplier / 5.0f, 0.1f, 1.0f);
		return Math.random() < percentage;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag isAdvanced) {
		super.addInformation(stack,world,tooltip,isAdvanced);
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

		if (subTag.hasKey("Months")) {
			int months = subTag.getInteger("Months");
			StringBuilder sb = new StringBuilder();
			sb.append(TextFormatting.GRAY + "Months: ");
			sb.append(TextFormatting.GREEN + String.valueOf(months));
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
			sb.append(TextFormatting.GREEN + date1.format(new Date(time)));
			sb.append(" ");
			sb.append(TextFormatting.GREEN + date2.format(new Date(time)));
			tooltip.add(sb.toString());
		}
	}

	public int getMonths(ItemStack stack) {
		if (!stack.hasTagCompound())
			return -1;
		NBTTagCompound stackTag = stack.getTagCompound();

		if (!stackTag.hasKey("EntityTag"))
			return -1;
		NBTTagCompound entityTag = stackTag.getCompoundTag("EntityTag");

		if (!entityTag.hasKey("SubData"))
			return -1;
		NBTTagCompound subTag = entityTag.getCompoundTag("SubData");

		if (!subTag.hasKey("Months"))
			return -1;
		return subTag.getInteger("Months");
	}

}
