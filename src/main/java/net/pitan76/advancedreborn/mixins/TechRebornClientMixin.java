package net.pitan76.advancedreborn.mixins;

import net.pitan76.advancedreborn.client.ClientGuiTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import techreborn.client.TechRebornClient;

@Mixin(TechRebornClient.class)
public class TechRebornClientMixin {
    @Inject(method = "onInitializeClient", at = @At("HEAD"), remap = false)
    public void onInitializeClient(CallbackInfo ci) {
        ClientGuiTypes.init();
    }
}
