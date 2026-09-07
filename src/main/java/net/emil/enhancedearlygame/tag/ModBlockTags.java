package net.emil.enhancedearlygame.tag;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ModBlockTags {

    public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL =
            TagKey.create(
                    Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(
                            EnhancedEarlygame.MODID,
                            "incorrect_for_copper_tool"
                    )
            );
    private ModBlockTags() {
    }
}
