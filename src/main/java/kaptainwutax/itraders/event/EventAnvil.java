package kaptainwutax.itraders.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Traders.MOD_ID)
public class EventAnvil {

	private static final Pattern SKULL_NAME = Pattern.compile("(?<skullname>.*?)('s Head)?", Pattern.CASE_INSENSITIVE);

	@SubscribeEvent
	public static void onSkullRenamed(AnvilRepairEvent event) {
		ItemStack stack = event.getItemResult();

		if(stack.getItem() != Items.SKULL)return;

		Matcher matcher = SKULL_NAME.matcher(stack.getDisplayName());

		if(matcher.matches()) {
			String skullName = matcher.group("skullname");
			NBTTagCompound nbt = stack.getTagCompound();
			
			if(nbt != null) {
				nbt.setString("SkullOwner", skullName);
				stack.setTagCompound(nbt);
			}
		}
	}

	@SubscribeEvent
	public static void onMinerCrafted(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();
		Item rightItem = right.getItem();
		
		if(left.getItem() != InitItem.SPAWN_EGG_TRADER)return;
		System.out.println(left.getItem());
		if(!(rightItem instanceof ItemTool))return;
		if(!((ItemTool)rightItem).getToolClasses(right).contains("pickaxe"))return;
		
		ItemStack minerEgg = new ItemStack(InitItem.SPAWN_EGG_MINER, 1);
		minerEgg.setTagCompound(left.getTagCompound());
		
		if(!minerEgg.hasTagCompound())minerEgg.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = minerEgg.getTagCompound();
		if(!nbt.hasKey("EntityTag"))nbt.setTag("EntityTag", new NBTTagCompound());
		NBTTagCompound entityTag = nbt.getCompoundTag("EntityTag");
		
		NBTTagList handItems = entityTag.getTagList("HandItems", 10);
		NBTTagCompound minerEggNbt = right.writeToNBT(new NBTTagCompound());
		handItems.appendTag(minerEggNbt);
		entityTag.setTag("HandItems", handItems);
		
		event.setCost(1);
		event.setOutput(minerEgg);
	}

}
