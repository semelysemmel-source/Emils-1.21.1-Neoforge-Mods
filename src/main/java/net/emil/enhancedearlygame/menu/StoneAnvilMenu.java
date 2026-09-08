package net.emil.enhancedearlygame.menu;

import net.emil.enhancedearlygame.forging.*;
import net.emil.enhancedearlygame.registry.ModBlocks;
import net.emil.enhancedearlygame.registry.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;

public class StoneAnvilMenu extends ItemCombinerMenu {
    private ForgingSelection selection;
    private AnvilOperation operation = AnvilOperation.NONE;

    private int materialToConsume;
    private int powderToConsume;
    private int repairMaterialsToConsume

    public StoneAnvilMenu(int containerId, Inventory inventory) {
        this(
                containerId,
                inventory,
                ContainerLevelAccess.NULL
        );
    }

    public StoneAnvilMenu(
            int containerId,
            Inventory inventory,
            ContainerLevelAccess access
    ) {
        super(
                ModMenus.STONE_ANVIL.get(),
                containerId,
                inventory,
                access
        );
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 47, stack -> true)
                .withSlot(1, 76, 47, stack -> true)
                .withResultSlot(2, 134, 47)
                .build();
    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return state.is(ModBlocks.STONE_ANVIL.get());
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        ForgingSelection clicked =
                ForgingSelection.byButtonId(id);

        if (clicked == null) {
            return false;
        }

        this.selection = clicked;
        this.createResult();
        return true;
    }

    @Override
    protected boolean mayPickup(Player player, boolean hasStack) {
        return false;
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {

    }

    @Override
    protected boolean isValidBlock(BlockState state) {
        return false;
    }

    @Override
    public void createResult() {
        this.resultSlots.setItem(0, ItemStack.EMPTY);
        this.operation = AnvilOperation.NONE;
        this.materialToConsume = 0;
        this.powderToConsume = 0;
        this.repairMaterialsToConsume = 0;

        ItemStack left = this.inputSlots.getItem(0);
        ItemStack right = this.inputSlots.getItem(1);

        if (left.isEmpty()) {
            return;
        }

        if (tryCreateForgingResult(left, right)) {
            return;
        }

        if (tryCreateMaterialRepair(left, right)) {
            return;
        }

        tryCreateItemRepair(left, right);
    }

    private boolean tryCreateForgingResult(
            ItemStack materialStack,
            ItemStack powderStack
    ) {
        if (this.selection == null) {
            return false;
        }

        ForgingMaterial material =
                ForgingMaterial.fromStack(materialStack);

        if (material == null || !material.allowedInStoneAnvil()) {
            return false;
        }

        ForgingCost cost = ForgingRules.calculateCost(
                this.selection,
                material,
                powderStack
        );

        if (cost == null) {
            return false;
        }

        if (materialStack.getCount() < cost.materialCount()
                || powderStack.getCount() < cost.powderCount()) {
            return false;
        }

        Item result = ForgingRules.getResult(
                material,
                this.selection
        );

        this.materialToConsume = cost.materialCount();
        this.powderToConsume = cost.powderCount();
        this.operation = AnvilOperation.FORGING;

        this.resultSlots.setItem(
                0,
                new ItemStack(result)
        );

        this.broadcastChanges();
        return true;
    }

    private boolean tryCreateMaterialRepair(
            ItemStack left,
            ItemStack right
    ) {
        if (!ForgingRules.canRepairInStoneAnvil(left)) {
            return false;
        }

        if (!right.is(Items.COPPER_INGOT)) {
            return false;
        }

        if (!left.isDamageableItem() || !left.isDamaged()) {
            return false;
        }

        ItemStack repaired = left.copy();
        int used = 0;

        while (repaired.isDamaged() && used < right.getCount()) {
            int repairAmount = Math.min(
                    repaired.getDamageValue(),
                    repaired.getMaxDamage() / 4
            );

            repaired.setDamageValue(
                    repaired.getDamageValue() - repairAmount
            );

            used++;
        }

        this.repairMaterialsToConsume = used;
        this.operation = AnvilOperation.MATERIAL_REPAIR;
        this.resultSlots.setItem(0, repaired);
        this.broadcastChanges();

        return true;
    }

    private boolean tryCreateItemRepair(
            ItemStack left,
            ItemStack right
    ) {
        if (!ForgingRules.canRepairInStoneAnvil(left)) {
            return false;
        }

        if (!left.is(right.getItem())) {
            return false;
        }

        if (!left.isDamageableItem() || right.isEmpty()) {
            return false;
        }

        if (!EnchantmentHelper
                .getEnchantmentsForCrafting(right)
                .isEmpty()) {
            return false;
        }

        int leftDurability =
                left.getMaxDamage() - left.getDamageValue();

        int rightDurability =
                right.getMaxDamage() - right.getDamageValue();

        int bonus = left.getMaxDamage() * 12 / 100;

        int combinedDurability = Math.min(
                left.getMaxDamage(),
                leftDurability + rightDurability + bonus
        );

        ItemStack repaired = left.copy();
        repaired.setDamageValue(
                repaired.getMaxDamage() - combinedDurability
        );

        if (repaired.getDamageValue() >= left.getDamageValue()) {
            return false;
        }

        this.operation = AnvilOperation.ITEM_REPAIR;
        this.resultSlots.setItem(0, repaired);
        this.broadcastChanges();

        return true;
    }

    @Override
    protected boolean mayPickup(
            Player player,
            boolean hasResult
    ) {
        return hasResult && this.operation != AnvilOperation.NONE;
    }

    this.access.execute((level,pos)->
            level.levelEvent(1030,pos,0)
            );

}
