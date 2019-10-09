package kaptainwutax.itraders.mixin;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;

import net.minecraft.server.management.PlayerProfileCache;
import scala.reflect.internal.Trees.This;

@Mixin(PlayerProfileCache.class)
public abstract class MixinPlayerProfileCache {
	
	@Shadow @Final @Mutable private Map<String, Object> usernameToProfileEntryMap;
	@Shadow @Final @Mutable private Map<UUID, Object> uuidToProfileEntryMap;
	@Shadow @Final @Mutable private Deque<GameProfile> gameProfiles;
	
	@Inject(method = "<init>", at = @At("RETURN"))
    private void PlayerProfileCache(GameProfileRepository profileRepo, File usercacheFile, CallbackInfo ci) {
		this.usernameToProfileEntryMap = new ConcurrentHashMap<>();
		this.uuidToProfileEntryMap = new ConcurrentHashMap<>();
		this.gameProfiles = new ConcurrentLinkedDeque<>();
    }
	
}
