package net.pitan76.advancedreborn.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.pitan76.advancedreborn.tile.LoggingMachineTile;
import reborncore.client.gui.GuiBase;
import reborncore.common.screen.BuiltScreenHandler;

public class GuiLoggingMachine extends GuiBase<BuiltScreenHandler> {

    public LoggingMachineTile tile;
    public GuiLoggingMachine(int syncId, Player player, LoggingMachineTile tile) {
        super(player, tile, tile.createScreenHandler(syncId, player));
        this.tile = tile;
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
        drawSlot(context, 55, 50, layer);

        drawSlot(context, 55, 72, layer);
        drawSlot(context, 73, 72, layer);
        drawSlot(context, 91, 72, layer);
        drawSlot(context, 109, 72, layer);
        drawSlot(context, 127, 72, layer);

        drawSlot(context, 8, 72, layer);
    }

    public void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        super.extractLabels(context, mouseX, mouseY);
        Layer layer = Layer.FOREGROUND;
        builder.drawMultiEnergyBar(context, this, 9, 19, (int) tile.getEnergy(), (int) tile.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }
}
