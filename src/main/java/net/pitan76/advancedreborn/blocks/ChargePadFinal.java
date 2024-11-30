package net.pitan76.advancedreborn.blocks;

import net.minecraft.item.ItemStack;
import net.pitan76.advancedreborn.api.Energy;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.entity.Player;
import net.pitan76.mcpitanlib.api.event.block.EntityCollisionEvent;
import team.reborn.energy.api.base.SimpleEnergyItem;

public class ChargePadFinal extends ChargePad {

    public ChargePadFinal(CompatibleBlockSettings settings, int multiple) {
        super(settings, multiple);
    }

    @Override
    public void onEntityCollision(EntityCollisionEvent e) {
        if (e.isClient()) return;
        if (!(e.hasPlayerEntity())) return;
        Player player = new Player(e.getPlayerEntity().get());

        boolean needCharge = false;
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack invStack = player.getInventory().getStack(i);

            if (invStack.isEmpty()) {
                continue;
            }

            if (Energy.isHolder(invStack)) {
                SimpleEnergyItem energy = Energy.of(invStack);
                if (energy.getStoredEnergy(invStack) >= energy.getEnergyCapacity(invStack)) continue;
                needCharge = true;
            }
        }
        if (needCharge) {
            super.onEntityCollision(e);
        }
    }
}
