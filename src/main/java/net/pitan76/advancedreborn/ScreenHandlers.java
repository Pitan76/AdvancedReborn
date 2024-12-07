package net.pitan76.advancedreborn;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.pitan76.advancedreborn.screen.CardboardBoxScreenHandler;

import static net.pitan76.advancedreborn.AdvancedReborn.registry;

public class ScreenHandlers {
    public static ScreenHandlerType<CardboardBoxScreenHandler> CARDBOARD_BOX_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(CardboardBoxScreenHandler::new);

    public static void init() {
        registry.registerScreenHandlerType(AdvancedReborn._id("cardboard_box"), () -> CARDBOARD_BOX_SCREEN_HANDLER);
    }
}
