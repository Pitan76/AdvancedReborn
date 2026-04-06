package net.pitan76.advancedreborn.blocks;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.pitan76.advancedreborn.GuiTypes;
import net.pitan76.advancedreborn.tile.EnchantmentExtractorTile;
import net.pitan76.mcpitanlib.api.block.v2.CompatibleBlockSettings;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import reborncore.api.blockentity.IMachineGuiHandler;

public class EnchantmentExtractor extends AdvancedMachineBlock {
    public EnchantmentExtractor(CompatibleBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(TileCreateEvent e) {
        return new EnchantmentExtractorTile(e);
    }

    @Override
    public IMachineGuiHandler getGui() {
        return GuiTypes.ENCHANTMENT_EXTRACTOR;
    }
}
