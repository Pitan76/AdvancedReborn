package ml.pkom.advancedreborn;

import ml.pkom.advancedreborn.blocks.RaySolar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AdvancedReborn implements ModInitializer {
    public static String MOD_ID = "advanced_reborn";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static AdvancedReborn INSTANCE;

    public static ItemGroup AR_GROUP = FabricItemGroupBuilder.build(
            createID("item_group"),
            () -> new ItemStack(Items.CHARGE_PAD_MK_1));

    @Override
    public void onInitialize() {
        Items.init();
        Blocks.init();
        Tiles.init();
        Particles.init();
    }

    public static List<RaySolar> solars = new ArrayList<>();

    static {
        solars.add((RaySolar) Blocks.RAY_SOLAR_1);
    }

    public static Identifier createID(String id) {
        return new Identifier(MOD_ID, id);
    }
}