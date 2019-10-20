package kaptainwutax.itraders.item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.entity.EntityTrader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpawnEggTrader extends ItemSpawnEgg<EntityTrader> {

	private static final Map<String, String> CURRENCY_SYMBOLS = new HashMap<>();

	static {
		CURRENCY_SYMBOLS.put("USD", "$");
		CURRENCY_SYMBOLS.put("EUR", "€");
		CURRENCY_SYMBOLS.put("GBP", "£");
		CURRENCY_SYMBOLS.put("TRY", "₺");
		CURRENCY_SYMBOLS.put("JPY", "¥");
	}

	public ItemSpawnEggTrader(String name) {
		super(name, Traders.getResource("trader"), false);
	}

	@Override
	protected EntityTrader onPlayerSpawn(World world, EntityPlayer player, BlockPos pos, ItemStack stack) {
		EntityTrader trader = spawnCreature(world, getNamedIdFrom(stack), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
		if(stack.hasDisplayName())trader.setCustomNameTag(stack.getDisplayName());
		ItemMonsterPlacer.applyItemEntityDataToEntity(world, (EntityPlayer) null, stack, trader);
		return trader;
	}
	
	@Override
	protected EntityTrader onDispenserSpawn(World world, EnumFacing enumfacing, BlockPos blockPos, ItemStack stack) {
		return null;
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

		if (subTag.hasKey("Amount")) {
			float amount = subTag.getFloat("Amount");
			StringBuilder sb = new StringBuilder();
			sb.append(TextFormatting.GRAY + "Amount: ");
			sb.append(TextFormatting.YELLOW + String.valueOf(amount));
			if(subTag.hasKey("Currency")) {
				String currencyISO = subTag.getString("Currency").toUpperCase();
				sb.append(CURRENCY_SYMBOLS.getOrDefault(currencyISO, currencyISO));
			}
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
