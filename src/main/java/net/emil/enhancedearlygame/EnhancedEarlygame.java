package net.emil.enhancedearlygame;

import net.emil.enhancedearlygame.item.ModArmorMaterials;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.emil.enhancedearlygame.registry.ModItems;
import net.emil.enhancedearlygame.registry.ModBlocks;

@Mod(EnhancedEarlygame.MODID)
public class EnhancedEarlygame {

    public static final String MODID = "enhancedearlygame";

    public static final Logger LOGGER = LogUtils.getLogger();


    public EnhancedEarlygame(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModArmorMaterials.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModItems.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {

            event.accept(ModItems.COPPER_PICKAXE.get());
            event.accept(ModItems.COPPER_AXE.get());
            event.accept(ModItems.COPPER_SHOVEL.get());
            event.accept(ModItems.COPPER_HOE.get());
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {

            event.accept(ModItems.COPPER_SWORD.get());
            event.accept(ModItems.COPPER_HELMET.get());
            event.accept(ModItems.COPPER_CHESTPLATE.get());
            event.accept(ModItems.COPPER_LEGGINGS.get());
            event.accept(ModItems.COPPER_BOOTS.get());
        }

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {

            // Copper
            event.accept(ModItems.COPPER_PICKAXE_HEAD.get());
            event.accept(ModItems.COPPER_AXE_HEAD.get());
            event.accept(ModItems.COPPER_SHOVEL_HEAD.get());
            event.accept(ModItems.COPPER_HOE_HEAD.get());
            event.accept(ModItems.COPPER_SWORD_BLADE.get());

            // Iron
            event.accept(ModItems.IRON_PICKAXE_HEAD.get());
            event.accept(ModItems.IRON_AXE_HEAD.get());
            event.accept(ModItems.IRON_SHOVEL_HEAD.get());
            event.accept(ModItems.IRON_HOE_HEAD.get());
            event.accept(ModItems.IRON_SWORD_BLADE.get());

            // Gold
            event.accept(ModItems.GOLD_PICKAXE_HEAD.get());
            event.accept(ModItems.GOLD_AXE_HEAD.get());
            event.accept(ModItems.GOLD_SHOVEL_HEAD.get());
            event.accept(ModItems.GOLD_HOE_HEAD.get());
            event.accept(ModItems.GOLD_SWORD_BLADE.get());

            // Diamond
            event.accept(ModItems.DIAMOND_PICKAXE_HEAD.get());
            event.accept(ModItems.DIAMOND_AXE_HEAD.get());
            event.accept(ModItems.DIAMOND_SHOVEL_HEAD.get());
            event.accept(ModItems.DIAMOND_HOE_HEAD.get());
            event.accept(ModItems.DIAMOND_SWORD_BLADE.get());

            // Smithing Powders
            event.accept(ModItems.SMITHING_POWDER.get());
            event.accept(ModItems.BLAZED_SMITHING_POWDER.get());

        }

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModItems.STONE_ANVIL.get());
        }

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
