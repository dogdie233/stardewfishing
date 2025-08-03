package com.bonker.stardewfishing.common.init;

import com.bonker.stardewfishing.StardewFishing;
import com.bonker.stardewfishing.common.blocks.FishDisplayBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SFBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, StardewFishing.MODID);

    public static final RegistryObject<BlockEntityType<FishDisplayBlockEntity>> FISH_DISPLAY = BLOCK_ENTITY_TYPES.register("fish_display",
        () -> BlockEntityType.Builder.of(FishDisplayBlockEntity::new, SFBlocks.FISH_DISPLAY.get()).build(null));
}
