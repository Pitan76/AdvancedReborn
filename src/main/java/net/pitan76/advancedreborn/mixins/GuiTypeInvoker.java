package net.pitan76.advancedreborn.mixins;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import techreborn.blockentity.GuiType;

@Mixin(GuiType.class)
public interface GuiTypeInvoker {
    @Invoker(value = "register", remap = false)
    static <T extends BlockEntity> GuiType<T> register(String path) {
        throw new AssertionError();
    }
}
