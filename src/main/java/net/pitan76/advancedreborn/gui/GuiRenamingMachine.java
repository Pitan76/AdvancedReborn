package net.pitan76.advancedreborn.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
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

    public TextFieldWidget fieldBox;
    public RenamingMachineTile tile;
    public GuiRenamingMachine(int syncId, PlayerEntity player, RenamingMachineTile tile) {
        super(player, tile, tile.createScreenHandler(syncId, player));
        this.tile = tile;
    }

    public boolean isConfigEnabled() {
        return true;
    }

    public void init() {
        super.init();
        //fieldBox = new TextFieldWidget(textRenderer, x + 98,  y + 7, 70, 9, TextUtil.literal(""));
        fieldBox = new TextFieldWidget(textRenderer, x + 55,  y + 20, 98, 15, TextUtil.literal(""));
        getFieldBox().setText(tile.getName());
        getFieldBox().setFocusUnlocked(false);
        ScreenUtil.TextFieldUtil.setFocused(getFieldBox(), true);
        getFieldBox().setMaxLength(2048);
        addSelectableChild(getFieldBox());
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (fieldBox.isFocused()) {
            if (input.getKeycode() != 256) {
                return fieldBox.keyPressed(input);
            }
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        if (fieldBox.isFocused()) {
            if (input.getKeycode() != 256) {
                tile.setNameClient(getFieldBox().getText());
                sendPacket();
            }
        }
        return super.keyReleased(input);
    }

    public void sendPacket() {
        PacketByteBuf buf = PacketByteUtil.create();
        NbtCompound data = NbtUtil.create();
        data.putString("name", getFieldBox().getText());
        data.putDouble("x", tile.getPos().getX());
        data.putDouble("y", tile.getPos().getY());
        data.putDouble("z", tile.getPos().getZ());
        buf.writeNbt(data);
        ClientNetworking.send(Defines.RENAMING_PACKET_ID.toMinecraft(), buf);
    }

    @Override
    public void removed() {
        super.removed();
        //client.keyboard.setRepeatEvents(false);
    }

    public TextFieldWidget getFieldBox() {
        return fieldBox;
    }

    public void drawBackground(DrawContext context, float lastFrameDuration, int mouseX, int mouseY) {
        super.drawBackground(context, lastFrameDuration, mouseX, mouseY);
        Layer layer = Layer.BACKGROUND;
        drawSlot(context, 55, 45, layer);
        drawOutputSlot(context, 101, 45, layer);
        drawSlot(context, 8, 72, layer);
        getFieldBox().render(context, mouseX, mouseY, lastFrameDuration);
    }

    public void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
        Layer layer = Layer.FOREGROUND;
        builder.drawProgressBar(context, this, tile.getProgressScaled(100), 100, 76, 48, mouseX, mouseY, GuiBuilder.ProgressDirection.RIGHT, layer);
        builder.drawMultiEnergyBar(context, this, 9, 19, (int) tile.getEnergy(), (int) tile.getMaxStoredPower(), mouseX, mouseY, 0, layer);
    }
}
