package net.pitan76.advancedreborn.renderer;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.render.entity.state.TntEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.entities.IndustrialTNTEntity;
import net.pitan76.mcpitanlib.api.util.MathUtil;
import net.pitan76.mcpitanlib.api.util.client.MatrixStackUtil;

public class IndustrialTNTEntityRenderer extends EntityRenderer<IndustrialTNTEntity, TntEntityRenderState> {

    public IndustrialTNTEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
    }

    @Override
    public TntEntityRenderState createRenderState() {
        return new TntEntityRenderState();
    }

    @Override
    public void render(TntEntityRenderState tntEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        MatrixStackUtil.push(matrixStack);
        MatrixStackUtil.translate(matrixStack, 0.0F, 0.5F, 0.0F);
        float f = tntEntityRenderState.fuse;
        if (f < 10.0F) {
            float g = 1.0F - f / 10.0F;
            g = MathHelper.clamp(g, 0.0F, 1.0F);
            g *= g;
            g *= g;
            float h = 1.0F + g * 0.3F;
            MatrixStackUtil.scale(matrixStack, h, h, h);
        }

        MatrixStackUtil.multiply(matrixStack, MathUtil.RotationAxisType.POSITIVE_Y, -90.0F);
        MatrixStackUtil.translate(matrixStack, -0.5F, -0.5F, 0.5F);
        MatrixStackUtil.multiply(matrixStack, MathUtil.RotationAxisType.POSITIVE_Y, 90.0F);
        if (tntEntityRenderState.blockState != null) {
            TntMinecartEntityRenderer.renderFlashingBlock(Blocks.INDUSTRIAL_TNT.get().getDefaultState(), matrixStack, orderedRenderCommandQueue, tntEntityRenderState.light, (int)f / 5 % 2 == 0, tntEntityRenderState.outlineColor);
        }

        MatrixStackUtil.pop(matrixStack);
        super.render(tntEntityRenderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
    }
}
