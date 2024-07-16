package com.bonker.stardewfishing.common;

import com.bonker.stardewfishing.SFConfig;
import com.bonker.stardewfishing.StardewFishing;
import com.bonker.stardewfishing.common.networking.S2CStartMinigamePacket;
import com.bonker.stardewfishing.common.networking.SFNetworking;
import com.bonker.stardewfishing.server.FishBehaviorReloadListener;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StardewFishingHook extends FishingHook {
    private final List<ItemStack> rewards = new ArrayList<>();

    public StardewFishingHook(EntityType<? extends FishingHook> entityType, Level level) {
        super(entityType, level);
    }

    public StardewFishingHook(Player player, Level level, int luck, int lureSpeed) {
        this(StardewFishing.STARDEW_FISHING_HOOK.get(), level);

        this.luck = luck;
        this.lureSpeed = lureSpeed;

        this.setOwner(player);
        float f = player.getXRot();
        float f1 = player.getYRot();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        double d0 = player.getX() - (double)f3 * 0.3D;
        double d1 = player.getEyeY();
        double d2 = player.getZ() - (double)f2 * 0.3D;
        this.moveTo(d0, d1, d2, f1, f);
        Vec3 vec3 = new Vec3((double)(-f3), (double)Mth.clamp(-(f5 / f4), -5.0F, 5.0F), (double)(-f2));
        double d3 = vec3.length();
        vec3 = vec3.multiply(0.6D / d3 + this.random.triangle(0.5D, 0.0103365D), 0.6D / d3 + this.random.triangle(0.5D, 0.0103365D), 0.6D / d3 + this.random.triangle(0.5D, 0.0103365D));
        this.setDeltaMovement(vec3);
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vec3.y, vec3.horizontalDistance()) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void startMinigame() {
        SFNetworking.sendToPlayer((ServerPlayer) getPlayerOwner(), new S2CStartMinigamePacket(
                FishBehaviorReloadListener.getBehavior(
                        rewards.stream()
                                .filter(stack -> stack.is(StardewFishing.STARTS_MINIGAME))
                                .findFirst()
                                .orElse(null)
                )
        ));
    }

    public int endMinigame(Player player, boolean success, double accuracy) {
        if (success && !level().isClientSide) {
            giveRewards((ServerPlayer) player, accuracy);
        }

        discard();
        return 1;
    }

    public void giveRewards(ServerPlayer player, double accuracy) {
        InteractionHand hand = getRodHand(player);
        if (hand != null) {
            ServerLevel level = player.serverLevel();
            ItemStack stack = player.getItemInHand(hand);

            CriteriaTriggers.FISHING_ROD_HOOKED.trigger(player, stack, this, rewards);

            int quality = SFConfig.getQuality(accuracy);
            for (ItemStack reward : rewards) {
                if (reward.is(StardewFishing.STARTS_MINIGAME)) {
                    player.awardStat(Stats.FISH_CAUGHT, 1);
                    if (StardewFishing.QUALITY_FOOD_INSTALLED) {
                        if (quality == 0 && reward.hasTag() && reward.getOrCreateTag().contains("quality_food")) {
                            if (reward.getOrCreateTag().size() > 1) {
                                reward.getOrCreateTag().remove("quality_food");
                            } else {
                                reward.setTag(null);
                            }
                        } else if (quality > 0) {
                            CompoundTag qualityFood = new CompoundTag();
                            qualityFood.putInt("quality", quality);
                            reward.getOrCreateTag().put("quality_food", qualityFood);
                        }
                    }
                }

                ItemEntity itementity = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), reward);
                double dx = player.getX() - this.getX();
                double dy = player.getY() - this.getY();
                double dz = player.getZ() - this.getZ();
                double scale = 0.1;
                itementity.setDeltaMovement(dx * scale, dy * scale + Math.sqrt(Math.sqrt(dx * dx + dy * dy + dz * dz)) * 0.08, dz * scale);
                level.addFreshEntity(itementity);

                int exp = Mth.floor((random.nextInt(6) + 1) * SFConfig.getMultiplier(accuracy));
                level.addFreshEntity(new ExperienceOrb(level, player.getX(), player.getY() + 0.5, player.getZ() + 0.5, exp));
            }

            level().playSound(null, player, StardewFishing.PULL_ITEM.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public void catchingFish(BlockPos pPos) {
        if (rewards.isEmpty()) {
            super.catchingFish(pPos);
        }
    }

    @Override
    public int retrieve(ItemStack pStack) {
        Player player = this.getPlayerOwner();
        if (!this.level().isClientSide && player != null && !this.shouldStopFishing(player)) {
            if (this.hookedIn != null) {
                this.pullEntity(this.hookedIn);
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)player, pStack, this, Collections.emptyList());
                this.level().broadcastEntityEvent(this, (byte)31);
                this.discard();
                return this.hookedIn instanceof ItemEntity ? 3 : 5;
            } else if (this.onGround()) {
                this.discard();
                return 2;
            } else if (this.nibble > 0) {
                LootParams lootparams = (new LootParams.Builder((ServerLevel)this.level())).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.TOOL, pStack).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.KILLER_ENTITY, this.getOwner()).withParameter(LootContextParams.THIS_ENTITY, this).withLuck((float)this.luck + player.getLuck()).create(LootContextParamSets.FISHING);
                LootTable loottable = this.level().getServer().getLootData().getLootTable(BuiltInLootTables.FISHING);
                rewards.addAll(loottable.getRandomItems(lootparams));

                if (rewards.stream().anyMatch(stack -> stack.is(StardewFishing.STARTS_MINIGAME))) {
                    startMinigame();
                    return 0;
                } else {
                    giveRewards((ServerPlayer) player, 0);
                    discard();
                    return 1;
                }
            }

            discard();
        }
        return 0;
    }

    public static InteractionHand getRodHand(Player player) {
        boolean mainHand = player.getItemInHand(InteractionHand.MAIN_HAND).canPerformAction(ToolActions.FISHING_ROD_CAST);
        if (mainHand) return InteractionHand.MAIN_HAND;

        boolean offHand = player.getItemInHand(InteractionHand.OFF_HAND).canPerformAction(ToolActions.FISHING_ROD_CAST);
        if (offHand) return InteractionHand.OFF_HAND;

        return null;
    }
}
