package net.emil.enhancedearlygame.registry;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.emil.enhancedearlygame.block.StoneAnvilBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(EnhancedEarlygame.MODID);

    public static final DeferredBlock<StoneAnvilBlock> STONE_ANVIL =
            BLOCKS.registerBlock(
                    "stone_anvil",
                    StoneAnvilBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(3.5F, 6.0F)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
            );

    private ModBlocks() {
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
