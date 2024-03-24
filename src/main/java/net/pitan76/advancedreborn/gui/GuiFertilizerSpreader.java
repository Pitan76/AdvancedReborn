package net.pitan76.advancedreborn.gui;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.pitan76.advancedreborn.tile.FertilizerSpreaderTile;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.screen.builder.BuiltScreenHandler;

public class GuiFertilizerSpreader extends GuiBase<BuiltScreenHandler> {

    public FertilizerSpreaderTile tile;
    public GuiFertilizerSpreader(int syncId, PlayerEntity player, FertilizerSpreaderTile tile) {
        super(player, tile, tile.createScreenHandler(syncId, player));
        this.tile = tile;
    }

    public boolean isConfigEnabled() {
        return true;
    }

    public void init() {
        super.init();
    }

    public void drawBackground(MatrixStack matrixStack, float lastFrameDuration, int mouseX, int mouseY) {
        super.drawBackground(matrixStack, lastFrameDuration, mouseX, mouseY);
        Layer layer = Layer.BACKGROUND;
        drawSlot(matrixStack, 55, 32, layer);
        drawSlot(matrixStack, 73, 32, layer);
        drawSlot(matrixStack, 91, 32, layer);
        drawSlot(matrixStack, 109, 32, layer);
        drawSlot(matrixStack, 127, 32, layer);
        drawSlot(matrixStack, 55, 50, layer);
        drawSlot(matrixStack, 73, 50, layer);
        drawSlot(matrixStack, 91, 50, layer);
        drawSlot(matrixStack, 109, 50, layer);
        drawSlot(matrixStack, 127, 50, layer);

        drawSlot(matrixStack, 8, 72, layer);
    }

    public void drawForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.drawForeground(matrixStack, mouseX, mouseY);
        Layer layer = Layer.FOREGROUND;
        builder.drawMultiEnergyBar(matrixStack, this, 9, 19, (int) tile.getEnergy(), (int) tile.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }
}