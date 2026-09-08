package net.emil.enhancedearlygame.client;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.emil.enhancedearlygame.client.screen.StoneAnvilScreen;
import net.emil.enhancedearlygame.registry.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(
        modid = EnhancedEarlygame.MODID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class ClientModEvents {
    private ClientModEvents() {
    }

    @SubscribeEvent
    public static void registerScreens(
            RegisterMenuScreensEvent event
    ) {
        event.register(
                ModMenus.STONE_ANVIL.get(),
                StoneAnvilScreen::new
        );
    }
}