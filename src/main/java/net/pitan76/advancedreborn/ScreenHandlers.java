package net.pitan76.advancedreborn;

import net.minecraft.screen.ScreenHandlerType;
import net.pitan76.advancedreborn.screen.CardboardBoxScreenHandler;
import net.pitan76.mcpitanlib.api.gui.ExtendedScreenHandlerTypeBuilder;
import net.pitan76.mcpitanlib.api.registry.result.RegistryResult;

import static net.pitan76.advancedreborn.AdvancedReborn.INSTANCE;
import static net.pitan76.advancedreborn.AdvancedReborn.registry;

public class ScreenHandlers {
    public static RegistryResult<ScreenHandlerType<CardboardBoxScreenHandler>> CARDBOARD_BOX_SCREEN_HANDLER;

    public static void init() {
        CARDBOARD_BOX_SCREEN_HANDLER = registry.registerScreenHandlerType(INSTANCE.compatId("cardboard_box"), new ExtendedScreenHandlerTypeBuilder<>(CardboardBoxScreenHandler::new));
    }
}
