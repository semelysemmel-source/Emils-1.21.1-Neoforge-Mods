package net.emil.enhancedearlygame.forging;

import net.emil.enhancedearlygame.registry.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ForgingRules {
    private ForgingRules() {
    }

    public static ForgingCost calculateCost(
            ForgingSelection selection,
            ForgingMaterial material,
            ItemStack powder
    ) {
        int baseMaterialCost = selection.MaterialCost();

        if (powder.is(ModItems.SMITHING_POWDER.get())) {
            return new ForgingCost(
                    baseMaterialCost,
                    material.normalPowderCost()
            );
        }

        if (powder.is(ModItems.BLAZED_SMITHING_POWDER.get())) {
            int usablePowder = Math.min(
                    powder.getCount(),
                    baseMaterialCost
            );

            int reducedMaterial = Math.max(
                    1,
                    baseMaterialCost - usablePowder + 1
            );

            return new ForgingCost(
                    reducedMaterial,
                    usablePowder
            );
        }

        return null;
    }

    public static boolean canRepairInStoneAnvil(ItemStack stack) {
        return stack.is(ModItems.COPPER_PICKAXE.get())
                || stack.is(ModItems.COPPER_AXE.get())
                || stack.is(ModItems.COPPER_SHOVEL.get())
                || stack.is(ModItems.COPPER_HOE.get())
                || stack.is(ModItems.COPPER_SWORD.get())
                || stack.is(ModItems.COPPER_HELMET.get())
                || stack.is(ModItems.COPPER_CHESTPLATE.get())
                || stack.is(ModItems.COPPER_LEGGINGS.get())
                || stack.is(ModItems.COPPER_BOOTS.get());
    }

    public static Item getResult(
            ForgingMaterial material,
            ForgingSelection selection
    ) {
        return switch (material) {
            case COPPER -> switch (selection) {
                case PICKAXE_HEAD -> ModItems.COPPER_PICKAXE_HEAD.get();
                case AXE_HEAD -> ModItems.COPPER_AXE_HEAD.get();
                case SHOVEL_HEAD -> ModItems.COPPER_SHOVEL_HEAD.get();
                case SWORD_BLADE -> ModItems.COPPER_SWORD_BLADE.get();
                case HOE_HEAD -> ModItems.COPPER_HOE_HEAD.get();
                case HELMET -> ModItems.COPPER_HELMET.get();
                case CHESTPLATE -> ModItems.COPPER_CHESTPLATE.get();
                case LEGGINGS -> ModItems.COPPER_LEGGINGS.get();
                case BOOTS -> ModItems.COPPER_BOOTS.get();
            };

            case IRON -> switch (selection) {
                case PICKAXE_HEAD -> ModItems.IRON_PICKAXE_HEAD.get();
                case AXE_HEAD -> ModItems.IRON_AXE_HEAD.get();
                case SHOVEL_HEAD -> ModItems.IRON_SHOVEL_HEAD.get();
                case SWORD_BLADE -> ModItems.IRON_SWORD_BLADE.get();
                case HOE_HEAD -> ModItems.IRON_HOE_HEAD.get();
                case HELMET -> Items.IRON_HELMET;
                case CHESTPLATE -> Items.IRON_CHESTPLATE;
                case LEGGINGS -> Items.IRON_LEGGINGS;
                case BOOTS -> Items.IRON_BOOTS;
            };

            case GOLD -> switch (selection) {
                case PICKAXE_HEAD -> ModItems.GOLD_PICKAXE_HEAD.get();
                case AXE_HEAD -> ModItems.GOLD_AXE_HEAD.get();
                case SHOVEL_HEAD -> ModItems.GOLD_SHOVEL_HEAD.get();
                case SWORD_BLADE -> ModItems.GOLD_SWORD_BLADE.get();
                case HOE_HEAD -> ModItems.GOLD_HOE_HEAD.get();
                case HELMET -> Items.GOLDEN_HELMET;
                case CHESTPLATE -> Items.GOLDEN_CHESTPLATE;
                case LEGGINGS -> Items.GOLDEN_LEGGINGS;
                case BOOTS -> Items.GOLDEN_BOOTS;
            };

            case DIAMOND -> switch (selection) {
                case PICKAXE_HEAD -> ModItems.DIAMOND_PICKAXE_HEAD.get();
                case AXE_HEAD -> ModItems.DIAMOND_AXE_HEAD.get();
                case SHOVEL_HEAD -> ModItems.DIAMOND_SHOVEL_HEAD.get();
                case SWORD_BLADE -> ModItems.DIAMOND_SWORD_BLADE.get();
                case HOE_HEAD -> ModItems.DIAMOND_HOE_HEAD.get();
                case HELMET -> Items.DIAMOND_HELMET;
                case CHESTPLATE -> Items.DIAMOND_CHESTPLATE;
                case LEGGINGS -> Items.DIAMOND_LEGGINGS;
                case BOOTS -> Items.DIAMOND_BOOTS;
            };
        };
    }
}

