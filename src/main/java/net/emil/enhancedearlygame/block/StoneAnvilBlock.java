package net.emil.enhancedearlygame.block;

import net.emil.enhancedearlygame.menu.StoneAnvilMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StoneAnvilBlock extends Block {
    public static final MapCodec<StoneAnvilBlock> CODEC =
            simpleCodec(StoneAnvilBlock::new);

    private static final VoxelShape SHAPE =
            Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public StoneAnvilBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return SHAPE;
    }

    @Override
    public MenuProvider getMenuProvider(
            BlockState state,
            Level level,
            BlockPos pos
    ) {
        return new SimpleMenuProvider(
                (containerId, inventory, player) ->
                        new StoneAnvilMenu(
                                containerId,
                                inventory,
                                ContainerLevelAccess.create(level, pos)
                        ),
                Component.translatable(
                        "container.enhancedearlygame.repair_and_forge"
                )
        );
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        if (!level.isClientSide
                && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(
                    state.getMenuProvider(level, pos)
            );
        }

        return InteractionResult.sidedSuccess(
                level.isClientSide
        );
    }


}
