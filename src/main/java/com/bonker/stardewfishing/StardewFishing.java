package com.bonker.stardewfishing;

import com.bonker.stardewfishing.common.init.SFAttributes;
import com.bonker.stardewfishing.common.init.SFItems;
import com.bonker.stardewfishing.common.init.SFLootPoolEntryTypes;
import com.bonker.stardewfishing.common.init.SFSoundEvents;
import com.bonker.stardewfishing.common.networking.SFNetworking;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(StardewFishing.MODID)
public class StardewFishing {
    public static final String MODID = "stardew_fishing";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean QUALITY_FOOD_INSTALLED = ModList.get().isLoaded("quality_food");
    public static final boolean AQUACULTURE_INSTALLED = ModList.get().isLoaded("aquaculture");
    public static final boolean TIDE_INSTALLED = ModList.get().isLoaded("tide");

    public static final TagKey<Item> STARTS_MINIGAME = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), resource("starts_minigame"));
    public static final TagKey<Item> MODIFIABLE_RODS = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), resource("modifiable_rods"));
    public static final TagKey<Item> BOBBERS = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), resource("bobbers"));

    public static final ResourceLocation TREASURE_CHEST_LOOT = resource("treasure_chest");
    public static final ResourceLocation TREASURE_CHEST_NETHER_LOOT = resource("treasure_chest_nether");

    public static String MOD_NAME;

    public static final Style GREEN = Style.EMPTY.withColor(0xb4ce99);
    public static final Style RED = Style.EMPTY.withColor(0xca7d6c);
    public static final Style LIGHTER_COLOR = Style.EMPTY.withColor(0xcca06d);
    public static final Style LIGHT_COLOR = Style.EMPTY.withColor(0xaf7a3e);
    public static final Style DARK_COLOR = Style.EMPTY.withColor(0x7e582c);

    public StardewFishing() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();

        IEventBus bus = context.getModEventBus();

        IModInfo info = context.getContainer().getModInfo();
        MOD_NAME = info.getDisplayName() + " " + info.getVersion();

        SFSoundEvents.SOUND_EVENTS.register(bus);
        SFLootPoolEntryTypes.LOOT_POOL_ENTRY_TYPES.register(bus);
        SFAttributes.ATTRIBUTES.register(bus);
        SFItems.ITEMS.register(bus);
        SFItems.CREATIVE_MODE_TABS.register(bus);

        context.registerConfig(ModConfig.Type.SERVER, SFConfig.SERVER_SPEC);
    }
    
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
