package kaptainwutax.itraders.item;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.gui.GuiHandler;
import kaptainwutax.itraders.init.InitItem;
import kaptainwutax.itraders.world.data.DataEggPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemEggPouch extends Item {

	public ItemEggPouch(String name) {
		this.setTranslationKey(name);
		this.setRegistryName(Traders.getResource(name));
		this.setCreativeTab(InitItem.ITRADERS_TAB);
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (!player.isSneaking())
			player.openGui(Traders.getInstance(), GuiHandler.POUCH, world, 0, 0, 0);
		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ) {
		if (player.isSneaking() && !world.isRemote) {
			ItemStack itemStack = DataEggPouch.get(world).getOrCreatePouch(player).randomFighterEgg();

			if (itemStack != null && (itemStack.getItem() instanceof ItemSpawnEggFighter)) {
				ItemSpawnEggFighter eggItem = (ItemSpawnEggFighter) itemStack.getItem();

				if (itemStack.hasDisplayName()) {
					StringBuilder sb = new StringBuilder();
					sb.append(TextFormatting.GREEN + itemStack.getDisplayName());

					int months = InitItem.SPAWN_EGG_FIGHTER.getMonths(itemStack);

					if (months == -1)
						sb.append(TextFormatting.GRAY + ", I choose you!");
					else
						sb.append(TextFormatting.GRAY + "(" + months + "), I choose you!");

					player.sendStatusMessage(new TextComponentString(sb.toString()), true);

					world.playSound(null, player.getPosition(),
							SoundEvents.BLOCK_ANVIL_LAND,
							SoundCategory.PLAYERS, 1.0f, 0.0f);
				}

				return eggItem.onItemUse(itemStack, player, world, pos, hand, facing, hitX, hitY, hitZ);
			}
		}

		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

}