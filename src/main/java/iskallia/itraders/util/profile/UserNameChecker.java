package iskallia.itraders.util.profile;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.yggdrasil.ProfileNotFoundException;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class UserNameChecker {

    public static GameProfileRepository repo;
    private static ConcurrentSet<String> validNames = new ConcurrentSet<>();
    private static ConcurrentSet<String> invalidNames = new ConcurrentSet<>();
    private static ConcurrentSet<String> currentLookup = new ConcurrentSet<>();

    public static String getTextFormatting(String name) {
        String finalName = name.toLowerCase();
        if (currentLookup.contains(finalName))//if name is already being requested return yellow
            return TextFormatting.YELLOW.toString();


        //Create a new lookup request for name if it hasn't been checked
        if (!(validNames.contains(finalName) || invalidNames.contains(finalName))) {
            currentLookup.add(finalName);
            ProfileLookupCallback callback = new ProfileLookupCallback() {
                @Override
                public void onProfileLookupSucceeded(GameProfile profile) {
                    validNames.add(finalName);
                    currentLookup.remove(finalName);
                }

                @Override
                public void onProfileLookupFailed(GameProfile profile, Exception exception) {
                    if (exception instanceof ProfileNotFoundException)//No profile was found for that person
                    {
                        invalidNames.add(finalName);
                    }
                    currentLookup.remove(finalName);
                }
            };
            
            SkinProfile.service.execute(() -> {repo.findProfilesByNames(new String[]{name}, Agent.MINECRAFT, callback);});

            return TextFormatting.YELLOW.toString();
        }
        return (validNames.contains(finalName) ? "" : TextFormatting.RED.toString());
    }

    public static String getTextFormattingFromItem(ItemStack item) {
        if (!item.hasTagCompound())
            return "";

        NBTTagCompound display = item.getTagCompound().getCompoundTag("display");

        if (display != null && !display.hasKey("Name"))
            return "";

        return UserNameChecker.getTextFormatting(display.getString("Name"));
    }

}
