package net.emil.enhancedearlygame.item;

import net.emil.enhancedearlygame.tag.ModBlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.common.Tags;

public final class ModToolTiers {

    public static final Tier COPPER = new SimpleTier(

            ModBlockTags.INCORRECT_FOR_COPPER_TOOL,
            200,
            5.0F,
            1.5F,
            15,
            () -> Ingredient.of(Tags.Items.INGOTS_COPPER)

    );

    private ModToolTiers() {
    }
}
