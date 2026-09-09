package net.emil.enhancedearlygame.event;

import net.emil.enhancedearlygame.EnhancedEarlygame;
import net.emil.enhancedearlygame.menu.EnhancedAnvilMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(
        modid = EnhancedEarlygame.MODID,
        bus = EventBusSubscriber.Bus.GAME
)
public final class VanillaAnvilInteractionEvents {

    private VanillaAnvilInteractionEvents() {
    }

    @SubscribeEvent
    public static void openEnhancedAnvil(
            PlayerInteractEvent.RightClickBlock event
    ) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Level level = event.getLevel();
        Player player = event.getEntity();

        BlockState state =
                level.getBlockState(event.getPos());

        boolean isVanillaAnvil =
                state.is(Blocks.ANVIL)
                        || state.is(Blocks.CHIPPED_ANVIL)
                        || state.is(Blocks.DAMAGED_ANVIL);

        if (!isVanillaAnvil) {
            return;
        }

        /*
         * Beim Schleichen darf die normale Item-Interaktion
         * weiterhin stattfinden.
         */
        if (player.isSecondaryUseActive()) {
            return;
        }

        /*
         * Verhindert, dass anschließend zusätzlich das normale
         * Vanilla-AnvilMenu geöffnet wird.
         */
        event.setCancellationResult(
                InteractionResult.sidedSuccess(
                        level.isClientSide
                )
        );
        event.setCanceled(true);

        /*
         * Das eigentliche Menü wird ausschließlich auf dem Server
         * geöffnet. Der Server sendet es danach an den Client.
         */
        if (level.isClientSide) {
            return;
        }

        player.openMenu(
                new SimpleMenuProvider(
                        (
                                containerId,
                                inventory,
                                menuPlayer
                        ) -> new EnhancedAnvilMenu(
                                containerId,
                                inventory,
                                ContainerLevelAccess.create(
                                        level,
                                        event.getPos()
                                )
                        ),
                        Component.translatable(
                                "container.enhancedearlygame.repair_and_forge"
                        )
                )
        );

        player.awardStat(
                Stats.INTERACT_WITH_ANVIL
        );
    }
}