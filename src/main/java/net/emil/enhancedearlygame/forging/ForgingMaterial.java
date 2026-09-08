package net.emil.enhancedearlygame.forging;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum ForgingMaterial {
    COPPER(Items.COPPER_INGOT, 1, true),
    IRON(Items.IRON_INGOT, 1, false),
    GOLD(Items.GOLD_INGOT, 1, false),
    DIAMOND(Items.DIAMOND, 2, false);

    private final Item ingredient;
    private final int normalPowderCost;
    private final boolean allowedInStoneAnvil;

    ForgingMaterial(
            Item ingredient,
            int normalPowderCost,
            boolean allowedInStoneAnvil
    ) {
        this.ingredient = ingredient;
        this.normalPowderCost = normalPowderCost;
        this.allowedInStoneAnvil = allowedInStoneAnvil;
    }

    public Item ingredient() {
        return ingredient;
    }

    public int normalPowderCost() {
        return normalPowderCost;
    }

    public boolean allowedInStoneAnvil() {
        return allowedInStoneAnvil;
    }

    public static ForgingMaterial fromStack(ItemStack stack) {
        for (ForgingMaterial material : values()) {
            if (stack.is(material.ingredient)) {
                return material;
            }
        }

    return null;

    }
}
