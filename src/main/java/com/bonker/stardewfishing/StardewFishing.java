package com.bonker.stardewfishing;

import com.bonker.stardewfishing.common.init.SFItems;
import com.bonker.stardewfishing.common.init.SFSoundEvents;
import com.bonker.stardewfishing.common.networking.SFNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(StardewFishing.MODID)
public class StardewFishing {
    /*
        Constants
     */

    public static final String MODID = "stardew_fishing";

    public static boolean QUALITY_FOOD_INSTALLED = false;
    public static boolean AQUACULTURE_INSTALLED = false;

    public static final TagKey<Item> STARTS_MINIGAME = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation(MODID, "starts_minigame"));

    /*
        Sound Events
     */

    public StardewFishing() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        SFSoundEvents.SOUND_EVENTS.register(bus);
        SFItems.ITEMS.register(bus);
        SFItems.CREATIVE_MODE_TABS.register(bus);
        SFNetworking.register();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SFConfig.SERVER_SPEC);
    }
}
