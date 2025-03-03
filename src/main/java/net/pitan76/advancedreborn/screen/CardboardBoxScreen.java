package net.pitan76.advancedreborn.screen;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.pitan76.advancedreborn.Defines;
import net.pitan76.mcpitanlib.api.client.CompatInventoryScreen;
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

public class CardboardBoxScreen extends CompatInventoryScreen {
    private static final CompatIdentifier TEXTURE = INSTANCE.compatId("textures/gui/cardboard_box.png");
    private TextFieldWidget noteBox;

    private final CardboardBoxScreenHandler handler;

    public CardboardBoxScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundHeight = 133;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        this.handler = (CardboardBoxScreenHandler) handler;
    }

    @Override
    public CompatIdentifier getCompatTexture() {
        return TEXTURE;
    }

    @Override
    public void drawBackgroundOverride(DrawBackgroundArgs args) {
        ScreenUtil.setBackground(getTexture());
        if (client == null) return;
        client.getTextureManager().bindTexture(getTexture());
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        args.getDrawObjectDM().getContext().drawTexture(getTexture(), x, y, 0, 0, backgroundWidth, backgroundHeight);
        getNoteBox().render(args.drawObjectDM.getContext(), args.mouseX, args.mouseY, args.delta);
        RenderUtil.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void initOverride() {
        super.initOverride();
        noteBox = new TextFieldWidget(textRenderer, x + 98,  y + 7, 70, 9, TextUtil.literal(""));
        getNoteBox().setText(handler.tmpNote);
        getNoteBox().setDrawsBackground(false);
        getNoteBox().setFocusUnlocked(false);
        ScreenUtil.TextFieldUtil.setFocused(getNoteBox(), true);
        getNoteBox().setMaxLength(2048);
        addSelectableChild(getNoteBox());
    }

    public void close() {
        super.close();
        PacketByteBuf buf = PacketByteUtil.create();
        NbtCompound data = NbtUtil.create();
        data.putString("note", getNote());
        data.putDouble("x", handler.pos.getX());
        data.putDouble("y", handler.pos.getY());
        data.putDouble("z", handler.pos.getZ());
        //AdvancedReborn.LOGGER.info("nbt: " + data);
        buf.writeNbt(data);
        ClientNetworking.send(Defines.CARDBOARD_BOX_CLOSE_PACKET_ID.toMinecraft(), buf);
    }

    public boolean keyPressed(KeyEventArgs args) {
        if (getNoteBox().keyPressed(args.keyCode, args.scanCode, args.modifiers)) return true;
        return super.keyPressed(args);
    }

    public void removed() {
        super.removed();
        ScreenUtil.setRepeatEvents(false);
    }

    public TextFieldWidget getNoteBox() {
        return noteBox;
    }

    public void setNoteBox(TextFieldWidget noteBox) {
        this.noteBox = noteBox;
    }

    public String getNote() {
        return getNoteBox().getText();
    }
}
