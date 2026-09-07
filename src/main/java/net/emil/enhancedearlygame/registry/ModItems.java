package net.emil.enhancedearlygame.registry;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.emil.enhancedearlygame.item.ModToolTiers;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.emil.enhancedearlygame.item.ModArmorMaterials;
import net.minecraft.world.item.ArmorItem;

public final class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(EnhancedEarlygame.MODID);

    public static final DeferredItem<PickaxeItem> COPPER_PICKAXE =
            ITEMS.register(
                    "copper_pickaxe",
                    () -> new PickaxeItem(
                            ModToolTiers.COPPER,
                            new Item.Properties().attributes(
                                    PickaxeItem.createAttributes(
                                            ModToolTiers.COPPER,
                                            1.0F,
                                            -2.8F
                                    )
                            )
                    )
            );

    public static final DeferredItem<AxeItem> COPPER_AXE =
            ITEMS.register(
                    "copper_axe",
                    () -> new AxeItem(
                            ModToolTiers.COPPER,
                            new Item.Properties().attributes(
                                    AxeItem.createAttributes(
                                            ModToolTiers.COPPER,
                                            6.0F,
                                            -3.1F
                                    )
                            )
                    )
            );

    public static final DeferredItem<ShovelItem> COPPER_SHOVEL =
            ITEMS.register(
                    "copper_shovel",
                    () -> new ShovelItem(
                            ModToolTiers.COPPER,
                            new Item.Properties().attributes(
                                    ShovelItem.createAttributes(
                                            ModToolTiers.COPPER,
                                            1.5F,
                                            -3.0F
                                    )
                            )
                    )
            );

    public static final DeferredItem<HoeItem> COPPER_HOE =
            ITEMS.register(
                    "copper_hoe",
                    () -> new HoeItem(
                            ModToolTiers.COPPER,
                            new Item.Properties().attributes(
                                    HoeItem.createAttributes(
                                            ModToolTiers.COPPER,
                                            -1.5F,
                                            -1.5F
                                    )
                            )
                    )
            );

    public static final DeferredItem<SwordItem> COPPER_SWORD =
            ITEMS.register(
                    "copper_sword",
                    () -> new SwordItem(
                            ModToolTiers.COPPER,
                            new Item.Properties().attributes(
                                    SwordItem.createAttributes(
                                            ModToolTiers.COPPER,
                                            3,
                                            -2.4F
                                    )
                            )
                    )
            );

    public static final DeferredItem<ArmorItem> COPPER_HELMET =
            ITEMS.register(
                    "copper_helmet",
                    () -> new ArmorItem(
                            ModArmorMaterials.COPPER,
                            ArmorItem.Type.HELMET,
                            new Item.Properties().durability(
                                    ArmorItem.Type.HELMET.getDurability(15)
                            )
                    )
            );

    public static final DeferredItem<ArmorItem> COPPER_CHESTPLATE =
            ITEMS.register(
                    "copper_chestplate",
                    () -> new ArmorItem(
                            ModArmorMaterials.COPPER,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().durability(
                                    ArmorItem.Type.CHESTPLATE.getDurability(15)
                            )
                    )
            );

    public static final DeferredItem<ArmorItem> COPPER_LEGGINGS =
            ITEMS.register(
                    "copper_leggings",
                    () -> new ArmorItem(
                            ModArmorMaterials.COPPER,
                            ArmorItem.Type.LEGGINGS,
                            new Item.Properties().durability(
                                    ArmorItem.Type.LEGGINGS.getDurability(15)
                            )
                    )
            );

    public static final DeferredItem<ArmorItem> COPPER_BOOTS =
            ITEMS.register(
                    "copper_boots",
                    () -> new ArmorItem(
                            ModArmorMaterials.COPPER,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().durability(
                                    ArmorItem.Type.BOOTS.getDurability(15)
                            )
                    )
            );



// COPPER TOOL PARTS


    public static final DeferredItem<Item> COPPER_PICKAXE_HEAD =
            registerToolPart("copper_pickaxe_head");

    public static final DeferredItem<Item> COPPER_AXE_HEAD =
            registerToolPart("copper_axe_head");

    public static final DeferredItem<Item> COPPER_SHOVEL_HEAD =
            registerToolPart("copper_shovel_head");

    public static final DeferredItem<Item> COPPER_HOE_HEAD =
            registerToolPart("copper_hoe_head");

    public static final DeferredItem<Item> COPPER_SWORD_BLADE =
            registerToolPart("copper_sword_blade");


// IRON TOOL PARTS


    public static final DeferredItem<Item> IRON_PICKAXE_HEAD =
            registerToolPart("iron_pickaxe_head");

    public static final DeferredItem<Item> IRON_AXE_HEAD =
            registerToolPart("iron_axe_head");

    public static final DeferredItem<Item> IRON_SHOVEL_HEAD =
            registerToolPart("iron_shovel_head");

    public static final DeferredItem<Item> IRON_HOE_HEAD =
            registerToolPart("iron_hoe_head");

    public static final DeferredItem<Item> IRON_SWORD_BLADE =
            registerToolPart("iron_sword_blade");


// GOLD TOOL PARTS


    public static final DeferredItem<Item> GOLD_PICKAXE_HEAD =
            registerToolPart("gold_pickaxe_head");

    public static final DeferredItem<Item> GOLD_AXE_HEAD =
            registerToolPart("gold_axe_head");

    public static final DeferredItem<Item> GOLD_SHOVEL_HEAD =
            registerToolPart("gold_shovel_head");

    public static final DeferredItem<Item> GOLD_HOE_HEAD =
            registerToolPart("gold_hoe_head");

    public static final DeferredItem<Item> GOLD_SWORD_BLADE =
            registerToolPart("gold_sword_blade");


// DIAMOND TOOL PARTS


    public static final DeferredItem<Item> DIAMOND_PICKAXE_HEAD =
            registerToolPart("diamond_pickaxe_head");

    public static final DeferredItem<Item> DIAMOND_AXE_HEAD =
            registerToolPart("diamond_axe_head");

    public static final DeferredItem<Item> DIAMOND_SHOVEL_HEAD =
            registerToolPart("diamond_shovel_head");

    public static final DeferredItem<Item> DIAMOND_HOE_HEAD =
            registerToolPart("diamond_hoe_head");

    public static final DeferredItem<Item> DIAMOND_SWORD_BLADE =
            registerToolPart("diamond_sword_blade");


    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

    private static DeferredItem<Item> registerToolPart(String name) {
        return ITEMS.register(
                name,
                () -> new Item(new Item.Properties())
        );
    }

    private ModItems() {
    }
}
