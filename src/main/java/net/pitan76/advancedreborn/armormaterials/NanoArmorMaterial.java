package net.pitan76.advancedreborn.armormaterials;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;
import net.pitan76.mcpitanlib.api.item.ArmorEquipmentType;
import net.pitan76.mcpitanlib.api.item.CompatibleArmorMaterial;
import net.pitan76.mcpitanlib.api.sound.CompatSoundEvents;
import net.pitan76.mcpitanlib.api.util.IdentifierUtil;

public class NanoArmorMaterial implements CompatibleArmorMaterial {

    public static CompatibleArmorMaterial NANO = new NanoArmorMaterial();

    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};

    @Override
    public int getDurability(ArmorEquipmentType type) {
        if (type.equals(ArmorEquipmentType.HEAD)) {
            return BASE_DURABILITY[0] * 33;
        } else if (type.equals(ArmorEquipmentType.CHEST)) {
            return BASE_DURABILITY[1] * 33;
        } else if (type.equals(ArmorEquipmentType.LEGS)) {
            return BASE_DURABILITY[2] * 33;
        } else if (type.equals(ArmorEquipmentType.FEET)) {
            return BASE_DURABILITY[3] * 33;
        }
        return 0;
    }

    @Override
    public int getProtection(ArmorEquipmentType type) {
        if (type.equals(ArmorEquipmentType.HEAD)) {
            return 3;
        } else if (type.equals(ArmorEquipmentType.CHEST)) {
            return 6;
        } else if (type.equals(ArmorEquipmentType.LEGS)) {
            return 8;
        } else if (type.equals(ArmorEquipmentType.FEET)) {
            return 3;
        }
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public SoundEvent getEquipSound() {
        return CompatSoundEvents.ITEM_ARMOR_EQUIP_DIAMOND.get();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }

    @Override
    public String getName() {
        return "nano";
    }

    @Override
    public Identifier getId() {
        return IdentifierUtil.id("nano");
    }

    @Override
    public float getToughness() {
        return 2.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}
