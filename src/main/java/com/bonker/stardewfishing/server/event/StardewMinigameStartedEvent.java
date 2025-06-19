package com.bonker.stardewfishing.server.event;

import com.bonker.stardewfishing.common.FishBehavior;
import com.bonker.stardewfishing.common.init.SFAttributes;
import com.bonker.stardewfishing.proxy.ItemUtils;
import com.bonker.stardewfishing.server.AttributeCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class StardewMinigameStartedEvent extends StardewMinigameEvent {
    private final ItemStack fish;
    private int idleTime;
    private float topSpeed;
    private float upAcceleration;
    private float downAcceleration;
    private int avgDistance;
    private int moveVariation;
    private double lineStrength;
    private int barSize;
    private double treasureChanceBonus;
    private double expMultiplier;
    private boolean forcedTreasureChest = false;
    private boolean forcedGoldenChest = false;
    private boolean lavaFishing;
    private int qualityBoost = 0;

    public StardewMinigameStartedEvent(ServerPlayer player, FishingHook hook, ItemStack fishingRod,
                                       ItemStack fish, FishBehavior behavior, boolean lavaFishing) {
        super(player, hook, fishingRod);
        this.fish = fish;
        this.idleTime = behavior.idleTime();
        this.topSpeed = behavior.topSpeed();
        this.upAcceleration = behavior.upAcceleration();
        this.downAcceleration = behavior.downAcceleration();
        this.avgDistance = behavior.avgDistance();
        this.moveVariation = behavior.moveVariation();
        this.lineStrength = AttributeCache.getAttribute(player, SFAttributes.LINE_STRENGTH.get());
        this.barSize = (int) AttributeCache.getAttribute(player, SFAttributes.BAR_SIZE.get());
        this.treasureChanceBonus = AttributeCache.getAttribute(player, SFAttributes.TREASURE_CHANCE_BONUS.get());
        this.expMultiplier = AttributeCache.getAttribute(player, SFAttributes.EXPERIENCE_MULTIPLIER.get());
        this.lavaFishing = lavaFishing;

        // according to the minecraft wiki, each level of luck grants a 2.1% higher chance of treasure
        float luckBonus = ItemUtils.getLuck(hook) * 0.021F;
        this.treasureChanceBonus += luckBonus;
    }

    public ItemStack getFish() {
        return fish;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public float getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(float topSpeed) {
        this.topSpeed = topSpeed;
    }

    public float getUpAcceleration() {
        return upAcceleration;
    }

    public void setUpAcceleration(float upAcceleration) {
        this.upAcceleration = upAcceleration;
    }

    public float getDownAcceleration() {
        return downAcceleration;
    }

    public void setDownAcceleration(float downAcceleration) {
        this.downAcceleration = downAcceleration;
    }

    public int getAvgDistance() {
        return avgDistance;
    }

    public void setAvgDistance(int avgDistance) {
        this.avgDistance = avgDistance;
    }

    public int getMoveVariation() {
        return moveVariation;
    }

    public void setMoveVariation(int moveVariation) {
        this.moveVariation = moveVariation;
    }

    public double getLineStrength() {
        return lineStrength;
    }

    public void setLineStrength(double lineStrength) {
        this.lineStrength = lineStrength;
    }

    public int getBarSize() {
        return barSize;
    }

    public void setBarSize(int barSize) {
        this.barSize = barSize;
    }

    public double getTreasureChanceBonus() {
        return treasureChanceBonus;
    }

    public void setTreasureChanceBonus(double treasureChanceBonus) {
        this.treasureChanceBonus = treasureChanceBonus;
    }

    public double getExpMultiplier() {
        return expMultiplier;
    }

    public void setExpMultiplier(double expMultiplier) {
        this.expMultiplier = expMultiplier;
    }

    public boolean isForcedTreasureChest() {
        return forcedTreasureChest;
    }

    public void setForcedTreasureChest(boolean forcedTreasureChest) {
        this.forcedTreasureChest = forcedTreasureChest;
    }

    public boolean isForcedGoldenChest() {
        return forcedGoldenChest;
    }

    public void setForcedGoldenChest(boolean forcedGoldenChest) {
        this.forcedGoldenChest = forcedGoldenChest;
    }

    public boolean isLavaFishing() {
        return lavaFishing;
    }

    public void setLavaFishing(boolean lavaFishing) {
        this.lavaFishing = lavaFishing;
    }

    public int getQualityBoost() {
        return qualityBoost;
    }

    public void setQualityBoost(int qualityBoost) {
        this.qualityBoost = qualityBoost;
    }
}
