package net.emil.enhancedearlygame.item;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public final class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(
                    Registries.ARMOR_MATERIAL,
                    EnhancedEarlygame.MODID
            );

    public static final Holder<ArmorMaterial> COPPER =
            ARMOR_MATERIALS.register(
                    "copper",
                    () -> new ArmorMaterial(
                            Util.make(
                                    new EnumMap<>(ArmorItem.Type.class),
                                    defense -> {
                                        defense.put(ArmorItem.Type.BOOTS, 2);
                                        defense.put(ArmorItem.Type.LEGGINGS, 4);
                                        defense.put(ArmorItem.Type.CHESTPLATE, 5);
                                        defense.put(ArmorItem.Type.HELMET, 2);
                                        defense.put(ArmorItem.Type.BODY, 4);
                                    }
                            ),

                            // Enchantability
                            15,

                            // Equip sound
                            SoundEvents.ARMOR_EQUIP_GENERIC,

                            // Repair ingredient
                            () -> Ingredient.of(Tags.Items.INGOTS_COPPER),

                            // Getragene Rüstungstextur
                            List.of(
                                    new ArmorMaterial.Layer(
                                            ResourceLocation.fromNamespaceAndPath(
                                                    EnhancedEarlygame.MODID,
                                                    "copper"
                                            )
                                    )
                            ),

                            // Toughness
                            0.0F,

                            // Knockback resistance
                            0.0F
                    )
            );

    public static void register(IEventBus modEventBus) {
        ARMOR_MATERIALS.register(modEventBus);
    }

    private ModArmorMaterials() {
    }
}

