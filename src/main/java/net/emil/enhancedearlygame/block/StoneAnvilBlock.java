package net.emil.enhancedearlygame.block;

import net.minecraft.world.level.block.Block;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
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
}
