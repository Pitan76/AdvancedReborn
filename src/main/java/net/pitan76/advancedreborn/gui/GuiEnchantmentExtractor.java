package net.pitan76.advancedreborn.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.pitan76.advancedreborn.tile.EnchantmentExtractorTile;
import net.pitan76.mcpitanlib.api.client.render.DrawObjectDM;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.client.RenderUtil;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.screen.BuiltScreenHandler;

import static net.pitan76.advancedreborn.AdvancedReborn.INSTANCE;

public class GuiEnchantmentExtractor extends GuiBase<BuiltScreenHandler> {

    public static final CompatIdentifier GUI = INSTANCE.compatId("textures/gui/slot_texture.png");

    public EnchantmentExtractorTile tile;
    public GuiEnchantmentExtractor(int syncId, Player player, EnchantmentExtractorTile tile) {
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

        RenderUtil.setShaderTexture(0, GuiBuilder.GUI_ELEMENTS);
        drawSlot(context, 40, 25, layer); // Input slot
        drawSlot(context, 40, 65, layer); // Output slot

        drawSlot(context, 82, 40, layer);
        drawSlot(context, 100, 40, layer);
        drawSlot(context, 118, 40, layer);
        drawSlot(context, 136, 40, layer);
        drawSlot(context, 82, 58, layer);
        drawSlot(context, 100, 58, layer);
        drawSlot(context, 118, 58, layer);
        drawSlot(context, 136, 58, layer);

        drawSlot(context, 8, 72, layer);

        //RenderSystem.setShaderTexture(0, GUI);
        // Book slot
        ScreenUtil.RendererUtil.drawTexture(new DrawObjectDM(context), GUI.toMinecraft(), 60 + this.leftPos - 1, 25 + this.topPos - 1, 0, 0, 18, 18);
    }

    public void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        super.extractLabels(context, mouseX, mouseY);
        Layer layer = Layer.FOREGROUND;
        builder.drawProgressBar(context, this, tile.getProgressScaled(100), 100, 43, 45, mouseX, mouseY, GuiBuilder.ProgressDirection.DOWN, layer);
        builder.drawMultiEnergyBar(context, this, 9, 19, (int) tile.getEnergy(), (int) tile.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }
}
