package kaptainwutax.itraders.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kaptainwutax.itraders.Traders;
import kaptainwutax.itraders.init.InitItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Traders.MOD_ID)
public class EventAnvil {

    private static final Pattern SKULL_NAME = Pattern.compile("(?<skullname>.*?)('s Head)?",
            Pattern.CASE_INSENSITIVE);

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
    }

}
