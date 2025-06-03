package net.pitan76.advancedreborn.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.render.entity.state.TntEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.pitan76.advancedreborn.Blocks;
import net.pitan76.advancedreborn.entities.IndustrialTNTEntity;
import net.pitan76.mcpitanlib.api.util.MathUtil;
import net.pitan76.mcpitanlib.api.util.client.MatrixStackUtil;

public class IndustrialTNTEntityRenderer extends EntityRenderer<IndustrialTNTEntity, TntEntityRenderState> {

    private final BlockRenderManager blockRenderManager;

    public IndustrialTNTEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        blockRenderManager = context.getBlockRenderManager();
        this.shadowRadius = 0.5F;
    }

    @Override
    public TntEntityRenderState createRenderState() {
        return new TntEntityRenderState();
    }

    public void render(TntEntityRenderState tntEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0D, 0.5D, 0.0D);
        if (tntEntityRenderState.fuse < 10.0F) {
            float h = 1.0F - tntEntityRenderState.fuse / 10.0F;
            h = MathHelper.clamp(h, 0.0F, 1.0F);
            h *= h;
            h *= h;
            float j = 1.0F + h * 0.3F;
            matrixStack.scale(j, j, j);
        }

        //matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
        MatrixStackUtil.multiply(matrixStack, MathUtil.RotationAxisType.POSITIVE_Y, -90.0F);
        matrixStack.translate(-0.5D, -0.5D, 0.5D);
        //matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        MatrixStackUtil.multiply(matrixStack, MathUtil.RotationAxisType.POSITIVE_Y, 90.0F);
        TntMinecartEntityRenderer.renderFlashingBlock(this.blockRenderManager, Blocks.INDUSTRIAL_TNT.get().getDefaultState(), matrixStack, vertexConsumerProvider, i, tntEntityRenderState.fuse / 5 % 2 == 0);
        matrixStack.pop();
        super.render(tntEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }
}
