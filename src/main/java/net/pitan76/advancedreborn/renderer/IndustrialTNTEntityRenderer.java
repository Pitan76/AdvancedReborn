package net.pitan76.advancedreborn.renderer;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.pitan76.advancedreborn.entities.IndustrialTNTEntity;
import net.pitan76.mcpitanlib.api.util.MathUtil;
import net.pitan76.mcpitanlib.api.util.client.MatrixStackUtil;

public class IndustrialTNTEntityRenderer extends EntityRenderer<IndustrialTNTEntity, TntRenderState> {

    public IndustrialTNTEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
    }

    @Override
    public TntRenderState createRenderState() {
        return new TntRenderState();
    }

    @Override
    public void submit(TntRenderState state, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        MatrixStackUtil.push(matrixStack);
        MatrixStackUtil.translate(matrixStack, 0.0F, 0.5F, 0.0F);
        float f = state.fuseRemainingInTicks;
        if (f < 10.0F) {
            float g = 1.0F - f / 10.0F;
            g = Mth.clamp(g, 0.0F, 1.0F);
            g *= g;
            g *= g;
            float h = 1.0F + g * 0.3F;
            MatrixStackUtil.scale(matrixStack, h, h, h);
        }

        MatrixStackUtil.multiply(matrixStack, MathUtil.RotationAxisType.POSITIVE_Y, -90.0F);
        MatrixStackUtil.translate(matrixStack, -0.5F, -0.5F, 0.5F);
        MatrixStackUtil.multiply(matrixStack, MathUtil.RotationAxisType.POSITIVE_Y, 90.0F);

        if (!state.blockState.isEmpty()) {
            TntMinecartRenderer.submitWhiteSolidBlock(state.blockState, matrixStack, submitNodeCollector, state.lightCoords, (int)f / 5 % 2 == 0, state.outlineColor);
        }

        MatrixStackUtil.pop(matrixStack);
        super.submit(state, matrixStack, submitNodeCollector, cameraRenderState);
    }
}
