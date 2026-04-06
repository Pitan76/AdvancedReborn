package net.pitan76.advancedreborn.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.pitan76.advancedreborn.Defines;
import net.pitan76.advancedreborn.tile.RenamingMachineTile;
import net.pitan76.mcpitanlib.api.network.ClientNetworking;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiBuilder;
import reborncore.common.screen.BuiltScreenHandler;

public class GuiRenamingMachine extends GuiBase<BuiltScreenHandler> {

    public EditBox fieldBox;
    public RenamingMachineTile tile;
    public GuiRenamingMachine(int syncId, Player player, RenamingMachineTile tile) {
        super(player, tile, tile.createScreenHandler(syncId, player));
        this.tile = tile;
    }

    public boolean isConfigEnabled() {
        return true;
    }

    public void init() {
        super.init();
        //fieldBox = new EditBox(font, x + 98,  y + 7, 70, 9, TextUtil.literal(""));
        fieldBox = new EditBox(font, titleLabelX + 55, titleLabelY + 20, 98, 15, TextUtil.literal(""));
        getFieldBox().setValue(tile.getName());
        getFieldBox().setCanLoseFocus(false);
        ScreenUtil.TextFieldUtil.setFocused(getFieldBox(), true);
        getFieldBox().setMaxLength(2048);
        addWidget(getFieldBox());
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (fieldBox.isFocused()) {
            if (input.key() != 256) {
                return fieldBox.keyPressed(input);
            }
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyEvent input) {
        if (fieldBox.isFocused()) {
            if (input.key() != 256) {
                tile.setNameClient(getFieldBox().getValue());
                sendPacket();
            }
        }
        return super.keyReleased(input);
    }

    public void sendPacket() {
        FriendlyByteBuf buf = PacketByteUtil.create();
        CompoundTag data = NbtUtil.create();
        data.putString("name", getFieldBox().getValue());
        data.putDouble("x", tile.getBlockPos().getX());
        data.putDouble("y", tile.getBlockPos().getY());
        data.putDouble("z", tile.getBlockPos().getZ());
        buf.writeNbt(data);
        ClientNetworking.send(Defines.RENAMING_PACKET_ID.toMinecraft(), buf);
    }

    @Override
    public void removed() {
        super.removed();
        //client.keyboard.setRepeatEvents(false);
    }

    public EditBox getFieldBox() {
        return fieldBox;
    }

    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float lastFrameDuration) {
        super.extractBackground(context, mouseX, mouseY, lastFrameDuration);
        Layer layer = Layer.BACKGROUND;
        drawSlot(context, 55, 45, layer);
        drawOutputSlot(context, 101, 45, layer);
        drawSlot(context, 8, 72, layer);
        getFieldBox().extractWidgetRenderState(context, mouseX, mouseY, lastFrameDuration);
    }

    public void extractLabels(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        super.extractLabels(context, mouseX, mouseY);
        Layer layer = Layer.FOREGROUND;
        builder.drawProgressBar(context, this, tile.getProgressScaled(100), 100, 76, 48, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
        builder.drawMultiEnergyBar(context, this, 9, 19, (int) tile.getEnergy(), (int) tile.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }
}
