package kaptainwutax.itraders;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.yggdrasil.ProfileNotFoundException;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserNameChecker {
    public static GameProfileRepository repo;
    private static ConcurrentSet<String> ValidNames=new ConcurrentSet<>();
    private static ConcurrentSet<String> InvalidNames=new ConcurrentSet<>();
    private static ConcurrentSet<String> CurrentLookup=new ConcurrentSet<>();

    private static Executor service=Executors.newFixedThreadPool(5);
    public static String getTextFormatting(String name) {
        String finalName=name.toLowerCase();
        if(CurrentLookup.contains(finalName))//if name is already being requested return yellow
            return TextFormatting.YELLOW.toString();


        //Create a new lookup request for name if it hasn't been checked
        if(!(ValidNames.contains(finalName)||InvalidNames.contains(finalName)))
        {
            CurrentLookup.add(finalName);
            ProfileLookupCallback callback=new ProfileLookupCallback() {
                @Override
                public void onProfileLookupSucceeded(GameProfile profile) {
                    ValidNames.add(finalName);
                    CurrentLookup.remove(finalName);
                }

                @Override
                public void onProfileLookupFailed(GameProfile profile, Exception exception) {
                    if(exception instanceof ProfileNotFoundException)//No profile was found for that person
                    {
                        InvalidNames.add(finalName);
                    }
                    CurrentLookup.remove(finalName);
                }
            };
            service.execute(()->{repo.findProfilesByNames(new String[]{name}, Agent.MINECRAFT,callback);});

            return TextFormatting.YELLOW.toString();
        }
        return (ValidNames.contains(finalName)?"":TextFormatting.RED.toString());
    }

    public static String getTextFormattingFromItem(ItemStack item)  {
        if(!item.hasTagCompound())
            return "";
        NBTTagCompound display=item.getTagCompound().getCompoundTag("display");
        if(display==null)
            return "";
        if(!display.hasKey("Name"))
            return "";
        return UserNameChecker.getTextFormatting(display.getString("Name"));
    }

}
