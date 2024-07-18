package com.bonker.stardewfishing.mixin;

import com.bonker.stardewfishing.StardewFishing;
import com.bonker.stardewfishing.common.FishingHookLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(targets = "com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity")
public abstract class AquaFishingBobberEntityMixin extends FishingHook {
    @Shadow protected abstract List<ItemStack> getLoot(LootParams lootParams, ServerLevel serverLevel);

    private AquaFishingBobberEntityMixin(EntityType<? extends FishingHook> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "catchingFish(Lnet/minecraft/core/BlockPos;)V", at = @At(value = "HEAD"), cancellable = true)
    private void cancel_catchingFish(BlockPos pPos, CallbackInfo ci) {
        if (FishingHookLogic.getStoredRewards(this).isEmpty()) {
            ci.cancel();
        }
    }

    @Inject(method = "retrieve(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At(value = "INVOKE",
                    target = "Lcom/teammetallurgy/aquaculture/entity/AquaFishingBobberEntity;discard()V",
                    ordinal = 1),
            cancellable = true)
    public void retrieve_cancel_discard(ItemStack pStack, CallbackInfoReturnable<Integer> cir) {
        if (getPlayerOwner() == null) return;

        FishingHookLogic.getStoredRewards(this).ifPresent(rewards -> {
            ServerPlayer player = (ServerPlayer) getPlayerOwner();

            if (rewards.stream().anyMatch(stack -> stack.is(StardewFishing.STARTS_MINIGAME))) {
                FishingHookLogic.startMinigame(player);
                cir.cancel();
            } else {
                player.level().playSound(null, player, StardewFishing.PULL_ITEM.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        });
    }

    @Inject(method = "spawnLoot(Lnet/minecraft/world/entity/player/Player;Ljava/util/List;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void inject_spawnLoot(Player player, List<ItemStack> items, CallbackInfo ci) {
        if (items.stream().anyMatch(item -> item.is(StardewFishing.STARTS_MINIGAME))) {
            FishingHookLogic.getStoredRewards(this).ifPresent(rewards -> rewards.addAll(items));
            ci.cancel();
        } else {
            FishingHookLogic.modifyRewards(items, 0);
        }
    }
}
