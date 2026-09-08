package net.emil.enhancedearlygame.registry;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.emil.enhancedearlygame.menu.StoneAnvilMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(
                    Registries.MENU,
                    EnhancedEarlygame.MODID
            );

    public static final DeferredHolder<
            MenuType<?>,
            MenuType<StoneAnvilMenu>
            > STONE_ANVIL =
            MENUS.register(
                    "stone_anvil",
                    () -> new MenuType<>(
                            StoneAnvilMenu::new,
                            FeatureFlags.DEFAULT_FLAGS
                    )
            );
    private ModMenus() {
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
