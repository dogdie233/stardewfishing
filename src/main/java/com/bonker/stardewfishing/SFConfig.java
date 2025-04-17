package com.bonker.stardewfishing;

import com.bonker.stardewfishing.common.init.SFAttributes;
import com.bonker.stardewfishing.server.AttributeCache;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StardewFishing.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SFConfig {
    static final ForgeConfigSpec SERVER_SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.DoubleValue QUALITY_1_THRESHOLD;
    private static final ForgeConfigSpec.DoubleValue QUALITY_2_THRESHOLD;
    private static final ForgeConfigSpec.DoubleValue QUALITY_3_THRESHOLD;
    private static final ForgeConfigSpec.DoubleValue QUALITY_1_MULTIPLIER;
    private static final ForgeConfigSpec.DoubleValue QUALITY_2_MULTIPLIER;
    private static final ForgeConfigSpec.DoubleValue QUALITY_3_MULTIPLIER;
    private static final ForgeConfigSpec.DoubleValue TRAP_BOBBER_LINE_STRENGTH;
    private static final ForgeConfigSpec.IntValue CORK_BOBBER_BAR_SIZE;
    private static final ForgeConfigSpec.DoubleValue TREASURE_BOBBER_TREASURE_CHANCE;
    private static final ForgeConfigSpec.DoubleValue QUALITY_BOBBER_EXP_MULTIPLIER;
    private static final ForgeConfigSpec.DoubleValue BITE_TIME_MULTIPLIER;
    private static final ForgeConfigSpec.DoubleValue TREASURE_CHEST_CHANCE;
    private static final ForgeConfigSpec.DoubleValue GOLDEN_CHEST_CHANCE;

    static {
        QUALITY_1_THRESHOLD = BUILDER
                .comment("The minimum accuracy that grants an item of quality 1.")
                .defineInRange("quality1Threshold", 0.75, 0, 1);

        QUALITY_2_THRESHOLD = BUILDER
                .comment("The minimum accuracy that grants an item of quality 2.")
                .defineInRange("quality2Threshold", 0.9, 0, 1);

        QUALITY_3_THRESHOLD = BUILDER
                .comment("The minimum accuracy that grants an item of quality 3.")
                .defineInRange("quality3Threshold", 1.0, 0, 1);

        QUALITY_1_MULTIPLIER = BUILDER
                .comment("The multiplier that is applied to experience gained from fishing a quality 1 reward.")
                .defineInRange("quality1Multiplier", 1.5, 1, 10);

        QUALITY_2_MULTIPLIER = BUILDER
                .comment("The multiplier that is applied to experience gained from fishing a quality 2 reward.")
                .defineInRange("quality2Multiplier", 2.5, 1, 10);

        QUALITY_3_MULTIPLIER = BUILDER
                .comment("The multiplier that is applied to experience gained from fishing a quality 3 reward.")
                .defineInRange("quality3Multiplier", 4.0, 1, 10);

        TRAP_BOBBER_LINE_STRENGTH = BUILDER
                .comment("The line strength attribute modifier granted by the Trap Bobber. Fish escape speed with the Trap Bobber is calculated as `1 - trapBobberLineStrength` points per tick.")
                .defineInRange("trapBobberLineStrength", 0.33, 0, 1);

        CORK_BOBBER_BAR_SIZE = BUILDER
                .comment("The fishing bar size attribute modifier granted by the Cork Bobber. The Cork Bobber adds corkBobberBarSize pixels to the size of the fishing bar.")
                .defineInRange("corkBobberBarSize", 10, 0, 100);

        TREASURE_BOBBER_TREASURE_CHANCE = BUILDER
                .comment("The treasure chance attribute modifier granted by the Treasure Bobber. The Treasure Bobber increases the chance of finding a treasure chest by treasureBobberTreasureChance percent.")
                .defineInRange("treasureBobberTreasureChance", 0.05, 0, 1);

        QUALITY_BOBBER_EXP_MULTIPLIER = BUILDER
                .comment("The fishing experience multiplier attribute modifier granted by the Quality Bobber. Fishing experience gained with the Quality Bobber is multiplied by this number.")
                .defineInRange("qualityBobberExpMultiplier", 1.4, 1, 10);

        BITE_TIME_MULTIPLIER = BUILDER
                .comment("The multiplier that is applied to the time it takes for a fish to bite after casting your rod.")
                .defineInRange("biteTimeMultiplier", 0.8, 0, 1);

        TREASURE_CHEST_CHANCE = BUILDER
                .comment("The chance for finding a treasure chest each time you play the fishing minigame.")
                .defineInRange("treasureChestChance", 0.15, 0, 1);

        GOLDEN_CHEST_CHANCE = BUILDER
                .comment("The chance that a treasure chest found in the fishing minigame is a golden chest.")
                .defineInRange("goldenChestChance", 0.1, 0, 1);

        SERVER_SPEC = BUILDER.build();
    }

    public static int getQuality(double accuracy) {
        if (accuracy >= SFConfig.QUALITY_3_THRESHOLD.get()) {
            return 3;
        } else if (accuracy >= SFConfig.QUALITY_2_THRESHOLD.get()) {
            return 2;
        } else if (accuracy >= SFConfig.QUALITY_1_THRESHOLD.get()) {
            return 1;
        }
        return 0;
    }

    public static double getMultiplier(double accuracy, Player player) {
        double multiplier = switch(getQuality(accuracy)) {
            case 3 -> QUALITY_3_MULTIPLIER.get();
            case 2 -> QUALITY_2_MULTIPLIER.get();
            case 1 -> QUALITY_1_MULTIPLIER.get();
            default -> 1;
        };

        multiplier *= AttributeCache.getAttribute(player, SFAttributes.EXPERIENCE_MULTIPLIER.get());

        return multiplier;
    }

    public static double getTrapBobberLineStrength() {
        return TRAP_BOBBER_LINE_STRENGTH.get();
    }

    public static double getCorkBobberBarSize() {
        return CORK_BOBBER_BAR_SIZE.get();
    }

    public static double getTreasureBobberTreasureChance() {
        return TREASURE_BOBBER_TREASURE_CHANCE.get();
    }

    public static double getQualityBobberExpMultiplier() {
        return QUALITY_BOBBER_EXP_MULTIPLIER.get();
    }

    public static double getBiteTimeMultiplier() {
        return BITE_TIME_MULTIPLIER.get();
    }

    public static double getTreasureChestChance() {
        return TREASURE_CHEST_CHANCE.get();
    }

    public static double getGoldenChestChance() {
        return GOLDEN_CHEST_CHANCE.get();
    }
}
