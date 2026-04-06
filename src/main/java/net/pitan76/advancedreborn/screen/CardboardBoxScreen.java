package net.pitan76.advancedreborn.screen;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.pitan76.advancedreborn.Defines;
import net.pitan76.mcpitanlib.api.client.gui.screen.CompatInventoryScreen;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.DrawBackgroundArgs;
import net.pitan76.mcpitanlib.api.client.render.handledscreen.KeyEventArgs;
import net.pitan76.mcpitanlib.api.network.ClientNetworking;
import net.pitan76.mcpitanlib.api.network.PacketByteUtil;
import net.pitan76.mcpitanlib.api.util.CompatIdentifier;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.mcpitanlib.api.util.TextUtil;
import net.pitan76.mcpitanlib.api.util.client.RenderUtil;
import net.pitan76.mcpitanlib.api.util.client.ScreenUtil;

import static net.pitan76.advancedreborn.AdvancedReborn.INSTANCE;

public class CardboardBoxScreen extends CompatInventoryScreen<CardboardBoxScreenHandler> {
    private static final CompatIdentifier TEXTURE = INSTANCE.compatId("textures/gui/cardboard_box.png");
    private EditBox noteBox;

    private final CardboardBoxScreenHandler handler;

    public CardboardBoxScreen(CardboardBoxScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        backgroundHeight = 133;
        this.inventoryLabelY = this.backgroundHeight - 94;
        this.handler = handler;
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        return TEXTURE;
    }

    @Override
    public void drawBackgroundOverride(DrawBackgroundArgs args) {
        ScreenUtil.setBackground(getCompatTexture().toMinecraft());
        if (client == null) return;
        //client.getTextureManager().bindTexture(getTexture());
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        ScreenUtil.RendererUtil.drawTexture(args.getDrawObjectDM(), getCompatTexture(), x, y, 0, 0, backgroundWidth, backgroundHeight);
        getNoteBox().extractWidgetRenderState(args.drawObjectDM.getContext(), args.mouseX, args.mouseY, args.delta);
        RenderUtil.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void initOverride() {
        super.initOverride();
        noteBox = new EditBox(font, x + 98,  y + 7, 70, 9, TextUtil.literal(""));
        getNoteBox().setValue(handler.tmpNote);
        getNoteBox().setBordered(false);
        getNoteBox().setCanLoseFocus(false);
        ScreenUtil.TextFieldUtil.setFocused(getNoteBox(), true);
        getNoteBox().setMaxLength(2048);
        addWidget(getNoteBox());
    }

    public void closeOverride() {
        super.closeOverride();
        FriendlyByteBuf buf = PacketByteUtil.create();
        CompoundTag data = NbtUtil.create();
        data.putString("note", getNote());
        data.putDouble("x", handler.pos.getX());
        data.putDouble("y", handler.pos.getY());
        data.putDouble("z", handler.pos.getZ());
        //AdvancedReborn.LOGGER.info("nbt: " + data);
        buf.writeNbt(data);
        ClientNetworking.send(Defines.CARDBOARD_BOX_CLOSE_PACKET_ID.toMinecraft(), buf);
    }

    public boolean keyPressed(KeyEventArgs args) {
        if (getNoteBox().keyPressed(new KeyEvent(args.keyCode, args.scanCode, args.modifiers))) return true;
        return super.keyPressed(args);
    }

    public void removed() {
        super.removed();
        ScreenUtil.setRepeatEvents(false);
    }

    public EditBox getNoteBox() {
        return noteBox;
    }

    public void setNoteBox(EditBox noteBox) {
        this.noteBox = noteBox;
    }

    public String getNote() {
        return getNoteBox().getValue();
    }
}
