package net.pitan76.advancedreborn.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.pitan76.advancedreborn.tile.RotaryGrinderTile;
import net.pitan76.mcpitanlib.api.client.render.DrawObjectDM;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.screen.BuiltScreenHandler;

public class GuiRotaryGrinder extends GuiBase<BuiltScreenHandler> {

    public RotaryGrinderTile tile;
    public GuiRotaryGrinder(int syncId, Player player, RotaryGrinderTile tile) {
        super(player, tile, tile.createScreenHandler(syncId, player));
        this.tile = tile;
        backgroundWidth = 176;
        backgroundHeight = 166;
    }

    public boolean isConfigEnabled() {
        return true;
    }

    public void init() {
        super.init();
    }

    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float lastFrameDuration) {
        super.extractBackground(context, mouseX, mouseY, lastFrameDuration);
        Layer layer = Layer.BACKGROUND;
        drawSlot(context, 55, 45, layer);
        drawOutputTwoLongSlot(context, 101, 45, layer);
        //drawOutputSlot(context, 101 + 18, 45, layer);
        drawSlot(context, 8, 72, layer);
        drawText(context, TextUtil.translatable("advanced_reborn.advanced_machine.text.speed", tile.getHeatPer() + "%"), 75, 70, 0, layer);
    }

    public void drawOutputTwoLongSlot(GuiGraphicsExtractor context, int x, int y, Layer layer) {
        if (layer == Layer.BACKGROUND) {
            x += this.x;
            y += this.y;
        }
        drawOutputTwoLongSlotBuilder(context, this, x - 5, y - 5);
    }

    public void drawOutputTwoLongSlotBuilder(GuiGraphicsExtractor context, Screen gui, int posX, int posY) {
        ScreenUtil.RendererUtil.drawTexture(new DrawObjectDM(context), GuiBuilder.GUI_ELEMENTS, posX, posY, 174, 0, 26 - 4, 26);
        ScreenUtil.RendererUtil.drawTexture(new DrawObjectDM(context), GuiBuilder.GUI_ELEMENTS, posX + 22, posY, 174 + 4, 0, 26 - 4, 26);
    }

    public void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        super.extractLabels(context, mouseX, mouseY);
        Layer layer = Layer.FOREGROUND;
        builder.drawProgressBar(context, this, tile.getProgressScaled(100), 100, 76, 48, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
        builder.drawMultiEnergyBar(context, this, 9, 19, (int) tile.getEnergy(), (int) tile.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }
}
