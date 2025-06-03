package net.pitan76.advancedreborn.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.pitan76.advancedreborn.tile.CentrifugalExtractorTile;
import net.pitan76.mcpitanlib.api.client.render.DrawObjectDM;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.screen.BuiltScreenHandler;

public class GuiCentrifugalExtractor extends GuiBase<BuiltScreenHandler> {

    public CentrifugalExtractorTile tile;
    public GuiCentrifugalExtractor(int syncId, PlayerEntity player, CentrifugalExtractorTile tile) {
        super(player, tile, tile.createScreenHandler(syncId, player));
        this.tile = tile;
        backgroundWidth = 177;
        backgroundHeight = 167;
    }

    public boolean isConfigEnabled() {
        return true;
    }

    public void init() {
        super.init();
    }

    public void drawBackground(DrawContext context, float lastFrameDuration, int mouseX, int mouseY) {
        super.drawBackground(context, lastFrameDuration, mouseX, mouseY);
        Layer layer = Layer.BACKGROUND;
        drawSlot(context, 55, 45, layer);
        drawOutputThreeLongSlot(context, 101, 45, layer);
        drawSlot(context, 8, 72, layer);
        drawText(context, TextUtil.translatable("advanced_reborn.advanced_machine.text.speed", tile.getHeatPer() + "%"), 75, 70, 0, layer);
    }

    public void drawOutputThreeLongSlot(DrawContext context, int x, int y, Layer layer) {
        if (layer == Layer.BACKGROUND) {
            x += this.x;
            y += this.y;
        }
        drawOutputThreeLongSlotBuilder(context, this, x - 5, y - 5);
    }

    public void drawOutputThreeLongSlotBuilder(DrawContext context, Screen gui, int posX, int posY) {
        ScreenUtil.RendererUtil.drawTexture(new DrawObjectDM(context), GuiBuilder.GUI_ELEMENTS, posX, posY, 174, 0, 26 - 4, 26);
        ScreenUtil.RendererUtil.drawTexture(new DrawObjectDM(context), GuiBuilder.GUI_ELEMENTS, posX + 22, posY, 174 + 4, 0, 26 - 8, 26);
        ScreenUtil.RendererUtil.drawTexture(new DrawObjectDM(context), GuiBuilder.GUI_ELEMENTS, posX + 40, posY, 174 + 4, 0, 26 - 4, 26);
    }

    public void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
        Layer layer = Layer.FOREGROUND;
        builder.drawProgressBar(context, this, tile.getProgressScaled(100), 100, 76, 48, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
        builder.drawMultiEnergyBar(context, this, 9, 19, (int) tile.getEnergy(), (int) tile.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }
}
