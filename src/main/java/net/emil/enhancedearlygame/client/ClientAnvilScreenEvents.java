package net.emil.enhancedearlygame.client;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.emil.enhancedearlygame.client.screen.EnhancedAnvilScreen;
import net.emil.enhancedearlygame.menu.EnhancedAnvilMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.minecraft.network.chat.Component;

@EventBusSubscriber(
        modid = EnhancedEarlygame.MODID,
        bus = EventBusSubscriber.Bus.GAME,
        value = Dist.CLIENT
)
public final class ClientAnvilScreenEvents {

    private ClientAnvilScreenEvents() {
    }

    @SubscribeEvent
    public static void replaceVanillaAnvilScreen(
            ScreenEvent.Opening event
    ) {
        Screen newScreen =
                event.getNewScreen();

        /*
         * Nur die exakte Vanilla-AnvilScreen-Klasse ersetzen.
         *
         * Dadurch wird der eigene EnhancedAnvilScreen
         * nicht erneut ersetzt.
         */
        if (newScreen == null
                || newScreen.getClass()
                != AnvilScreen.class) {
            return;
        }

        Minecraft minecraft =
                Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        AnvilScreen vanillaScreen =
                (AnvilScreen) newScreen;

        Inventory inventory =
                minecraft.player.getInventory();

        int containerId =
                vanillaScreen
                        .getMenu()
                        .containerId;

        /*
         * Clientseitig ebenfalls EnhancedAnvilMenu verwenden.
         *
         * Dadurch kennt auch der Client kostenlose Outputs
         * und erlaubt deren Entnahme.
         */
        EnhancedAnvilMenu enhancedMenu =
                new EnhancedAnvilMenu(
                        containerId,
                        inventory
                );

        minecraft.player.containerMenu =
                enhancedMenu;

        event.setNewScreen(
                new EnhancedAnvilScreen(
                        enhancedMenu,
                        inventory,
                        Component.translatable(
                                "container.enhancedearlygame.repair_and_forge"
                        )
                )
        );
    }
}
