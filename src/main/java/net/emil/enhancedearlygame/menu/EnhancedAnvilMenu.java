package net.emil.enhancedearlygame.menu;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.emil.enhancedearlygame.forging.AnvilOperation;
import net.emil.enhancedearlygame.forging.ForgingCost;
import net.emil.enhancedearlygame.forging.ForgingMaterial;
import net.emil.enhancedearlygame.forging.ForgingRules;
import net.emil.enhancedearlygame.forging.ForgingSelection;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.Nullable;

public class EnhancedAnvilMenu extends AnvilMenu {

    @Nullable
    private ForgingSelection selection;

    private AnvilOperation operation =
            AnvilOperation.NONE;

    private int materialToConsume;
    private int powderToConsume;

    @Nullable
    private String requestedName;

    public EnhancedAnvilMenu(
            int containerId,
            Inventory inventory
    ) {
        this(
                containerId,
                inventory,
                ContainerLevelAccess.NULL
        );
    }

    public EnhancedAnvilMenu(
            int containerId,
            Inventory inventory,
            ContainerLevelAccess access
    ) {
        super(
                containerId,
                inventory,
                access
        );
    }

    @Override
    protected ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(
                        0,
                        16,
                        50,
                        stack -> true
                )
                .withSlot(
                        1,
                        65,
                        50,
                        stack -> true
                )
                .withResultSlot(
                        2,
                        123,
                        50
                )
                .build();
    }

    /*
     * Wird aufgerufen, wenn der Spieler im Forging-Widget
     * eine der neun Optionen auswählt.
     */
    @Override
    public boolean clickMenuButton(
            Player player,
            int buttonId
    ) {
        if (buttonId
                == ForgingSelection.CLEAR_SELECTION_BUTTON_ID) {
            this.selection = null;
            this.createResult();
            return true;
        }


        ForgingSelection clickedSelection =
                ForgingSelection.byButtonId(buttonId);

        if (clickedSelection == null) {
            return false;
        }

        this.selection = clickedSelection;
        this.createResult();

        return true;
    }

    /*
     * Speichert den Text des Rename-Feldes.
     *
     * Der Vanilla-Rename-Packet funktioniert weiterhin, weil
     * diese Klasse AnvilMenu erweitert.
     */
    @Override
    public boolean setItemName(String name) {
        String filteredName =
                StringUtil.filterText(name);

        if (filteredName.length() > MAX_NAME_LENGTH) {
            return false;
        }

        if (filteredName.equals(this.requestedName)) {
            return false;
        }

        this.requestedName = filteredName;
        this.createResult();

        return true;
    }

    /*
     * Berechnet den aktuellen Output.
     *
     * Reihenfolge:
     *
     * 1. Forging
     * 2. Vanilla Repair
     * 3. Item-Kombination
     * 4. Enchantments
     * 5. Rename
     */
    @Override
    public void createResult() {
        this.resultSlots.setItem(
                0,
                ItemStack.EMPTY
        );

        this.operation = AnvilOperation.NONE;
        this.materialToConsume = 0;
        this.powderToConsume = 0;
        this.repairItemCountCost = 0;
        this.setMaximumCost(0);

        ItemStack left =
                this.inputSlots.getItem(0);

        ItemStack right =
                this.inputSlots.getItem(1);

        if (left.isEmpty()) {
            return;
        }

        /*
         * Forging wird vor der Vanilla-Logik geprüft.
         */
        if (tryCreateForgingResult(left, right)) {
            return;
        }

        /*
         * Eine Kopie des linken Items wird bearbeitet.
         * Dadurch bleiben seine Komponenten und Enchantments erhalten.
         */
        ItemStack output = left.copy();

        ItemEnchantments.Mutable outputEnchantments =
                new ItemEnchantments.Mutable(
                        EnchantmentHelper
                                .getEnchantmentsForCrafting(left)
                );

        long previousWorkCost =
                left.getOrDefault(
                        DataComponents.REPAIR_COST,
                        0
                );

        previousWorkCost +=
                right.getOrDefault(
                        DataComponents.REPAIR_COST,
                        0
                );

        boolean resultChanged = false;
        boolean materialRepair = false;
        boolean itemRepair = false;
        boolean enchantmentsChanged = false;
        boolean renamed = false;

        int enchantmentCost = 0;

        /*
         * Verarbeitung des rechten Slots.
         */
        if (!right.isEmpty()) {
            boolean rightContainsStoredEnchantments =
                    right.has(
                            DataComponents.STORED_ENCHANTMENTS
                    );

            /*
             * Reparatur mit dem normalen Reparaturmaterial.
             *
             * Beispiele:
             * Iron Pickaxe + Iron Ingot
             * Diamond Pickaxe + Diamond
             */
            if (output.isDamageableItem()
                    && output.getItem()
                    .isValidRepairItem(left, right)) {

                int repairAmount = Math.min(
                        output.getDamageValue(),
                        output.getMaxDamage() / 4
                );

                if (repairAmount <= 0) {
                    clearResult();
                    return;
                }

                int materialsUsed = 0;

                while (repairAmount > 0
                        && materialsUsed < right.getCount()) {

                    output.setDamageValue(
                            output.getDamageValue()
                                    - repairAmount
                    );

                    materialsUsed++;

                    repairAmount = Math.min(
                            output.getDamageValue(),
                            output.getMaxDamage() / 4
                    );
                }

                this.repairItemCountCost =
                        materialsUsed;

                resultChanged = materialsUsed > 0;
                materialRepair = resultChanged;
            } else {
                /*
                 * Wenn das rechte Item kein Enchanted Book ist,
                 * muss es dasselbe beschädigbare Item sein.
                 */
                if (!rightContainsStoredEnchantments
                        && (!left.is(right.getItem())
                        || !left.isDamageableItem())) {
                    clearResult();
                    return;
                }

                /*
                 * Zwei gleiche Tools oder Rüstungsteile kombinieren.
                 */
                if (left.isDamageableItem()
                        && !rightContainsStoredEnchantments) {

                    int leftDurability =
                            left.getMaxDamage()
                                    - left.getDamageValue();

                    int rightDurability =
                            right.getMaxDamage()
                                    - right.getDamageValue();

                    int repairBonus =
                            left.getMaxDamage() * 12 / 100;

                    int combinedDurability =
                            leftDurability
                                    + rightDurability
                                    + repairBonus;

                    int newDamage =
                            left.getMaxDamage()
                                    - combinedDurability;

                    newDamage = Math.max(
                            0,
                            newDamage
                    );

                    if (newDamage
                            < output.getDamageValue()) {
                        output.setDamageValue(newDamage);
                        resultChanged = true;
                        itemRepair = true;
                    }
                }

                /*
                 * Enchantments des rechten Items übernehmen.
                 *
                 * Nur dieser Abschnitt erzeugt XP-Kosten.
                 */
                ItemEnchantments rightEnchantments =
                        EnchantmentHelper
                                .getEnchantmentsForCrafting(right);

                boolean compatibleEnchantmentFound = false;
                boolean incompatibleEnchantmentFound = false;

                for (Entry<Holder<Enchantment>> entry
                        : rightEnchantments.entrySet()) {

                    Holder<Enchantment> enchantmentHolder =
                            entry.getKey();

                    int oldLevel =
                            outputEnchantments.getLevel(
                                    enchantmentHolder
                            );

                    int rightLevel =
                            entry.getIntValue();

                    int newLevel;

                    if (oldLevel == rightLevel) {
                        newLevel = rightLevel + 1;
                    } else {
                        newLevel = Math.max(
                                oldLevel,
                                rightLevel
                        );
                    }

                    Enchantment enchantment =
                            enchantmentHolder.value();

                    boolean compatible =
                            left.supportsEnchantment(
                                    enchantmentHolder
                            );

                    if (this.player
                            .getAbilities()
                            .instabuild) {
                        compatible = true;
                    }

                    for (Holder<Enchantment>
                            existingEnchantment
                            : outputEnchantments.keySet()) {

                        if (!existingEnchantment.equals(
                                enchantmentHolder
                        ) && !Enchantment.areCompatible(
                                enchantmentHolder,
                                existingEnchantment
                        )) {
                            compatible = false;
                            enchantmentCost++;
                        }
                    }

                    if (!compatible) {
                        incompatibleEnchantmentFound = true;
                        continue;
                    }

                    compatibleEnchantmentFound = true;

                    newLevel = Math.min(
                            newLevel,
                            enchantment.getMaxLevel()
                    );

                    outputEnchantments.set(
                            enchantmentHolder,
                            newLevel
                    );

                    int singleEnchantmentCost =
                            enchantment.getAnvilCost();

                    if (rightContainsStoredEnchantments) {
                        singleEnchantmentCost =
                                Math.max(
                                        1,
                                        singleEnchantmentCost / 2
                                );
                    }

                    enchantmentCost +=
                            singleEnchantmentCost * newLevel;

                    enchantmentsChanged = true;
                    resultChanged = true;
                }

                if (incompatibleEnchantmentFound
                        && !compatibleEnchantmentFound
                        && !itemRepair) {
                    clearResult();
                    return;
                }

                if (rightContainsStoredEnchantments
                        && !output.isBookEnchantable(right)) {
                    clearResult();
                    return;
                }
            }
        }

        /*
         * Rename beziehungsweise Entfernen eines eigenen Namens.
         */
        if (this.requestedName != null
                && !StringUtil.isBlank(
                this.requestedName
        )) {
            if (!this.requestedName.equals(
                    left.getHoverName().getString()
            )) {
                output.set(
                        DataComponents.CUSTOM_NAME,
                        Component.literal(
                                this.requestedName
                        )
                );

                renamed = true;
                resultChanged = true;
            }
        } else if (left.has(
                DataComponents.CUSTOM_NAME
        )) {
            output.remove(
                    DataComponents.CUSTOM_NAME
            );

            renamed = true;
            resultChanged = true;
        }

        /*
         * Ohne tatsächliche Änderung darf kein Ergebnis erscheinen.
         */
        if (!resultChanged) {
            clearResult();
            return;
        }

        /*
         * Die neuen Enchantments auf den Output schreiben.
         */
        EnchantmentHelper.setEnchantments(
                output,
                outputEnchantments.toImmutable()
        );

        /*
         * Operation bestimmen und Kosten festlegen.
         */
        if (enchantmentsChanged) {
            this.operation =
                    AnvilOperation.ENCHANTING;

            int finalEnchantmentCost =
                    (int) Mth.clamp(
                            previousWorkCost
                                    + enchantmentCost,
                            1L,
                            Integer.MAX_VALUE
                    );

            this.setMaximumCost(
                    finalEnchantmentCost
            );

            /*
             * Too Expensive gilt ausschließlich für
             * Enchantment-Aktionen.
             */
            if (finalEnchantmentCost >= 40
                    && !this.player
                    .getAbilities()
                    .instabuild) {
                clearResult();
                return;
            }
        } else if (materialRepair) {
            this.operation =
                    AnvilOperation.MATERIAL_REPAIR;

            this.setMaximumCost(0);
        } else if (itemRepair) {
            this.operation =
                    AnvilOperation.ITEM_REPAIR;

            this.setMaximumCost(0);
        } else if (renamed) {
            this.operation =
                    AnvilOperation.RENAME;

            this.setMaximumCost(0);
        } else {
            clearResult();
            return;
        }

        /*
         * Vanilla-Repair-Cost auf dem Item erhöhen,
         * wenn mehr als nur der Name verändert wurde.
         */
        if (!renamed
                || materialRepair
                || itemRepair
                || enchantmentsChanged) {

            int outputRepairCost =
                    Math.max(
                            left.getOrDefault(
                                    DataComponents.REPAIR_COST,
                                    0
                            ),
                            right.getOrDefault(
                                    DataComponents.REPAIR_COST,
                                    0
                            )
                    );

            outputRepairCost =
                    AnvilMenu.calculateIncreasedRepairCost(
                            outputRepairCost
                    );

            output.set(
                    DataComponents.REPAIR_COST,
                    outputRepairCost
            );
        }

        this.resultSlots.setItem(
                0,
                output
        );

        this.broadcastChanges();
    }

    /*
     * Versucht, aus Material und Powder das ausgewählte
     * Forging-Ergebnis herzustellen.
     */
    private boolean tryCreateForgingResult(
            ItemStack materialStack,
            ItemStack powderStack
    ) {
        if (this.selection == null) {
            return false;
        }

        ForgingMaterial material =
                ForgingMaterial.fromStack(
                        materialStack
                );

        /*
         * Der normale Anvil akzeptiert alle Materialien:
         * Copper, Iron, Gold und Diamond.
         */
        if (material == null) {
            return false;
        }

        ForgingCost cost =
                ForgingRules.calculateCost(
                        this.selection,
                        material,
                        powderStack
                );

        if (cost == null) {
            return false;
        }

        if (materialStack.getCount()
                < cost.materialCount()) {
            return false;
        }

        if (powderStack.getCount()
                < cost.powderCount()) {
            return false;
        }

        Item result =
                ForgingRules.getResult(
                        material,
                        this.selection
                );

        this.materialToConsume =
                cost.materialCount();

        this.powderToConsume =
                cost.powderCount();

        this.operation =
                AnvilOperation.FORGING;

        this.setMaximumCost(0);

        this.resultSlots.setItem(
                0,
                new ItemStack(result)
        );

        this.broadcastChanges();

        return true;
    }

    /*
     * Entfernt einen ungültigen Output.
     */
    private void clearResult() {
        this.operation =
                AnvilOperation.NONE;

        this.setMaximumCost(0);

        this.resultSlots.setItem(
                0,
                ItemStack.EMPTY
        );

        this.broadcastChanges();
    }

    /*
     * Kostenlose Ergebnisse dürfen herausgenommen werden.
     * Nur Enchanting überprüft das XP-Level.
     */
    @Override
    protected boolean mayPickup(
            Player player,
            boolean hasResult
    ) {
        if (!hasResult
                || this.operation
                == AnvilOperation.NONE) {
            return false;
        }

        if (this.operation
                != AnvilOperation.ENCHANTING) {
            return true;
        }

        return player.hasInfiniteMaterials()
                || player.experienceLevel
                >= this.getCost();
    }

    /*
     * Wird ausgeführt, wenn der Spieler das fertige
     * Ergebnis aus dem Output-Slot nimmt.
     */
    @Override
    protected void onTake(
            Player player,
            ItemStack output
    ) {
        AnvilOperation finishedOperation =
                this.operation;

        int finishedMaterialCost =
                this.materialToConsume;

        int finishedPowderCost =
                this.powderToConsume;

        int finishedExperienceCost =
                this.getCost();

        ItemStack leftInput =
                this.inputSlots.getItem(0).copy();

        ItemStack rightInput =
                this.inputSlots.getItem(1).copy();

        /*
         * XP ausschließlich bei Enchantment-Aktionen.
         */
        if (finishedOperation
                == AnvilOperation.ENCHANTING
                && !player.getAbilities().instabuild) {
            player.giveExperienceLevels(
                    -finishedExperienceCost
            );
        }

        /*
         * Inputs verbrauchen.
         */
        if (finishedOperation
                == AnvilOperation.FORGING) {

            this.inputSlots.removeItem(
                    0,
                    finishedMaterialCost
            );

            this.inputSlots.removeItem(
                    1,
                    finishedPowderCost
            );
        } else {
            this.inputSlots.setItem(
                    0,
                    ItemStack.EMPTY
            );

            if (this.repairItemCountCost > 0) {
                ItemStack right =
                        this.inputSlots.getItem(1);

                if (right.getCount()
                        > this.repairItemCountCost) {
                    right.shrink(
                            this.repairItemCountCost
                    );

                    this.inputSlots.setItem(
                            1,
                            right
                    );
                } else {
                    this.inputSlots.setItem(
                            1,
                            ItemStack.EMPTY
                    );
                }
            } else {
                this.inputSlots.setItem(
                        1,
                        ItemStack.EMPTY
                );
            }
        }

        this.setMaximumCost(0);

        /*
         * NeoForge darf die Anvil-Beschädigungschance
         * über AnvilRepairEvent anpassen.
         */
        float breakChance =
                CommonHooks.onAnvilRepair(
                        player,
                        output,
                        leftInput,
                        rightInput
                );

        damageAnvil(
                player,
                breakChance
        );
    }

    /*
     * Gewünschte Schadensfolge:
     *
     * Anvil -> Damaged Anvil -> kaputt
     *
     * Chipped Anvil wird nur unterstützt, damit bereits
     * existierende Chipped Anvils weiter funktionieren.
     */
    private void damageAnvil(
            Player player,
            float breakChance
    ) {
        this.access.execute(
                (level, pos) -> {
                    BlockState state =
                            level.getBlockState(pos);

                    if (player.getAbilities().instabuild
                            || !state.is(BlockTags.ANVIL)
                            || player.getRandom().nextFloat()
                            >= breakChance) {

                        level.levelEvent(
                                1030,
                                pos,
                                0
                        );

                        return;
                    }

                    if (state.is(Blocks.ANVIL)
                            || state.is(
                            Blocks.CHIPPED_ANVIL
                    )) {
                        BlockState damagedState =
                                Blocks.DAMAGED_ANVIL
                                        .defaultBlockState()
                                        .setValue(
                                                AnvilBlock.FACING,
                                                state.getValue(
                                                        AnvilBlock.FACING
                                                )
                                        );

                        level.setBlock(
                                pos,
                                damagedState,
                                2
                        );

                        level.levelEvent(
                                1030,
                                pos,
                                0
                        );

                        return;
                    }

                    if (state.is(
                            Blocks.DAMAGED_ANVIL
                    )) {
                        level.removeBlock(
                                pos,
                                false
                        );

                        level.levelEvent(
                                1029,
                                pos,
                                0
                        );
                    }
                }
        );
    }
}

